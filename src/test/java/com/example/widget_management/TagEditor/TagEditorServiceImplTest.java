package com.example.widget_management.TagEditor;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.TagEditorDto;
import com.example.widget_management.Entity.Editor;
import com.example.widget_management.Entity.Widget;
import com.example.widget_management.Exception.ResourceNotFoundException;
import com.example.widget_management.Repository.EditorRepository;
import com.example.widget_management.Repository.WidgetRepository;
import com.example.widget_management.Service.Impl.EditorServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TagEditorServiceImplTest {
    @InjectMocks
    EditorServiceImpl editorService;
    @Mock
    EditorRepository editorRepository;
    @Mock
    WidgetRepository widgetRepository;
    private static final Long widgetId = 1L;
    public static final String tagName = "testTag";
    private static final Long editorId = 2L;
    public static final String editorName = "test Editor Name";

    @Test
    public void testCreateNewTagEditorImpl_Success() {
        TagEditorDto tagEditorDto = new TagEditorDto();
        tagEditorDto.setWidgetId(widgetId);
        tagEditorDto.setTagName(tagName);
        tagEditorDto.setTagEditor(true);

        Widget widget = new Widget();
        widget.setWidgetId(widgetId);

        Mockito.when(widgetRepository.findById(widgetId)).thenReturn(Optional.of(widget));
        Mockito.when(editorRepository.save(Mockito.any(Editor.class))).thenReturn(new Editor());

        Boolean result = editorService.createNewTagEditor(tagEditorDto);

        Assert.assertTrue(result);
        Mockito.verify(widgetRepository, Mockito.times(1)).findById(widgetId);
        Mockito.verify(editorRepository, Mockito.times(1)).save(Mockito.any(Editor.class));
    }

    @Test
    public void testCreateNewTagEditorImpl_WidgetNotFound() {
        TagEditorDto tagEditorDto = new TagEditorDto();
        tagEditorDto.setWidgetId(widgetId);
        tagEditorDto.setTagName(tagName);
        tagEditorDto.setTagEditor(true);

        Mockito.when(widgetRepository.findById(widgetId)).thenReturn(Optional.empty());

        Boolean result = editorService.createNewTagEditor(tagEditorDto);

        Assert.assertFalse(result);
        Mockito.verify(widgetRepository, Mockito.times(1)).findById(widgetId);
        Mockito.verify(editorRepository, Mockito.never()).save(Mockito.any(Editor.class));
    }

    @Test
    public void testGetTagEditorByIdImpl_Success() {
        Widget mockWidget = new Widget();
        mockWidget.setWidgetId(widgetId);

        Editor mockEditor = new Editor();
        mockEditor.setEditorId(editorId);
        mockEditor.setEditorName(editorName);
        mockEditor.setWidget(mockWidget);

        Mockito.when(editorRepository.findByEditorId(editorId)).thenReturn(mockEditor);

        EditorDto result = editorService.getTagEditorById(editorId);

        Assert.assertNotNull(result);
        Assert.assertEquals(editorId, result.getEditorId());
        Assert.assertEquals(editorName, result.getEditorName());
        Assert.assertEquals(widgetId, result.getWidgetId());

        Mockito.verify(editorRepository, Mockito.times(1)).findByEditorId(editorId);
    }

    @Test
    public void testGetTagEditorByIdImpl_ElseCase() {
        Mockito.when(editorRepository.findByEditorId(editorId)).thenReturn(null);

        Assert.assertThrows(ResourceNotFoundException.class, () -> {
            editorService.getTagEditorById(editorId);
        });
        Mockito.verify(editorRepository, Mockito.times(1)).findByEditorId(editorId);
    }

    @Test
    public void testGetTagEditorsByWidgetIdImpl_Success() {
        Widget mockWidget = new Widget();
        mockWidget.setWidgetId(widgetId);

        Editor mockEditor1 = new Editor();
        mockEditor1.setEditorId(editorId);
        mockEditor1.setEditorName(editorName);
        mockEditor1.setWidget(mockWidget);

        Editor mockEditor2 = new Editor();
        mockEditor2.setEditorId(54L);
        mockEditor2.setEditorName("New name");
        mockEditor2.setWidget(mockWidget);

        List<Editor> mockEditors = Arrays.asList(mockEditor1, mockEditor2);

        Mockito.when(editorRepository.findAllEditorByWidgetId(widgetId)).thenReturn(mockEditors);

        List<EditorDto> result = editorService.getTagEditorsByWidgetId(widgetId);

        EditorDto editorDto1 = result.get(0);
        Assert.assertEquals(mockEditor1.getEditorId(), editorDto1.getEditorId());
        Assert.assertEquals(widgetId, editorDto1.getWidgetId());
        Mockito.verify(editorRepository, Mockito.times(1)).findAllEditorByWidgetId(widgetId);
    }

    @Test
    public void testDeleteTagEditorByEditorIdImpl_Success() {
        Mockito.when(editorRepository.deleteTagEditorByEditorId(editorId)).thenReturn(1);
        Boolean result = editorService.deleteTagEditorByEditorId(editorId);

        Assert.assertTrue(result);
        Mockito.verify(editorRepository, Mockito.times(1)).deleteTagEditorByEditorId(editorId);
    }

    @Test
    public void testEditTagNameImpl_Success() {
        String newTagName = "New Tag Name";

        Editor mockEditor = new Editor();
        mockEditor.setEditorId(editorId);
        mockEditor.setEditorName(editorName);
        mockEditor.setTagEditor(true);

        Mockito.when(editorRepository.findByEditorId(editorId)).thenReturn(mockEditor);
        Mockito.when(editorRepository.save(Mockito.any(Editor.class))).thenReturn(mockEditor);

        Boolean result = editorService.editTagName(editorId, newTagName);

        Assert.assertTrue(result);
        Assert.assertEquals(newTagName, mockEditor.getEditorName());
        Mockito.verify(editorRepository, Mockito.times(1)).findByEditorId(editorId);
        Mockito.verify(editorRepository, Mockito.times(1)).save(mockEditor);
    }

    @Test
    public void testEditTagNameImpl_EditorNotFound() {
        String newTagName = "New Tag Name";

        Mockito.when(editorRepository.findByEditorId(editorId)).thenReturn(null);

        Boolean result = editorService.editTagName(editorId, newTagName);

        Assert.assertFalse(result);
        Mockito.verify(editorRepository, Mockito.times(1)).findByEditorId(editorId);
        Mockito.verify(editorRepository, Mockito.never()).save(Mockito.any(Editor.class));
    }

}
