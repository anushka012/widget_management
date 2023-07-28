package com.example.widget_management.TagEditor;

import com.example.widget_management.Controller.TagEditorController;
import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.TagEditorDto;
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
@WebMvcTest(TagEditorController.class)
public class TagEditorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    EditorService editorService;
    public static final Long widgetId = 1L;
    public static final Long editorId = 2L;
    public static final String tagName = "testTag";
    public static final String editorName = "test Editor Name";

    @Test
    public void testCreateTagEditor_Success() throws Exception {
        TagEditorDto tagEditorDto = new TagEditorDto();
        tagEditorDto.setWidgetId(widgetId);
        tagEditorDto.setTagName(tagName);

        Mockito.when(editorService.createNewTagEditor(Mockito.any(TagEditorDto.class))).thenReturn(true);

        mockMvc.perform(post("/api/tagEditor/createTagEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tagEditorDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateTagEditor_WidgetNotFound() throws Exception {
        TagEditorDto tagEditorDto = new TagEditorDto();
        tagEditorDto.setWidgetId(widgetId);
        tagEditorDto.setTagName(tagName);

        Mockito.when(editorService.createNewTagEditor(Mockito.any(TagEditorDto.class))).thenReturn(false);

        mockMvc.perform(post("/api/tagEditor/createTagEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tagEditorDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Widget not found with ID: " + tagEditorDto.getWidgetId()))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void createWidgetTest_throwsException() throws Exception {
        TagEditorDto tagEditorDto = new TagEditorDto();
        tagEditorDto.setWidgetId(widgetId);
        tagEditorDto.setTagName(tagName);

        Mockito.when(editorService.createNewTagEditor(Mockito.any(TagEditorDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/TagEditor/createTagEditor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tagEditorDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTagEditorById_Success() throws Exception {
        EditorDto mockEditorDto = new EditorDto();
        mockEditorDto.setEditorId(editorId);
        mockEditorDto.setEditorName(editorName);

        Mockito.when(editorService.getTagEditorById(editorId)).thenReturn(mockEditorDto);

        mockMvc.perform(get("/api/tagEditor/getTagEditorByEditorId")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.editorId").value(editorId))
                .andExpect(jsonPath("$.editorName").value("test Editor Name"));
    }

    @Test
    public void testGetTagEditorById_NotFound() throws Exception {
        Mockito.when(editorService.getTagEditorById(editorId)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/tagEditor/getTagEditorByEditorId")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTagEditorById_Exception() throws Exception {
        Mockito.when(editorService.getTagEditorById(editorId)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/tagEditor/getTagEditorByEditorId")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTagEditorByWidgetId_Success() throws Exception {
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

        Mockito.when(editorService.getTagEditorsByWidgetId(Mockito.anyLong())).thenReturn(mockEditors);

        mockMvc.perform(get("/api/tagEditor/getTagEditorByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(editorService, Mockito.times(1)).getTagEditorsByWidgetId(widgetId);
    }

    @Test
    public void testGetTagEditorByWidgetId_Exception() throws Exception {
        Mockito.when(editorService.getTagEditorsByWidgetId(Mockito.anyLong())).thenThrow(new RuntimeException("Error fetching tag editors"));

        mockMvc.perform(get("/api/tagEditor/getTagEditorByWidgetId")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testDeleteTagEditorById_Success() throws Exception {
        Mockito.when(editorService.deleteTagEditorByEditorId(editorId)).thenReturn(true);

        mockMvc.perform(delete("/api/tagEditor/deleteTagEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tag Editor deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(editorService, Mockito.times(1)).deleteTagEditorByEditorId(editorId);
    }

    @Test
    public void testDeleteTagEditorById_EditorNotFound() throws Exception {
        Mockito.when(editorService.deleteTagEditorByEditorId(editorId)).thenReturn(false);

        mockMvc.perform(delete("/api/tagEditor/deleteTagEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tag Editor not found"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).deleteTagEditorByEditorId(editorId);
    }

    @Test
    public void testDeleteTagEditorById_Exception() throws Exception {
        Mockito.when(editorService.deleteTagEditorByEditorId(editorId)).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(delete("/api/tagEditor/deleteTagEditor")
                        .param("editorId", String.valueOf(editorId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error deleting tag editor: Test Exception"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).deleteTagEditorByEditorId(editorId);
    }

    @Test
    public void testUpdateTagName_Success() throws Exception {
        String newTagName = "New Tag Name";

        Mockito.when(editorService.editTagName(editorId, newTagName)).thenReturn(true);

        mockMvc.perform(put("/api/tagEditor/updateTagName")
                        .param("editorId", String.valueOf(editorId))
                        .param("newTagName", newTagName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tag name updated successfully"))
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(editorService, Mockito.times(1)).editTagName(editorId, newTagName);
    }

    @Test
    public void testUpdateTagName_EditorNotFound() throws Exception {
        String newTagName = "New Tag Name";

        Mockito.when(editorService.editTagName(editorId, newTagName)).thenReturn(false);

        mockMvc.perform(put("/api/tagEditor/updateTagName")
                        .param("editorId", String.valueOf(editorId))
                        .param("newTagName", newTagName))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tag editor not found with ID: " + editorId))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).editTagName(editorId, newTagName);
    }

    @Test
    public void testUpdateTagName_Exception() throws Exception {
        String newTagName = "New Tag Name";

        Mockito.when(editorService.editTagName(editorId, newTagName)).thenThrow(new RuntimeException("Some error occurred"));

        mockMvc.perform(put("/api/tagEditor/updateTagName")
                        .param("editorId", String.valueOf(editorId))
                        .param("newTagName", newTagName))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Error updating tag name: Some error occurred"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(editorService, Mockito.times(1)).editTagName(editorId, newTagName);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
