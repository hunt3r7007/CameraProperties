package com.example.cameraproperties;

public class CameraProperties {
    private String propertyName;
    private String propertyValue;

    public CameraProperties(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}
