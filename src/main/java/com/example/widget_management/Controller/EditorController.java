package com.example.widget_management.Controller;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.EditorUpdateDto;
import com.example.widget_management.Model.CustomResponse;
import com.example.widget_management.Service.EditorService;
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
@RequestMapping("/api/editor")
public class EditorController {
    @Autowired
    EditorService editorService;

    @PostMapping("/createEditor")
    @ApiOperation(value = "Create a new editor associated with a widget by providing the widget ID, name, and initial content")
    public ResponseEntity<CustomResponse> createEditor(@RequestBody EditorDto editorDto) {
        try {
            CustomResponse response = new CustomResponse("Editor created successfully!", editorService.createNewEditor(editorDto));
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            CustomResponse response = new CustomResponse("Error creating editor: "
                    + e.getMessage(), false);
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/getEditorDetailsByEditorId")
    @ApiOperation(value = "Retrieve the details of a specific editor by its ID")
    public ResponseEntity<List<EditorDto>> getEditorByEditorId(@RequestParam Long editorId) {
        try {
            return new ResponseEntity<>(editorService.getDetailsByEditorId(editorId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEditorDetailsByWidgetId")
    @ApiOperation(value = "Retrieve all editors associated with a specific widget")
    public ResponseEntity<List<EditorDto>> getEditorByWidgetId(@RequestParam Long widgetId) {
        try {
            return new ResponseEntity<>(editorService.getDetailsByWidgetId(widgetId), HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateEditorDetails")
    @ApiOperation(value = "Update the name and content of an editor")
    public ResponseEntity<CustomResponse> updateEditorDetails(@RequestParam Long editorId, @RequestBody EditorUpdateDto editorUpdateDto) {
        try {
            boolean isUpdated = editorService.updateEditorNameAndContent(editorId, editorUpdateDto);
            if (isUpdated) {
                return new ResponseEntity<>(new CustomResponse( "Editor details updated successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Editor not found", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error updating widget details: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteEditor")
    @ApiOperation(value = "Delete an editor by its ID")
    public ResponseEntity<CustomResponse> deleteEditorDetails(@RequestParam Long editorId) {
        try {
            boolean isDeleted = editorService.deleteEditorByEditorId(editorId);
            if (isDeleted) {
                return new ResponseEntity<>(new CustomResponse( "Editor deleted successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Editor not found", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error deleting editor: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }


}
