package com.example.widget_management.Service;

import com.example.widget_management.Dto.WidgetDto;
import com.example.widget_management.Dto.WidgetUpdateDto;

import java.util.List;

public interface WidgetService {
    public Boolean createNewWidget(WidgetDto widgetDto);
    public List<WidgetDto> getAllWidgets();
    public List<WidgetDto> getWidgetById(Long widgetId);
    public Boolean updateWidgetNameAndDescription(Long widgetId,WidgetUpdateDto widgetUpdateDto);
    public Boolean deleteWidgetByWidgetId(Long widgetId);
}
