package com.banannaWaffleProductions.stockcenter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAction implements SearchView.OnQueryTextListener {
    /* Class that handles the responsibility of retrieving stocks from the Alpha Vantage API,
    that match the search of a user. */
    private final String TAG = SearchAction.class.getSimpleName();
    private Activity invoker; //Activity that invoked this instance of SearchAction.
    private Activity invoked; //Activity that gets invoked by this instance of SearchAction.
    private FragmentManager fragmentManager; //Needed for handling errors.

    public SearchAction(Activity invoker, Activity invoked, FragmentManager fragmentManager){
        this.invoker = invoker;
        this.invoked = invoked;
        this.fragmentManager = fragmentManager;
    }

    //Getter Functions/Accessor Methods.
    public Activity getInvoker(){
        return this.invoker;
    }
    public Activity getInvoked(){
        return this.invoked;
    }
    public FragmentManager getFragmentManager(){
        return this.fragmentManager;
    }

    //Setter Functions/Mutator Methods.
    public void setInvoker(Activity activity){
        this.invoker = invoker;
    }
    public void setInvoked(Activity activity){
        this.invoked = invoked;
    }
    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public String toString(){
        return "SearchAction";
    }
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(other == null){
            return false;
        }
        if(this.getClass() == other.getClass()){
            return false;
        }
        SearchAction searchAction = (SearchAction) other;
        return this.invoker.equals(searchAction.getInvoker()) &&
                this.invoked.equals(searchAction.getInvoked()) &&
                this.fragmentManager.equals(searchAction.getFragmentManager());
    }
    public int hashCode(){
        return Objects.hash(this.invoker, this.invoked, this.fragmentManager);
    }

    @Override
    public boolean onQueryTextSubmit(String string){
        Log.i(TAG, "onQueryTextSubmit: " + string);
        this.dataRequest(string);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String string){
        return false;
    }

    private void dataRequest(String keyWord){
        //Getting Data from Alpha Vantage through http request.
        final ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        String FinalURL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="
                + keyWord + "&apikey=" + R.string.API_key;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(FinalURL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure");
                errorDialogFragment.show(fragmentManager, TAG);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                Log.i(TAG, "onResponse");
                try{
                    ArrayList<Stock> stockArray = new ArrayList<>();
                    String dataRecieved = response.body().string();
                    Log.i(TAG, dataRecieved);
                    if(response.isSuccessful()){
                        Log.i(TAG, "Response Successful");
                        JSONObject jsonObject = new JSONObject(dataRecieved);
                        JSONArray equityList = jsonObject.getJSONArray("bestMatches");
                        for(int i = 0; i < equityList.length(); i++ ){
                            JSONObject equity = equityList.getJSONObject(i);
                            String symbol = equity.getString("1. symbol");
                            String name = equity.getString("2. name");
                            String type = equity.getString("3. type");
                            String region = equity.getString("4. region");
                            String marketOpen = equity.getString("5. marketOpen");
                            String marketClose = equity.getString("6. marketClose");
                            String timeZone = equity.getString("7. timezone");
                            String currency = equity.getString("8. currency");
                            Log.i(TAG,  symbol + " " + name + " " + region);
                            Stock parsedStock = new Stock(symbol, name, type, region, marketOpen,
                                                            marketClose, timeZone, currency);
                            stockArray.add(parsedStock);
                        }
                        Log.i(TAG, stockArray.toString());
                        Intent intent = new Intent(invoker, invoked.getClass());
                        intent.putParcelableArrayListExtra("stockArray", stockArray);
                        invoker.startActivity(intent);

                    }
                    else{
                        Log.d(TAG, "Response Unsuccessful");
                        errorDialogFragment.show(fragmentManager, TAG);
                    }
                }
                catch (IOException e){
                    Log.e(TAG, "IOException", e);
                    errorDialogFragment.show(fragmentManager, TAG);
                }
                catch (JSONException e){
                    Log.e(TAG, "JSONException", e);
                    errorDialogFragment.show(fragmentManager, TAG);
                }
            }
        });
    }
}
