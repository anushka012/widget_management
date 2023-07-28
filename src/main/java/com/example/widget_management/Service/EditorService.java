package com.example.widget_management.Service;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.EditorUpdateDto;
import com.example.widget_management.Dto.ImageEditorDto;
import com.example.widget_management.Dto.TagEditorDto;

import java.util.List;

public interface EditorService {
    public Boolean createNewEditor(EditorDto editorDto);
    public List<EditorDto> getDetailsByEditorId(Long editorId);
    public List<EditorDto> getDetailsByWidgetId(Long widgetId);
    public Boolean updateEditorNameAndContent(Long editorId, EditorUpdateDto editorUpdateDto);
    public Boolean deleteEditorByEditorId(Long editorId);
    public Boolean createNewTagEditor(TagEditorDto tagEditorDto);
    public EditorDto getTagEditorById(Long editorId);
    public List<EditorDto> getTagEditorsByWidgetId(Long widgetId);
    public Boolean editTagName(Long editorId, String newTagName);
    public Boolean deleteTagEditorByEditorId(Long editorId);
    public Boolean createNewImageEditor(ImageEditorDto imageEditorDto);
    public EditorDto getImageEditorById(Long editorId);
    public List<EditorDto> getImageEditorsByWidgetId(Long widgetId);
    public Boolean editImageUrl(Long editorId, String newImageUrl);
    public Boolean deleteImageEditorByEditorId(Long editorId);
}
