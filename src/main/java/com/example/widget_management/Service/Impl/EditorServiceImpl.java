package com.example.widget_management.Service.Impl;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.EditorUpdateDto;
import com.example.widget_management.Dto.ImageEditorDto;
import com.example.widget_management.Dto.TagEditorDto;
import com.example.widget_management.Entity.Editor;
import com.example.widget_management.Entity.Widget;
import com.example.widget_management.Exception.ResourceNotFoundException;
import com.example.widget_management.Repository.EditorRepository;
import com.example.widget_management.Repository.WidgetRepository;
import com.example.widget_management.Service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EditorServiceImpl implements EditorService {
    @Autowired
    EditorRepository editorRepository;
    @Autowired
    WidgetRepository widgetRepository;

    @Override
    public Boolean createNewEditor(EditorDto editorDto) {
        if (Objects.nonNull(editorDto)) {
            Optional<Widget> widget1 = widgetRepository.findById(editorDto.getWidgetId());
            if (widget1.isPresent()) {
                Widget widget = widget1.get();

                Editor editor = new Editor();
                editor.setEditorName(editorDto.getEditorName());
                editor.setContent(editorDto.getContent());
                editor.setLastUpdated(new Date());
                editor.setWidget(widget);
                editorRepository.save(editor);
                return true;
            } else {
                throw new ResourceNotFoundException("Widget not found with ID: " + editorDto.getWidgetId());
            }
        } else {
            return false;
        }
    }

    @Override
    public List<EditorDto> getDetailsByEditorId(Long editorId) {
        List<Editor> editors = editorRepository.findAllByEditorId(editorId);
        return editors.stream()
                .map(this::convertToEditorDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EditorDto> getDetailsByWidgetId(Long widgetId) {
        Widget widget = widgetRepository.findById(widgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Widget not found with ID: " + widgetId));

        List<Editor> editorList = editorRepository.findAllByWidget(widget);
        return editorList.stream()
                .map(this::convertToEditorDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean updateEditorNameAndContent(Long editorId, EditorUpdateDto editorUpdateDto) {
        int updatedRows = editorRepository.updateEditorNameAndContent(editorId, editorUpdateDto.getEditorName(),
                editorUpdateDto.getContent());
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public Boolean deleteEditorByEditorId(Long editorId) {
        int deletedRows = editorRepository.deleteEditorByEditorId(editorId);
        return deletedRows > 0;
    }

    @Override
    public Boolean createNewTagEditor(TagEditorDto tagEditorDto) {
        Widget widget = widgetRepository.findById(tagEditorDto.getWidgetId())
                .orElse(null);

        if (widget != null) {
            Editor tagEditor = new Editor();
            tagEditor.setEditorName(tagEditorDto.getTagName());
            tagEditor.setWidget(widget);
            tagEditor.setTagEditor(tagEditorDto.getTagEditor());
            tagEditor.setLastUpdated(new Date());

            editorRepository.save(tagEditor);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public EditorDto getTagEditorById(Long editorId) {
        Editor editor = editorRepository.findByEditorId(editorId);
        if (editor != null) {
            return convertToEditorDto(editor);
        } else {
            throw new ResourceNotFoundException("Tag Editor not found with ID: " + editorId);
        }
    }

    @Override
    public List<EditorDto> getTagEditorsByWidgetId(Long widgetId) {
        List<Editor> tagEditors = editorRepository.findAllEditorByWidgetId(widgetId);

        return tagEditors.stream()
                .map(this::convertToEditorDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean editTagName(Long editorId, String newTagName) {
        Editor editor = editorRepository.findByEditorId(editorId);
        if (editor != null && editor.isTagEditor()) {
            editor.setEditorName(newTagName);
            editorRepository.save(editor);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean deleteTagEditorByEditorId(Long editorId) {
        int deletedRows = editorRepository.deleteTagEditorByEditorId(editorId);
        return deletedRows > 0;
    }

    @Override
    public Boolean createNewImageEditor(ImageEditorDto imageEditorDto) {
        Widget widget = widgetRepository.findById(imageEditorDto.getWidgetId())
                .orElse(null);

        if (widget != null) {
            Editor imageEditor = new Editor();
            imageEditor.setEditorName(imageEditorDto.getImageUrl());
            imageEditor.setWidget(widget);
            imageEditor.setBoolImageEditor(imageEditorDto.getBoolImageEditor());
            imageEditor.setLastUpdated(new Date());

            editorRepository.save(imageEditor);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public EditorDto getImageEditorById(Long editorId) {
        Editor editor = editorRepository.findImageEditorByEditorId(editorId);
        if (editor != null) {
            return convertToEditorDto(editor);
        } else {
            throw new ResourceNotFoundException("Image Editor not found with ID: " + editorId);
        }
    }

    @Override
    public List<EditorDto> getImageEditorsByWidgetId(Long widgetId) {
        List<Editor> tagEditors = editorRepository.findAllImageEditorByWidgetId(widgetId);

        return tagEditors.stream()
                .map(this::convertToEditorDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean editImageUrl(Long editorId, String newImageUrl) {
        Editor editor = editorRepository.findImageEditorByEditorId(editorId);
        if (editor != null && editor.isBoolImageEditor()) {
            editor.setEditorName(newImageUrl);
            editorRepository.save(editor);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean deleteImageEditorByEditorId(Long editorId) {
        int deletedRows = editorRepository.deleteImageEditorByEditorId(editorId);
        return deletedRows > 0;
    }

    private EditorDto convertToEditorDto(Editor editor) {
        EditorDto editorDto = new EditorDto();
        editorDto.setEditorId(editor.getEditorId());
        editorDto.setWidgetId(editor.getWidget().getWidgetId());
        editorDto.setEditorName(editor.getEditorName());
        editorDto.setContent(editor.getContent());
        editorDto.setLastUpdated(editor.getLastUpdated());
        return editorDto;
    }
}
