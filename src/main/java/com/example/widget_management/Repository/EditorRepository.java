package com.example.widget_management.Repository;

import com.example.widget_management.Entity.Editor;
import com.example.widget_management.Entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {
    List<Editor> findAllByEditorId(Long editorId);
    List<Editor> findAllByWidget(Widget widget);
    @Modifying
    @Query("UPDATE Editor w SET " +
            "w.editorName = CASE WHEN :editorName IS NOT NULL THEN :editorName ELSE w.editorName END, " +
            "w.content = CASE WHEN :content IS NOT NULL THEN :content ELSE w.content END " +
            "WHERE w.editorId = :editorId")
    int updateEditorNameAndContent(Long editorId, String editorName, String content);

    @Modifying
    @Query("DELETE FROM Editor w WHERE w.editorId = :editorId")
    int deleteEditorByEditorId(Long editorId);

    @Query("SELECT e FROM Editor e WHERE e.editorId = :editorId AND e.boolTagEditor = true")
    Editor findByEditorId(Long editorId);

    @Query("SELECT e FROM Editor e WHERE e.widget.widgetId = :widgetId AND e.boolTagEditor = true")
    List<Editor> findAllEditorByWidgetId(Long widgetId);

    @Modifying
    @Query("DELETE FROM Editor e WHERE e.editorId = :editorId AND e.boolTagEditor = true")
    int deleteTagEditorByEditorId(Long editorId);

    @Query("SELECT e FROM Editor e WHERE e.editorId = :editorId AND e.boolImageEditor = true")
    Editor findImageEditorByEditorId(Long editorId);

    @Query("SELECT e FROM Editor e WHERE e.widget.widgetId = :widgetId AND e.boolImageEditor = true")
    List<Editor> findAllImageEditorByWidgetId(Long widgetId);

    @Modifying
    @Query("DELETE FROM Editor e WHERE e.editorId = :editorId AND e.boolImageEditor = true")
    int deleteImageEditorByEditorId(Long editorId);
}
