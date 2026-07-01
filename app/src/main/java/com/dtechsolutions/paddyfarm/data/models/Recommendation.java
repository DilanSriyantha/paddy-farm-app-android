package com.dtechsolutions.paddyfarm.data.models;

public class Recommendation {
    private String watering_recommendation;
    private String fertilizer_recommendation;
    private String disease_and_pest_check;
    private String maintenance_tips;
    private String next_stage_prediction;

    public Recommendation(String watering_recommendation, String fertilizer_recommendation, String disease_and_pest_check, String maintenance_tips, String next_stage_prediction) {
        this.watering_recommendation = watering_recommendation;
        this.fertilizer_recommendation = fertilizer_recommendation;
        this.disease_and_pest_check = disease_and_pest_check;
        this.maintenance_tips = maintenance_tips;
        this.next_stage_prediction = next_stage_prediction;
    }

    public String getWateringRecommendation() {
        return watering_recommendation;
    }

    public void setWateringRecommendation(String watering_recommendation) {
        this.watering_recommendation = watering_recommendation;
    }

    public String getFertilizerRecommendation() {
        return fertilizer_recommendation;
    }

    public void setFertilizerRecommendation(String fertilizer_recommendation) {
        this.fertilizer_recommendation = fertilizer_recommendation;
    }

    public String getDiseaseAndPestCheck() {
        return disease_and_pest_check;
    }

    public void setDiseaseAndPestCheck(String disease_and_pest_check) {
        this.disease_and_pest_check = disease_and_pest_check;
    }

    public String getMaintenanceTips() {
        return maintenance_tips;
    }

    public void setMaintenanceTips(String maintenance_tips) {
        this.maintenance_tips = maintenance_tips;
    }

    public String getNextStagePrediction() {
        return next_stage_prediction;
    }

    public void setNextStagePrediction(String next_stage_prediction) {
        this.next_stage_prediction = next_stage_prediction;
    }
}
