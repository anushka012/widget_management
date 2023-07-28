package com.example.widget_management.Widget;

import com.example.widget_management.Dto.WidgetDto;
import com.example.widget_management.Dto.WidgetUpdateDto;
import com.example.widget_management.Entity.Widget;
import com.example.widget_management.Repository.WidgetRepository;
import com.example.widget_management.Service.Impl.WidgetServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WidgetServiceImplTest {
    @InjectMocks
    private WidgetServiceImpl widgetService;

    @Mock
    private WidgetRepository widgetRepository;


    @Test
    public void testCreateNewWidgetImpl_thenSuccess200() {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setWidgetName("Test Widget");
        widgetDto.setWidgetId(1234L);
        widgetDto.setCreationDate(new Date());
        widgetDto.setDescription("This is a test widget");

        Boolean result = widgetService.createNewWidget(widgetDto);

        ArgumentCaptor<Widget> widgetCaptor = ArgumentCaptor.forClass(Widget.class);
        Mockito.verify(widgetRepository).save(widgetCaptor.capture());

        Widget savedWidget = widgetCaptor.getValue();
        Assert.assertEquals(widgetDto.getWidgetId(), savedWidget.getWidgetId());
        Assert.assertEquals(widgetDto.getWidgetName(), savedWidget.getWidgetName());
        Assert.assertTrue(result);
    }

    @Test
    public void testCreateNewWidgetImpl_elseCase() {
        Boolean result = widgetService.createNewWidget(null);

        Assert.assertFalse(result);
        Mockito.verifyNoInteractions(widgetRepository);
    }

    @Test
    public void testGetAllWidgetsImpl() {
        List<Widget> mockedWidgets = new ArrayList<>();
        Widget widget1 = new Widget();
        widget1.setWidgetId(1L);
        widget1.setWidgetName("Widget 1");
        widget1.setDescription("Description for Widget 1");
        widget1.setCreationDate(new Date());
        mockedWidgets.add(widget1);

        Widget widget2 = new Widget();
        widget2.setWidgetId(2L);
        widget2.setWidgetName("Widget 2");
        widget2.setDescription("Description for Widget 2");
        widget2.setCreationDate(new Date());
        mockedWidgets.add(widget2);

        Mockito.when(widgetRepository.findAll()).thenReturn(mockedWidgets);

        List<WidgetDto> result = widgetService.getAllWidgets();
        Assert.assertEquals(2, result.size());

        WidgetDto widgetDto1 = result.get(0);
        Assert.assertEquals(1L, widgetDto1.getWidgetId().longValue());
        Mockito.verify(widgetRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetWidgetByIdImpl() {
        Long widgetId = 1L;

        Widget widget1 = new Widget();
        widget1.setWidgetId(widgetId);
        widget1.setWidgetName("Widget 1");
        widget1.setDescription("Description for Widget 1");

        Widget widget2 = new Widget();
        widget2.setWidgetId(widgetId);
        widget2.setWidgetName("Widget 2");
        widget2.setDescription("Description for Widget 2");

        Mockito.when(widgetRepository.findAllByWidgetId(widgetId)).thenReturn(Arrays.asList(widget1, widget2));

        List<WidgetDto> widgetDtoList = widgetService.getWidgetById(widgetId);

        Assert.assertEquals(2, widgetDtoList.size());
        Assert.assertEquals("Widget 1", widgetDtoList.get(0).getWidgetName());
        Assert.assertEquals("Description for Widget 2", widgetDtoList.get(1).getDescription());

        Mockito.verify(widgetRepository, Mockito.times(1)).findAllByWidgetId(widgetId);
    }

    @Test
    public void testUpdateWidgetNameAndDescription_Success() {
        Long widgetId = 1L;

        WidgetUpdateDto widgetUpdateDto = new WidgetUpdateDto();
        widgetUpdateDto.setWidgetName("Updated Widget");
        widgetUpdateDto.setDescription("Updated description");

        Mockito.when(widgetRepository.updateWidgetNameAndDescription(widgetId, widgetUpdateDto.getWidgetName(), widgetUpdateDto.getDescription()))
                .thenReturn(1);
        Boolean result = widgetService.updateWidgetNameAndDescription(widgetId, widgetUpdateDto);

        Mockito.verify(widgetRepository, Mockito.times(1)).updateWidgetNameAndDescription(widgetId, widgetUpdateDto.getWidgetName(), widgetUpdateDto.getDescription());
        Assert.assertTrue(result);
    }

    @Test
    public void testDeleteWidgetByWidgetId() {
        Long widgetId = 1L;

        Mockito.when(widgetRepository.deleteWidgetByWidgetId(widgetId)).thenReturn(1);
        Boolean result = widgetService.deleteWidgetByWidgetId(widgetId);

        Assert.assertTrue(result);
        Mockito.verify(widgetRepository, Mockito.times(1)).deleteWidgetByWidgetId(widgetId);
    }

}
