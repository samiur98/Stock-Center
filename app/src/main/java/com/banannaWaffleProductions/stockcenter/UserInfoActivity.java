package com.banannaWaffleProductions.stockcenter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity {
    //Class responsible for the UserInfoActivity.
    //The UserInfoActivity shows details about Stocks in the user's list.
    private final static String TAG = UserInfoActivity.class.getSimpleName();
    private TextView mTextView;
    private Button mDeleteButton;
    private Button mBackButton;
    private DetailedStock detailedStock;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private HashMap<String, Stock> userData;
    private String sharedPreferencesString;
    private String userDataString;

    public UserInfoActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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
        Gson gson = new Gson();
        String gsonData = this.mSharedPreferences.getString(userDataString, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Stock>>(){}.getType();
        this.userData = gson.fromJson(gsonData, type);
        Log.d(TAG, this.userData.entrySet().toString());
        this.mTextView = findViewById(R.id.user_info_textView);
        this.mDeleteButton = findViewById(R.id.user_info_delete_button);
        this.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userData.containsKey(detailedStock.getSymbol())) {
                    userData.remove(detailedStock.getSymbol());
                    String message = detailedStock.getSymbol() +
                            " has been deleted from your list.";
                    Toast.makeText(UserInfoActivity.this, message,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    String message = detailedStock.getSymbol() +
                            " has already been deleted from your list.";
                    Toast.makeText(UserInfoActivity.this, message,
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        this.mBackButton = findViewById(R.id.user_info_back_button);
        this.mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        this.detailedStock = intent.getParcelableExtra("detailedStock");
        String text = this.displayText();
        this.mTextView.setText(text);
    }

    @Override
    protected void onPause(){
        Log.i(TAG, "onPause");
        super.onPause();
        Log.d(TAG, this.userData.entrySet().toString());
        Gson gson = new Gson();
        String json = gson.toJson(this.userData, this.userData.getClass());
        this.mEditor.putString(userDataString, json);
        this.mEditor.apply();
    }

    private String displayText(){
        //Method that creates a String that organizes Stock Data for displaying.
        String text = "SYMBOL: " + this.detailedStock.getSymbol() + "\n" +
                "NAME: " + this.detailedStock.getName() + "\n" +
                "TYPE: " + this.detailedStock.getType() + "\n" +
                "REGION: " + this.detailedStock.getRegion() + "\n" + "\n" +
                "MARKET OPEN: " + this.detailedStock.getMarketOpen() + " (" +
                this.detailedStock.getTimeZone() + ")\n" +
                "MARKET CLOSE: " + this.detailedStock.getMarketClose() + " (" +
                this.detailedStock.getTimeZone() + ")\n" + "\n" +
                "TODAY'S OPEN VALUE: " + this.detailedStock.getOpen() + " " +
                this.detailedStock.getCurrency() + "\n" +
                "TODAY'S HIGH VALUE: " + this.detailedStock.getHigh() + " " +
                this.detailedStock.getCurrency() + "\n" +
                "TODAY'S LOW VALUE: " + this.detailedStock.getLow() + " " +
                this.detailedStock.getCurrency() + "\n" +
                "TODAY'S CLOSE VALUE: " + this.detailedStock.getClose() + " " +
                this.detailedStock.getCurrency() + "\n" +
                "TODAY'S VOLUME: " + this.detailedStock.getVolume();
        return text;
    }
}
