package com.example.widget_management.ImageEditor;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.ImageEditorDto;
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
public class ImageEditorServiceImplTest {
    @InjectMocks
    EditorServiceImpl editorService;
    @Mock
    EditorRepository editorRepository;
    @Mock
    WidgetRepository widgetRepository;
    private static final Long widgetId = 1L;
    public static final String imageUrl = "https://picsum.photos/200/300/?blur";
    private static final Long editorId = 2L;
    public static final String editorName = "test Editor Name";

    @Test
    public void testCreateNewImageEditorImpl_Success() {
        ImageEditorDto imageEditorDto = new ImageEditorDto();
        imageEditorDto.setWidgetId(widgetId);
        imageEditorDto.setImageUrl(imageUrl);
        imageEditorDto.setBoolImageEditor(true);

        Widget widget = new Widget();
        widget.setWidgetId(widgetId);

        Mockito.when(widgetRepository.findById(widgetId)).thenReturn(Optional.of(widget));
        Mockito.when(editorRepository.save(Mockito.any(Editor.class))).thenReturn(new Editor());

        Boolean result = editorService.createNewImageEditor(imageEditorDto);

        Assert.assertTrue(result);
        Mockito.verify(widgetRepository, Mockito.times(1)).findById(widgetId);
        Mockito.verify(editorRepository, Mockito.times(1)).save(Mockito.any(Editor.class));
    }

    @Test
    public void testCreateNewImageEditorImpl_WidgetNotFound() {
        ImageEditorDto imageEditorDto = new ImageEditorDto();
        imageEditorDto.setWidgetId(widgetId);
        imageEditorDto.setImageUrl(imageUrl);
        imageEditorDto.setBoolImageEditor(true);

        Mockito.when(widgetRepository.findById(widgetId)).thenReturn(Optional.empty());
        Boolean result = editorService.createNewImageEditor(imageEditorDto);

        Assert.assertFalse(result);
        Mockito.verify(widgetRepository, Mockito.times(1)).findById(widgetId);
        Mockito.verify(editorRepository, Mockito.never()).save(Mockito.any(Editor.class));
    }

    @Test
    public void testGetImageEditorById_Success() {
        Widget mockWidget = new Widget();
        mockWidget.setWidgetId(widgetId);

        Editor mockEditor = new Editor();
        mockEditor.setEditorId(editorId);
        mockEditor.setEditorName(editorName);
        mockEditor.setWidget(mockWidget);

        Mockito.when(editorRepository.findImageEditorByEditorId(editorId)).thenReturn(mockEditor);

        EditorDto result = editorService.getImageEditorById(editorId);

        Assert.assertNotNull(result);
        Assert.assertEquals(editorId, result.getEditorId());
        Assert.assertEquals(editorName, result.getEditorName());

        Mockito.verify(editorRepository, Mockito.times(1)).findImageEditorByEditorId(editorId);
    }

    @Test
    public void testGetImageEditorById_EditorNotFound() {
        Mockito.when(editorRepository.findImageEditorByEditorId(editorId)).thenReturn(null);

        Assert.assertThrows(ResourceNotFoundException.class, () -> {
            editorService.getImageEditorById(editorId);
        });
        Mockito.verify(editorRepository, Mockito.times(1)).findImageEditorByEditorId(editorId);
    }

    @Test
    public void testGetImageEditorsByWidgetIdImpl_Success() {
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

        Mockito.when(editorRepository.findAllImageEditorByWidgetId(widgetId)).thenReturn(mockEditors);

        List<EditorDto> result = editorService.getImageEditorsByWidgetId(widgetId);

        EditorDto editorDto1 = result.get(0);
        Assert.assertEquals(mockEditor1.getEditorId(), editorDto1.getEditorId());
        Assert.assertEquals(widgetId, editorDto1.getWidgetId());
        Mockito.verify(editorRepository, Mockito.times(1)).findAllImageEditorByWidgetId(widgetId);
    }

    @Test
    public void testDeleteImageEditorByEditorIdImpl_Success() {
        Mockito.when(editorRepository.deleteImageEditorByEditorId(editorId)).thenReturn(1);
        Boolean result = editorService.deleteImageEditorByEditorId(editorId);

        Assert.assertTrue(result);
        Mockito.verify(editorRepository, Mockito.times(1)).deleteImageEditorByEditorId(editorId);
    }

    @Test
    public void testEditImageUrl_Success() {
        Editor mockEditor = new Editor();
        mockEditor.setEditorId(editorId);
        mockEditor.setEditorName(editorName);
        mockEditor.setBoolImageEditor(true);

        String newImageUrl = "https://example.com/new_image_url";

        Mockito.when(editorRepository.findImageEditorByEditorId(editorId)).thenReturn(mockEditor);

        Boolean result = editorService.editImageUrl(editorId, newImageUrl);

        Assert.assertTrue(result);
        Assert.assertEquals(newImageUrl, mockEditor.getEditorName());

        Mockito.verify(editorRepository, Mockito.times(1)).findImageEditorByEditorId(editorId);
        Mockito.verify(editorRepository, Mockito.times(1)).save(mockEditor);
    }

    @Test
    public void testEditImageUrl_EditorNotFound() {
        String newImageUrl = "https://example.com/new_image_url";

        Mockito.when(editorRepository.findImageEditorByEditorId(editorId)).thenReturn(null);

        Boolean result = editorService.editImageUrl(editorId, newImageUrl);

        Assert.assertFalse(result);

        Mockito.verify(editorRepository, Mockito.times(1)).findImageEditorByEditorId(editorId);
        Mockito.verify(editorRepository, Mockito.never()).save(Mockito.any(Editor.class));
    }

}
