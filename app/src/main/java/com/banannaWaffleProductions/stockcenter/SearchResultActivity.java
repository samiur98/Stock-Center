package com.banannaWaffleProductions.stockcenter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;


public class SearchResultActivity extends AppCompatActivity {
    //Class responsible for the SearchResultActivity of the app.
    //The SearchResultActivity is rendered after a user makes a search request.
    private static final String TAG = SearchResultActivity.class.getSimpleName();
    private ImageButton mBackButton;
    private SearchView mSearchView;
    private LinearLayout mLinearLayout;

    public SearchResultActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.mLinearLayout = findViewById(R.id.search_result_scroll_view_linear_layout);
        this.mSearchView = findViewById(R.id.search_result_search_view);
        this.mBackButton = findViewById(R.id.search_result_back_button);
        this.mSearchView.setOnQueryTextListener(new SearchAction(this,
                new SearchResultActivity(), getSupportFragmentManager()));
        this.mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultActivity.this.mLinearLayout.removeAllViews();
                SearchResultActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume(){
        Log.i(TAG, "onResume");
        super.onResume();
        Intent intent = getIntent();
        if(intent != null){
            this.mLinearLayout.removeAllViews();
            ArrayList<Stock> stockArray = intent.getParcelableArrayListExtra("stockArray");
            Log.d(TAG, "SIZE:" + Integer.toString(stockArray.size()));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(200, 32, 200, 32);
            for(Stock stock : stockArray) {
                Log.i(TAG, stock.getSymbol());
                String text = stock.getSymbol() + "\n" + stock.getName() + "\n" + stock.getRegion();
                TextView textView = new TextView(this);
                textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textView.setText(text);
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setLayoutParams(params);
                textView.setOnClickListener(new InfoAction(stock, getSupportFragmentManager(),
                        this, new SearchInfoActivity()));
                this.mLinearLayout.addView(textView);
            }
        }
        else{
            Log.d(TAG, "Intent null");
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
            errorDialogFragment.show(this.getSupportFragmentManager(), TAG);
        }

    }

}
