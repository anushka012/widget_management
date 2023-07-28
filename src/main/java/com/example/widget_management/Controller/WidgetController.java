package com.example.widget_management.Controller;

import com.example.widget_management.Dto.WidgetDto;
import com.example.widget_management.Dto.WidgetUpdateDto;
import com.example.widget_management.Model.CustomResponse;
import com.example.widget_management.Service.WidgetService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/widget")
public class WidgetController {
    @Autowired
    WidgetService widgetService;
    @PostMapping("/createWidget")
    @ApiOperation(value = " Creating a new widget with a name and description")
    public ResponseEntity<CustomResponse> createWidget(@RequestBody WidgetDto widgetDto) {
        try {
            CustomResponse response = new CustomResponse("Widget created successfully!", widgetService.createNewWidget(widgetDto));
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            CustomResponse response = new CustomResponse("Error creating widget: "
                    + e.getMessage(), false);
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/getAllWidgets")
    @ApiOperation(value = "Retrieving the details of all widgets")
    public ResponseEntity<List<WidgetDto>> getAllWidgetDetails() {
        try {
            return new ResponseEntity<>(widgetService.getAllWidgets(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getWidgetById")
    @ApiOperation(value = "Retrieving the details of a specific widget by its ID")
    public ResponseEntity<List<WidgetDto>> getWidgetDetailsById(@RequestParam Long widgetId) {
        try {
            return new ResponseEntity<>(widgetService.getWidgetById(widgetId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editWidgetDetails")
    @ApiOperation(value = "Update the name and description of a widget")
    public ResponseEntity<CustomResponse> editWidgetDetails(@RequestParam Long widgetId, @RequestBody WidgetUpdateDto widgetUpdateDto) {
        try {
            boolean isUpdated = widgetService.updateWidgetNameAndDescription(widgetId, widgetUpdateDto);
            if (isUpdated) {
                return new ResponseEntity<>(new CustomResponse( "Widget details updated successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Widget not found", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error updating widget details: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteWidget")
    @ApiOperation(value = "Delete a widget by its ID")
    public ResponseEntity<CustomResponse> deleteWidgetDetails(@RequestParam Long widgetId) {
        try {
            boolean isDeleted = widgetService.deleteWidgetByWidgetId(widgetId);
            if (isDeleted) {
                return new ResponseEntity<>(new CustomResponse( "Widget deleted successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Widget not found", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error deleting widget: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

}
