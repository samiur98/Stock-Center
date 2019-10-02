package com.banannaWaffleProductions.stockcenter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Class responsible for the MainActivity of the app.
    private final String TAG = MainActivity.class.getSimpleName();
    private SearchView mSearchView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private LinearLayout mLinearLayout;
    private HashMap<String, Stock> userData;
    private String sharedPreferencesString;
    private String userDataString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mSearchView = findViewById(R.id.main_search_view);
        this.mSearchView.setOnQueryTextListener(new SearchAction(this,
                new SearchResultActivity(), getSupportFragmentManager()));
        this.mLinearLayout = findViewById(R.id.main_scroll_view_linear_layout);
        this.sharedPreferencesString = getResources().getString
                (R.string.sharedPreferencesString);
        this.userDataString = getResources().getString(R.string.UserDataString);
    }

    @Override
    protected void onResume(){
        Log.i(TAG, "onResume");
        super.onResume();
        this.mSharedPreferences = getSharedPreferences(sharedPreferencesString,
                Context.MODE_PRIVATE);
        this.mEditor = this.mSharedPreferences.edit();
        String GSONdata = this.mSharedPreferences.getString(userDataString, "");
        Gson gson = new Gson();
        if(GSONdata.equals("")){
            //Code to be run when the app is first opened.
            Log.i(TAG, "first time");
            HashMap<String, Stock> userData = new HashMap<>();
            String json = gson.toJson(userData, userData.getClass());
            this.mEditor.putString(userDataString, json);
            this.mEditor.apply();
        }
        else{
            //Code that generates the User List of Stocks.
            java.lang.reflect.Type type = new TypeToken<HashMap<String, Stock>>(){}.getType();
            this.userData = gson.fromJson(GSONdata, type);
            Log.d(TAG, this.userData.entrySet().toString());
            this.mLinearLayout.removeAllViews();
            for(Map.Entry<String, Stock> entry: this.userData.entrySet()){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(200, 32, 200, 32);
                String text = entry.getKey() + "\n" + entry.getValue().getName() + "\n" +
                        entry.getValue().getRegion();
                TextView textView = new TextView(this);
                textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textView.setText(text);
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setLayoutParams(params);
                textView.setOnClickListener(new InfoAction(entry.getValue(),
                        getSupportFragmentManager(), this, new UserInfoActivity()));
                this.mLinearLayout.addView(textView);
            }
        }

    }

    @Override
    public void onPause(){
        Log.i(TAG, "onPause");
        super.onPause();
        Log.d(TAG, this.userData.entrySet().toString());
        Gson gson = new Gson();
        String json = gson.toJson(this.userData, this.userData.getClass());
        this.mEditor.putString(userDataString, json);
        this.mEditor.apply();
    }
    
}
