package com.example.widget_management.Entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="editor")
public class Editor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "editorid")
    private Long editorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "widgetid")
    private Widget widget;
    @Column(name = "editorname", nullable = false)
    private String editorName;
    @Column(name = "content", nullable = true)
    private String content;
    @Column(name = "lastupdated")
    @UpdateTimestamp
    private Date lastUpdated;
    @Column(name = "is_tag_editor")
    private boolean boolTagEditor;
    @Column(name = "is_image_editor")
    private boolean boolImageEditor;

    public boolean isTagEditor() {
        return boolTagEditor;
    }

    public boolean isBoolImageEditor() {
        return boolImageEditor;
    }

    public void setBoolImageEditor(boolean boolImageEditor) {
        this.boolImageEditor = boolImageEditor;
    }

    public void setTagEditor(boolean tagEditor) {
        boolTagEditor = tagEditor;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
