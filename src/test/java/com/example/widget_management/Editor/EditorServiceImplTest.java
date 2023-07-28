package com.example.widget_management.Editor;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.EditorUpdateDto;
import com.example.widget_management.Entity.Editor;
import com.example.widget_management.Entity.Widget;
import com.example.widget_management.Exception.ResourceNotFoundException;
import com.example.widget_management.Repository.EditorRepository;
import com.example.widget_management.Repository.WidgetRepository;
import com.example.widget_management.Service.Impl.EditorServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EditorServiceImplTest {
    @InjectMocks
    EditorServiceImpl editorService;
    @Mock
    EditorRepository editorRepository;
    @Mock
    WidgetRepository widgetRepository;
    private static final Long editorId = 2L;
    private static final Long widgetId = 1L;
    private static final String content = "testContent";
    public static final String editorName = "test Editor Name";

    @Test
    public void testCreateNewEditorImpl_Success() {
        EditorDto editorDto = new EditorDto();
        editorDto.setWidgetId(widgetId);
        editorDto.setEditorName(editorName);
        editorDto.setContent(content);

        Widget widget = new Widget();
        widget.setWidgetId(4L);

        Mockito.when(widgetRepository.findById(editorDto.getWidgetId())).thenReturn(Optional.of(widget));
        Mockito.when(editorRepository.save(Mockito.any(Editor.class))).thenReturn(new Editor());

        Boolean result = editorService.createNewEditor(editorDto);

        Assert.assertTrue(result);
        Mockito.verify(widgetRepository, Mockito.times(1)).findById(editorDto.getWidgetId());
        Mockito.verify(editorRepository, Mockito.times(1)).save(Mockito.any(Editor.class));
    }

    @Test
    public void testCreateNewEditorImpl_NullEditorDto() {
        EditorDto editorDto = null;

        Boolean result = editorService.createNewEditor(editorDto);

        Assert.assertFalse(result);
        Mockito.verify(widgetRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(editorRepository, Mockito.never()).save(Mockito.any(Editor.class));
    }

    @Test
    public void testCreateNewEditorImpl_ResourceNotFoundException() {
        EditorDto editorDto = new EditorDto();
        editorDto.setWidgetId(widgetId);
        editorDto.setEditorName(editorName);
        editorDto.setContent(content);

        Mockito.when(widgetRepository.findById(editorDto.getWidgetId())).thenReturn(Optional.empty());

        Assert.assertThrows(ResourceNotFoundException.class, () -> editorService.createNewEditor(editorDto));
    }

    @Test
    public void testGetDetailsByEditorIdImpl_Success() {
        Widget mockWidget = new Widget();
        mockWidget.setWidgetId(widgetId);

        Editor editor1 = new Editor();
        editor1.setEditorId(editorId);
        editor1.setEditorName(editorName);
        editor1.setContent(content);
        editor1.setWidget(mockWidget);
        editor1.setLastUpdated(new Date());

        Editor editor2 = new Editor();
        editor2.setEditorId(589L);
        editor2.setEditorName("Editor 2");
        editor2.setWidget(mockWidget);
        editor2.setContent("Content for Editor 2");
        editor2.setLastUpdated(new Date());

        List<Editor> mockedEditors = new ArrayList<>();
        mockedEditors.add(editor1);
        mockedEditors.add(editor2);

        Mockito.when(editorRepository.findAllByEditorId(editorId)).thenReturn(mockedEditors);

        List<EditorDto> result = editorService.getDetailsByEditorId(editorId);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals("test Editor Name", result.get(0).getEditorName());

        Mockito.verify(editorRepository, Mockito.times(1)).findAllByEditorId(editorId);
    }

    @Test
    public void testGetDetailsByWidgetIdImpl_Success() {
        Long widgetId = 1L;

        Widget widget = new Widget();
        widget.setWidgetId(widgetId);

        Editor editor1 = new Editor();
        editor1.setEditorId(1L);
        editor1.setEditorName("Editor 1");
        editor1.setContent("Content for Editor 1");
        editor1.setLastUpdated(new Date());
        editor1.setWidget(widget);

        Editor editor2 = new Editor();
        editor2.setEditorId(2L);
        editor2.setEditorName("Editor 2");
        editor2.setContent("Content for Editor 2");
        editor2.setLastUpdated(new Date());
        editor2.setWidget(widget);

        List<Editor> mockedEditors = new ArrayList<>();
        mockedEditors.add(editor1);
        mockedEditors.add(editor2);

        Mockito.when(widgetRepository.findById(widgetId)).thenReturn(Optional.of(widget));
        Mockito.when(editorRepository.findAllByWidget(widget)).thenReturn(mockedEditors);

        List<EditorDto> result = editorService.getDetailsByWidgetId(widgetId);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals("Editor 1", result.get(0).getEditorName());

        Mockito.verify(widgetRepository, Mockito.times(1)).findById(widgetId);
        Mockito.verify(editorRepository, Mockito.times(1)).findAllByWidget(widget);
    }

    @Test
    public void testDeleteEditorByEditorIdImpl_Success() {
        Mockito.when(editorRepository.deleteEditorByEditorId(editorId)).thenReturn(1);
        Boolean result = editorService.deleteEditorByEditorId(editorId);

        Assert.assertTrue(result);
        Mockito.verify(editorRepository, Mockito.times(1)).deleteEditorByEditorId(editorId);
    }

    @Test
    public void testUpdateEditorNameAndContentImpl_Success() {
        String newEditorName = "Updated Editor";
        String newContent = "Updated content";

        Mockito.when(editorRepository.updateEditorNameAndContent(editorId, newEditorName, newContent)).thenReturn(1);

        EditorUpdateDto editorUpdateDto = new EditorUpdateDto();
        editorUpdateDto.setEditorName(newEditorName);
        editorUpdateDto.setContent(newContent);

        Boolean result = editorService.updateEditorNameAndContent(editorId, editorUpdateDto);

        Assert.assertTrue(result);
        Mockito.verify(editorRepository, Mockito.times(1)).updateEditorNameAndContent(editorId, newEditorName, newContent);
    }



}
