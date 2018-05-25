package com.cool.eye.func.view.trend.mode;


import com.cool.eye.func.view.trend.utils.Utils;

public class GoldTariff {
    private long time;
    private double price;

    public long getTime() {
        return time;
    }

    public String getTimeStr() {
        return Utils.formatDate(time);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public int getPriceInt() {
        return (int) price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GoldTariff{" +
                "time=" + time +
                ", price=" + price +
                '}';
    }
}
