package com.example.widget_management.Entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="widget")
public class Widget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "widgetid")
    private Long widgetId;
    @Column(name = "widgetname", nullable = false)
    private String widgetName;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "creationdate")
    @CreationTimestamp
    private Date creationDate;

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
