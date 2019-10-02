package com.banannaWaffleProductions.stockcenter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InfoAction implements View.OnClickListener{
    /*Class that handles the responsibility of retrieving detailed information about a Stock from
    the Alpha Vantage API*/
    private static final String TAG = InfoAction.class.getSimpleName();
    private Stock stock;
    private FragmentManager fragmentManager; //Fragment Manager necessary for error handling.
    private Activity invoker; //The activity that invoked this instance of InfoAction.
    private Activity invoked; //The activity to be invoked by this instance of InfoAction.
    private DetailedStock detailedStock;

    public InfoAction(Stock stock, FragmentManager fragmentManager, Activity invoker,
                      Activity invoked){
        this.stock = stock;
        this.fragmentManager = fragmentManager;
        this.invoker = invoker;
        this.invoked = invoked;
    }

    //Getter Functions/Accessor Methods.
    public Stock getStock(){
        return this.stock;
    }
    public FragmentManager getFragmentManager(){
        return this.fragmentManager;
    }
    public Activity getInvoker() {
        return this.invoker;
    }
    public Activity getInvoked(){
        return this.invoked;
    }
    public DetailedStock getDetailedStock(){
        return this.detailedStock;
    }

    //Setter Functions/Mutator Methods.
    public void setStock(Stock stock){
        this.stock = stock;
    }
    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }
    public void setInvoker(Activity invoker){
        this.invoker = invoker;
    }
    public void setInvoked(Activity invoked){
        this.invoked = invoked;
    }
    public void setDetailedStock(DetailedStock detailedStock){
        this.detailedStock = detailedStock;
    }

    @Override
    public String toString(){
        return "InfoAction";
    }
    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(other == null){
            return false;
        }
        if(this.getClass() != other.getClass()){
            return false;
        }
        InfoAction infoAction = (InfoAction) other;
        return this.stock.equals(infoAction.getStock()) &&
                this.fragmentManager.equals(infoAction.getFragmentManager()) &&
                this.invoker.equals(infoAction.getInvoker()) &&
                this.invoked.equals(infoAction.getInvoked()) &&
                this.detailedStock.equals(infoAction.getDetailedStock());
    }
    @Override
    public int hashCode(){
        return Objects.hash(this.stock, this.fragmentManager, this.invoker, this.invoked,
                this.detailedStock);
    }

    @Override
    public void onClick(View view){
        String symbol = this.stock.getSymbol();
        String URLSent = "https://www.alphavantage.co/query?function=" +
                "TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + R.string.API_key;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URLSent).build();
        Call call = client.newCall(request);
        final ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure");
                errorDialogFragment.show(fragmentManager, TAG);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                Log.i(TAG, "onResponse");
                try {
                    if(response.isSuccessful()) {
                        Log.i(TAG, "Response Successful");
                        String dataRecieved = response.body().string();
                        Log.i(TAG, dataRecieved);
                        JSONObject jsonObject = new JSONObject(dataRecieved);
                        JSONObject data = jsonObject.getJSONObject("Time Series (Daily)");
                        Iterator<String> keys = data.keys();
                        String latest = keys.next();
                        JSONObject firstEntry = data.getJSONObject(latest);
                        String open = "1. open";
                        String high = "2. high";
                        String low = "3. low";
                        String close = "4. close";
                        String volume = "5. volume";
                        DetailedStock detailedStock = new DetailedStock(InfoAction.this.stock,
                                firstEntry.getString(open), firstEntry.getString(high),
                                firstEntry.getString(low), firstEntry.getString(close),
                                firstEntry.getString(volume));
                        Intent intent = new Intent(InfoAction.this.invoker,
                                InfoAction.this.invoked.getClass());
                        intent.putExtra("detailedStock", detailedStock);
                        InfoAction.this.invoker.startActivity(intent);
                    }
                    else{
                        Log.d(TAG, "Response Unsuccessful");
                        errorDialogFragment.show(fragmentManager, TAG);
                    }
                }
                catch (IOException exception){
                    Log.e(TAG, "IOException", exception);
                    errorDialogFragment.show(fragmentManager, TAG);
                }
                catch (JSONException exception){
                    Log.e(TAG, "JSONException", exception);
                    errorDialogFragment.show(fragmentManager, TAG);
                }
            }
        });
    }
}
