package com.dtechsolutions.paddyfarm.data.models;

import com.dtechsolutions.paddyfarm.data.api.ApiClient;

import java.util.Date;

public class DiseaseDetectionResult {
    private String id;
    private String imagePath;
    private String disease;
    private String diseaseScientificName;
    private DiseaseRiskLevel riskLevel;
    private Date createdAt;

    public DiseaseDetectionResult(String id, String imagePath, String disease, String diseaseScientificName, DiseaseRiskLevel riskLevel, Date createdAt) {
        this.id = id;
        this.imagePath = imagePath;
        this.disease = disease;
        this.diseaseScientificName = diseaseScientificName;
        this.riskLevel = riskLevel;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDiseaseScientificName() {
        return diseaseScientificName;
    }

    public void setDiseaseScientificName(String diseaseScientificName) {
        this.diseaseScientificName = diseaseScientificName;
    }

    public DiseaseRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(DiseaseRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return ApiClient.baseUrl + imagePath;
    }
}
