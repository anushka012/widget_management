package com.example.widget_management.Editor;

import com.example.widget_management.Controller.EditorController;
import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.EditorUpdateDto;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(EditorController.class)
public class EditorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    EditorService editorService;
    private static final Long editorId = 2L;
    private static final Long widgetId = 1L;
    private static final String content = "testContent";
    public static final String editorName = "test Editor Name";

    @Test
    public void testCreateEditor_Success() throws Exception {
        EditorDto editorDto = new EditorDto();
        editorDto.setWidgetId(widgetId);
        editorDto.setEditorName(editorName);
        editorDto.setContent(content);

        Mockito.when(editorService.createNewEditor(Mockito.any(EditorDto.class))).thenReturn(true);

        mockMvc.perform(post("/api/editor/createEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(editorDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateEditor_throwsException() throws Exception {
        EditorDto editorDto = new EditorDto();
        editorDto.setWidgetId(widgetId);
        editorDto.setEditorName(editorName);
        editorDto.setContent(content);

        Mockito.when(editorService.createNewEditor(Mockito.any(EditorDto.class))).thenThrow(new RuntimeException("Some error occurred."));

        mockMvc.perform(post("/api/editor/createEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(editorDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteEditor_Success() throws Exception {
        Mockito.when(editorService.deleteEditorByEditorId(editorId)).thenReturn(true);

        mockMvc.perform(delete("/api/editor/deleteEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Editor deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDeleteEditor_NotFound() throws Exception {
        Mockito.when(editorService.deleteEditorByEditorId(editorId)).thenReturn(false);

        mockMvc.perform(delete("/api/editor/deleteEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Editor not found"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDeleteEditor_Error() throws Exception {
        Mockito.when(editorService.deleteEditorByEditorId(editorId)).thenThrow(new RuntimeException("Some error occurred."));

        mockMvc.perform(delete("/api/editor/deleteEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error deleting editor: Some error occurred."))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateEditorDetails_Success() throws Exception {
        EditorUpdateDto editorUpdateDto = new EditorUpdateDto();
        editorUpdateDto.setEditorName("Updated Editor Name");
        editorUpdateDto.setContent("Updated Editor Content");

        Mockito.when(editorService.updateEditorNameAndContent(editorId, editorUpdateDto)).thenReturn(true);

        mockMvc.perform(put("/api/editor/updateEditorDetails")
                        .param("editorId", String.valueOf(editorId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(editorUpdateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateEditorDetails_throwsException() throws Exception {
        EditorUpdateDto editorUpdateDto = new EditorUpdateDto();
        editorUpdateDto.setEditorName("Updated Editor Name");
        editorUpdateDto.setContent("Updated Editor Content");

        Mockito.when(editorService.updateEditorNameAndContent(editorId, editorUpdateDto)).thenThrow(new RuntimeException("Some error occurred."));

        mockMvc.perform(put("/api/editor/updateEditorDetails")
                        .param("editorId", String.valueOf(editorId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(editorUpdateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Editor not found"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testGetEditorByWidgetId_Success() throws Exception {
        List<EditorDto> editorList = new ArrayList<>();
        EditorDto editorDto1 = new EditorDto();
        editorDto1.setEditorId(1L);
        editorDto1.setEditorName("Editor 1");
        editorDto1.setContent("Content 1");
        editorList.add(editorDto1);

        EditorDto editorDto2 = new EditorDto();
        editorDto2.setEditorId(2L);
        editorDto2.setEditorName("Editor 2");
        editorDto2.setContent("Content 2");
        editorList.add(editorDto2);

        Mockito.when(editorService.getDetailsByWidgetId(widgetId)).thenReturn(editorList);

        mockMvc.perform(get("/api/editor/getEditorDetailsByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].editorId").value(1))
                .andExpect(jsonPath("$[1].content").value("Content 2"));
    }

    @Test
    public void testGetEditorByWidgetId_EmptyList() throws Exception {
        Mockito.when(editorService.getDetailsByWidgetId(widgetId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/editor/getEditorDetailsByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetEditorByWidgetId_Exception() throws Exception {
        Mockito.when(editorService.getDetailsByWidgetId(widgetId)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/editor/getEditorDetailsByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetEditorByEditorId_Success() throws Exception {
        EditorDto editorDto = new EditorDto();
        editorDto.setEditorId(editorId);
        editorDto.setWidgetId(widgetId);
        editorDto.setContent(content);
        editorDto.setEditorName(editorName);

        List<EditorDto> editorDtoList = Collections.singletonList(editorDto);

        Mockito.when(editorService.getDetailsByEditorId(editorId)).thenReturn(editorDtoList);

        mockMvc.perform(get("/api/editor/getEditorDetailsByEditorId")
                        .param("editorId", String.valueOf(editorId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].editorId").value(editorId))
                .andExpect(jsonPath("$[0].editorName").value(editorName));

        Mockito.verify(editorService, Mockito.times(1)).getDetailsByEditorId(editorId);
    }

    @Test
    public void testGetEditorByEditorId_Exception() throws Exception {
        Mockito.when(editorService.getDetailsByEditorId(editorId)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/editor/getEditorDetailsByEditorId")
                        .param("editorId", String.valueOf(editorId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
