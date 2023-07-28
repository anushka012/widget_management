package com.example.widget_management.Widget;

import com.example.widget_management.Controller.WidgetController;
import com.example.widget_management.Dto.WidgetDto;
import com.example.widget_management.Dto.WidgetUpdateDto;
import com.example.widget_management.Service.WidgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(WidgetController.class)
public class WidgetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WidgetService widgetService;

    @Test
    public void createWidgetTest_thenSuccess200() throws Exception {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setWidgetName("Test Widget");
        widgetDto.setWidgetId(1234L);
        widgetDto.setCreationDate(new Date());
        widgetDto.setDescription("This is a test widget");

        Mockito.when(widgetService.createNewWidget(Mockito.any(WidgetDto.class))).thenReturn(true);

        mockMvc.perform(post("/api/widget/createWidget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(widgetDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void createWidgetTest_throwsException() throws Exception {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setWidgetName("Test Widget");
        widgetDto.setWidgetId(1234L);
        widgetDto.setCreationDate(new Date());
        widgetDto.setDescription("This is a test widget");

        Mockito.when(widgetService.createNewWidget(Mockito.any(WidgetDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/widget/createWidget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(widgetDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllWidgetDetails() throws Exception {
        List<WidgetDto> mockWidgets = new ArrayList<>();
        WidgetDto widgetDto1 = new WidgetDto();
        widgetDto1.setWidgetId(1L);
        widgetDto1.setWidgetName("Widget 1");
        widgetDto1.setDescription("Description for Widget 1");
        widgetDto1.setCreationDate(new Date());
        mockWidgets.add(widgetDto1);

        WidgetDto widgetDto2 = new WidgetDto();
        widgetDto2.setWidgetId(2L);
        widgetDto2.setWidgetName("Widget 2");
        widgetDto2.setDescription("Description for Widget 2");
        widgetDto2.setCreationDate(new Date());
        mockWidgets.add(widgetDto2);

        Mockito.when(widgetService.getAllWidgets()).thenReturn(mockWidgets);

        mockMvc.perform(get("/api/widget/getAllWidgets"))
                .andExpect(status().isOk()) // Assert the response status is 200 (OK)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))); // Assert the response contains 2 widgets

        Mockito.verify(widgetService).getAllWidgets();
    }

    @Test
    public void testGetAllWidgetDetails_Exception() throws Exception {
        Mockito.when(widgetService.getAllWidgets()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/widget/getAllWidgets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetWidgetDetailsById_thenSuccess200() throws Exception {
        List<WidgetDto> mockedWidgets = new ArrayList<>();
        WidgetDto widgetDto1 = new WidgetDto();
        widgetDto1.setWidgetId(1L);
        widgetDto1.setWidgetName("Widget 1");
        widgetDto1.setDescription("Description for Widget 1");
        widgetDto1.setCreationDate(new Date());
        mockedWidgets.add(widgetDto1);

        WidgetDto widgetDto2 = new WidgetDto();
        widgetDto2.setWidgetId(2L);
        widgetDto2.setWidgetName("Widget 2");
        widgetDto2.setDescription("Description for Widget 2");
        widgetDto2.setCreationDate(new Date());
        mockedWidgets.add(widgetDto2);

        Mockito.when(widgetService.getWidgetById(Mockito.anyLong())).thenReturn(mockedWidgets);

        mockMvc.perform(get("/api/widget/getWidgetById")
                        .param("widgetId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // Assert the response contains 2 widgets
                .andExpect(jsonPath("$[0].widgetId").value(1L))
                .andExpect(jsonPath("$[1].widgetName").value("Widget 2"));

        Mockito.verify(widgetService, Mockito.times(1)).getWidgetById(1L);
    }

    @Test
    public void testGetWidgetById_throwsException() throws Exception {
        Mockito.when(widgetService.getWidgetById(Mockito.anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/widget/getWidgetById")
                        .param("widgetId", "1"))
                .andExpect(status().isBadRequest());

        Mockito.verify(widgetService, Mockito.times(1)).getWidgetById(1L);
    }

    @Test
    public void testEditWidgetDetails() throws Exception {
        Long widgetId = 1L;

        WidgetUpdateDto widgetUpdateDto = new WidgetUpdateDto();
        widgetUpdateDto.setWidgetName("Updated Widget");
        widgetUpdateDto.setDescription("Updated description");

        Mockito.when(widgetService.updateWidgetNameAndDescription(widgetId, widgetUpdateDto)).thenReturn(true);

        mockMvc.perform(put("/api/widget/editWidgetDetails")
                        .param("widgetId", String.valueOf(widgetId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(widgetUpdateDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Widget not found"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDeleteWidgetDetails_thenSuccess200() throws Exception {
        Long widgetId = 1L;

        Mockito.when(widgetService.deleteWidgetByWidgetId(widgetId)).thenReturn(true);

        mockMvc.perform(delete("/api/widget/deleteWidget")
                        .param("widgetId", String.valueOf(widgetId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Widget deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(widgetService, Mockito.times(1)).deleteWidgetByWidgetId(widgetId);
    }

    @Test
    public void testDeleteWidgetDetails_thenNotFound404() throws Exception {
        Long widgetId = 1L;

        Mockito.when(widgetService.deleteWidgetByWidgetId(widgetId)).thenReturn(false);

        mockMvc.perform(delete("/api/widget/deleteWidget")
                        .param("widgetId", String.valueOf(widgetId)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Widget not found"))
                .andExpect(jsonPath("$.success").value(false));

        Mockito.verify(widgetService, Mockito.times(1)).deleteWidgetByWidgetId(widgetId);
    }

    @Test
    public void testDeleteWidgetByWidgetId_throwsException() throws Exception {
        Mockito.when(widgetService.deleteWidgetByWidgetId(Mockito.anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(delete("/api/widget/deleteWidget")
                        .param("widgetId", "1"))
                .andExpect(status().isBadRequest());

        Mockito.verify(widgetService, Mockito.times(1)).deleteWidgetByWidgetId(1L);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
