package com.dtechsolutions.paddyfarm.data.models;

public class FertilizerPrice {
    private final String imageUrl;
    private final String name;
    private final float price;

    public FertilizerPrice(String imageUrl, String name, float price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "FertilizerPrice{" +
                "imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
