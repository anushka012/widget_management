package com.example.widget_management.Dto;

public class TagEditorDto {
    private Long widgetId;
    private String tagName;
    private Boolean boolTagEditor = true;

    public Boolean getTagEditor() {
        return boolTagEditor;
    }

    public void setTagEditor(Boolean tagEditor) {
        boolTagEditor = tagEditor;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
