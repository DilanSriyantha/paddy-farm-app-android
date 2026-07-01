package com.dtechsolutions.paddyfarm.data.models;

import java.util.Date;

public class Cultivation {
    private int id;
    private Date startDate;
    private String seedType;
    private float sizeInAcres;
    private String paddyVariety;

    public Cultivation(int id, Date startDate, String seedType, float sizeInAcres, String paddyVariety) {
        this.id = id;
        this.startDate = startDate;
        this.seedType = seedType;
        this.sizeInAcres = sizeInAcres;
        this.paddyVariety = paddyVariety;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSeedType() {
        return seedType;
    }

    public void setSeedType(String seedType) {
        this.seedType = seedType;
    }

    public float getSizeInAcres() {
        return sizeInAcres;
    }

    public void setSizeInAcres(float sizeInAcres) {
        this.sizeInAcres = sizeInAcres;
    }

    public String getPaddyVariety() {
        return paddyVariety;
    }

    public void setPaddyVariety(String paddyVariety) {
        this.paddyVariety = paddyVariety;
    }
}
