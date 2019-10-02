package com.banannaWaffleProductions.stockcenter;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class Stock implements Parcelable {
    //Class that represents a Stock in the Application.
    private final String symbol;
    private final String name;
    private final String type;
    private final String region;
    private final String marketOpen;
    private final String marketClose;
    private final String timeZone;
    private final String currency;

    public Stock(String symbol, String name, String type, String region, String marketOpen,
                 String marketClose, String timeZone, String currency){
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.region = region;
        this.marketOpen = marketOpen;
        this.marketClose = marketClose;
        this.timeZone = timeZone;
        this.currency = currency;
    }

    //Getter Functions/Accessor Methods.
    public String getSymbol(){
        return this.symbol;
    }
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public String getRegion(){
        return this.region;
    }
    public String getMarketOpen(){
        return this.marketOpen;
    }
    public String getMarketClose(){
        return this.marketClose;
    }
    public String getTimeZone(){
        return this.timeZone;
    }
    public String getCurrency(){
        return this.currency;
    }

    @Override
    public String toString(){
        return "Stock:" + this.symbol;
    }
    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(other ==  null){
            return false;
        }
        if(this.getClass() != other.getClass()){
            return false;
        }
        Stock stock = (Stock) other;
        return Objects.equals(this.symbol, stock.getSymbol()) &&
                Objects.equals(this.name, stock.getName()) &&
                Objects.equals(this.type, stock.getType()) &&
                Objects.equals(this.region, stock.getRegion()) &&
                Objects.equals(this.timeZone, stock.getTimeZone()) &&
                Objects.equals(this.currency, stock.getCurrency());
    }
    @Override
    public int hashCode(){
        return Objects.hash(this.symbol, this.name, this.type, this.region, this.timeZone,
                this.currency);
    }

    //Code that allows the Class to implement the Parcelable Interface.
    protected Stock(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        type = in.readString();
        region = in.readString();
        marketOpen = in.readString();
        marketClose = in.readString();
        timeZone = in.readString();
        currency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(region);
        dest.writeString(marketOpen);
        dest.writeString(marketClose);
        dest.writeString(timeZone);
        dest.writeString(currency);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };
}