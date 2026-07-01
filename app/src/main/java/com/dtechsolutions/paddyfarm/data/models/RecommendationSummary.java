package com.dtechsolutions.paddyfarm.data.models;

public class RecommendationSummary {
    private Stage stage;
    private Recommendation recommendation;
    private int daysGone;

    public RecommendationSummary(Stage stage, Recommendation recommendation, int daysGone) {
        this.stage = stage;
        this.recommendation = recommendation;
        this.daysGone = daysGone;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public int getDaysGone() {
        return daysGone;
    }

    public void setDaysGone(int daysGone) {
        this.daysGone = daysGone;
    }
}
