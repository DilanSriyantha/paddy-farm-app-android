package com.dtechsolutions.paddyfarm.data.models;

public class Fertilizer {
    private String imgPath;
    private String name;
    private float pricePerKg;

    public Fertilizer(String imgPath, String name, float pricePerKg) {
        this.imgPath = imgPath;
        this.name = name;
        this.pricePerKg = pricePerKg;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(float pricePerKg) {
        this.pricePerKg = pricePerKg;
    }
}
