package com.example.widget_management.Dto;

public class ImageEditorDto {
    private Long widgetId;
    private String imageUrl;
    private Boolean boolImageEditor = true;

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getBoolImageEditor() {
        return boolImageEditor;
    }

    public void setBoolImageEditor(Boolean boolImageEditor) {
        this.boolImageEditor = boolImageEditor;
    }
}
