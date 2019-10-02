package com.banannaWaffleProductions.stockcenter;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class DetailedStock extends Stock {
    //Class that represents a detailed Stock in the Application.
    private final String open;
    private final String high;
    private final String low;
    private final String close;
    private final String volume;

    public DetailedStock(String symbol, String name, String type, String region, String marketOpen,
                         String marketClose, String timeZone, String currency, String open,
                         String high, String low, String close, String volume){
        super(symbol, name, type, region, marketOpen, marketClose, timeZone, currency);
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
    public DetailedStock(Stock stock, String open, String high, String low, String close,
                         String volume){
        super(stock.getSymbol(), stock.getName(), stock.getType(), stock.getRegion(),
                stock.getMarketOpen(), stock.getMarketClose(), stock.getTimeZone(),
                stock.getCurrency());
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    //Getter Functions/Accessor Methods.
    public String getOpen(){
        return this.open;
    }
    public String getHigh(){
        return this.high;
    }
    public String getLow(){
        return this.low;
    }
    public String getClose(){
        return this.close;
    }
    public String getVolume(){
        return this.volume;
    }

    @Override
    public String toString(){
        return "DetailedStock:" + this.getSymbol();
    }

    @Override
    public boolean equals(Object other){
        if(super.equals(other) == false){
            return false;
        }
        DetailedStock detailedStock = (DetailedStock) other;
        return this.open.equals(detailedStock.getOpen()) &&
                this.high.equals(detailedStock.getHigh()) &&
                this.low.equals(detailedStock.getLow()) &&
                this.close.equals(detailedStock.getClose()) &&
                this.volume.equals(detailedStock.getVolume());
    }

    @Override
    public int hashCode(){
        return super.hashCode() + (31 * Objects.hash(this.open, this.high, this.low,
                this.close, this.volume));
    }

    //Code that allows the class to implement the Parcelable Interface.
    protected DetailedStock(Parcel in) {
        super(in);
        this.open = in.readString();
        this.high = in.readString();
        this.low = in.readString();
        this.close = in.readString();
        this.volume = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(open);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(close);
        dest.writeString(volume);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DetailedStock> CREATOR = new Parcelable.
            Creator<DetailedStock>() {
        @Override
        public DetailedStock createFromParcel(Parcel in) {
            return new DetailedStock(in);
        }

        @Override
        public DetailedStock[] newArray(int size) {
            return new DetailedStock[size];
        }
    };

}
