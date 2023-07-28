package com.example.widget_management.Repository;

import com.example.widget_management.Entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
    List<Widget> findAllByWidgetId(Long widgetId);
    @Modifying
    @Query("UPDATE Widget w SET " +
            "w.widgetName = CASE WHEN :widgetName IS NOT NULL THEN :widgetName ELSE w.widgetName END, " +
            "w.description = CASE WHEN :description IS NOT NULL THEN :description ELSE w.description END " +
            "WHERE w.widgetId = :widgetId")
    int updateWidgetNameAndDescription(Long widgetId, String widgetName, String description);

    @Modifying
    @Query("DELETE FROM Widget w WHERE w.widgetId = :widgetId")
    int deleteWidgetByWidgetId(Long widgetId);
}
