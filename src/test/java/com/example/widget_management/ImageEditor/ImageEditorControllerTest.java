package com.example.widget_management.ImageEditor;

import com.example.widget_management.Controller.ImageEditorController;
import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.ImageEditorDto;
import com.example.widget_management.Exception.ResourceNotFoundException;
import com.example.widget_management.Service.EditorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(ImageEditorController.class)
public class ImageEditorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    EditorService editorService;
    private static final Long widgetId = 1L;
    public static final String imageUrl = "https://picsum.photos/200/300/?blur";
    private static final Long editorId = 2L;
    public static final String editorName = "test Editor Name";

    @Test
    public void testCreateImageEditor_Success() throws Exception {
        ImageEditorDto imageEditorDto = new ImageEditorDto();
        imageEditorDto.setWidgetId(widgetId);
        imageEditorDto.setImageUrl(imageUrl);
        imageEditorDto.setBoolImageEditor(true);

        Mockito.when(editorService.createNewImageEditor(Mockito.any(ImageEditorDto.class))).thenReturn(true);

        mockMvc.perform(post("/api/imageEditor/createImageEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(imageEditorDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateImageEditor_WidgetNotFound() throws Exception {
        ImageEditorDto imageEditorDto = new ImageEditorDto();
        imageEditorDto.setWidgetId(widgetId);
        imageEditorDto.setImageUrl(imageUrl);
        imageEditorDto.setBoolImageEditor(true);

        Mockito.when(editorService.createNewImageEditor(Mockito.any(ImageEditorDto.class))).thenReturn(false);

        mockMvc.perform(post("/api/imageEditor/createImageEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(imageEditorDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createWidgetTest_throwsException() throws Exception {
        ImageEditorDto imageEditorDto = new ImageEditorDto();
        imageEditorDto.setWidgetId(widgetId);
        imageEditorDto.setImageUrl(imageUrl);

        Mockito.when(editorService.createNewImageEditor(Mockito.any(ImageEditorDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/imageEditor/createImageEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(imageEditorDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetImageEditorById_Success() throws Exception {
        EditorDto mockEditorDto = new EditorDto();
        mockEditorDto.setEditorId(editorId);
        mockEditorDto.setEditorName(editorName);

        Mockito.when(editorService.getImageEditorById(editorId)).thenReturn(mockEditorDto);

        mockMvc.perform(get("/api/imageEditor/getImageEditorByEditorId")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.editorId").value(editorId))
                .andExpect(jsonPath("$.editorName").value("test Editor Name"));
    }

    @Test
    public void testGetImageEditorById_NotFound() throws Exception {
        Mockito.when(editorService.getImageEditorById(editorId)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/imageEditor/getImageEditorByEditorId")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetImageEditorById_Exception() throws Exception {
        Mockito.when(editorService.getImageEditorById(editorId)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/imageEditor/getImageEditorByEditorId")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetImageEditorByWidgetId_Success() throws Exception {
        List<EditorDto> mockEditors = new ArrayList<>();
        EditorDto editorDto1 = new EditorDto();
        editorDto1.setEditorId(editorId);
        editorDto1.setWidgetId(widgetId);
        editorDto1.setEditorName(editorName);
        mockEditors.add(editorDto1);

        EditorDto editorDto2 = new EditorDto();
        editorDto2.setEditorId(102L);
        editorDto2.setWidgetId(widgetId);
        editorDto2.setEditorName("Editor 2");
        mockEditors.add(editorDto2);

        Mockito.when(editorService.getImageEditorsByWidgetId(Mockito.anyLong())).thenReturn(mockEditors);

        mockMvc.perform(get("/api/imageEditor/getImageEditorByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(editorService, Mockito.times(1)).getImageEditorsByWidgetId(widgetId);
    }

    @Test
    public void testGetImageEditorByWidgetId_Exception() throws Exception {
        Mockito.when(editorService.getImageEditorsByWidgetId(Mockito.anyLong())).thenThrow(new RuntimeException("Error fetching tag editors"));

        mockMvc.perform(get("/api/imageEditor/getImageEditorByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testDeleteImageEditorById_Success() throws Exception {
        Mockito.when(editorService.deleteImageEditorByEditorId(editorId)).thenReturn(true);

        mockMvc.perform(delete("/api/imageEditor/deleteImageEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image Editor deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(editorService, Mockito.times(1)).deleteImageEditorByEditorId(editorId);
    }

    @Test
    public void testDeleteImageEditorById_EditorNotFound() throws Exception {
        Mockito.when(editorService.deleteImageEditorByEditorId(editorId)).thenReturn(false);

        mockMvc.perform(delete("/api/imageEditor/deleteImageEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Image Editor not found"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).deleteImageEditorByEditorId(editorId);
    }

    @Test
    public void testDeleteImageEditorById_Exception() throws Exception {
        Mockito.when(editorService.deleteImageEditorByEditorId(editorId)).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(delete("/api/imageEditor/deleteImageEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error deleting image editor: Test Exception"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).deleteImageEditorByEditorId(editorId);
    }

    @Test
    public void testUpdateImageUrl_Success() throws Exception {
        String newImageUrl = "https://example.com/new_image_url";

        Mockito.when(editorService.editImageUrl(editorId, newImageUrl)).thenReturn(true);

        mockMvc.perform(put("/api/imageEditor/updateImageUrl")
                        .param("editorId", String.valueOf(editorId))
                        .param("newImageUrl", newImageUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Image Url updated successfully"))
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(editorService, Mockito.times(1)).editImageUrl(editorId, newImageUrl);
    }

    @Test
    public void testUpdateImageUrl_EditorNotFound() throws Exception {
        String newImageUrl = "https://example.com/new_image_url";

        Mockito.when(editorService.editImageUrl(editorId, newImageUrl)).thenReturn(false);

        mockMvc.perform(put("/api/imageEditor/updateImageUrl")
                        .param("editorId", String.valueOf(editorId))
                        .param("newImageUrl", newImageUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Image editor not found with ID: " + editorId))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).editImageUrl(editorId, newImageUrl);
    }

    @Test
    public void testUpdateImageUrl_Exception() throws Exception {
        String newImageUrl = "https://example.com/new_image_url";

        Mockito.when(editorService.editImageUrl(editorId, newImageUrl)).thenThrow(new RuntimeException("Some error occurred"));

        mockMvc.perform(put("/api/imageEditor/updateImageUrl")
                        .param("editorId", String.valueOf(editorId))
                        .param("newImageUrl", newImageUrl))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Error updating image url: Some error occurred"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).editImageUrl(editorId, newImageUrl);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
