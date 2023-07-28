package com.example.widget_management.Service.Impl;

import com.example.widget_management.Dto.WidgetDto;
import com.example.widget_management.Dto.WidgetUpdateDto;
import com.example.widget_management.Entity.Widget;
import com.example.widget_management.Repository.WidgetRepository;
import com.example.widget_management.Service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WidgetServiceImpl implements WidgetService {
    @Autowired
    WidgetRepository widgetRepository;

    @Override
    public Boolean createNewWidget(WidgetDto widgetDto) {
            if(Objects.nonNull(widgetDto)) {
                Widget widget = new Widget();
                widget.setWidgetId(widgetDto.getWidgetId());
                widget.setWidgetName(widgetDto.getWidgetName());
                widget.setDescription(widgetDto.getDescription());
                widget.setCreationDate(widgetDto.getCreationDate());
                widgetRepository.save(widget);
                return true;
            } else {
                return false;
            }
    }

    @Override
    public List<WidgetDto> getAllWidgets() {
        List<Widget> widgets = widgetRepository.findAll();
        List<WidgetDto> widgetDtos = new ArrayList<>();

        for (Widget widget : widgets) {
            WidgetDto widgetDto = new WidgetDto();
            widgetDto.setWidgetId(widget.getWidgetId());
            widgetDto.setWidgetName(widget.getWidgetName());
            widgetDto.setDescription(widget.getDescription());
            widgetDto.setCreationDate(widget.getCreationDate());

            widgetDtos.add(widgetDto);
        }
        return widgetDtos;
    }

    @Override
    public List<WidgetDto> getWidgetById(Long widgetId) {
        List<Widget> widgets = widgetRepository.findAllByWidgetId(widgetId);

        return widgets.stream()
                .map(this::convertToWidgetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean updateWidgetNameAndDescription(Long widgetId, WidgetUpdateDto widgetUpdateDto) {
        int updatedRows = widgetRepository.updateWidgetNameAndDescription(widgetId, widgetUpdateDto.getWidgetName(),
                widgetUpdateDto.getDescription());
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public Boolean deleteWidgetByWidgetId(Long widgetId) {
        int deletedRows = widgetRepository.deleteWidgetByWidgetId(widgetId);
        return deletedRows > 0;
    }

    private WidgetDto convertToWidgetDto(Widget widget) {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setWidgetId(widget.getWidgetId());
        widgetDto.setWidgetName(widget.getWidgetName());
        widgetDto.setDescription(widget.getDescription());
        widgetDto.setCreationDate(widget.getCreationDate());

        return widgetDto;
    }
}
