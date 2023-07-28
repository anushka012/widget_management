package com.example.widget_management.Controller;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.ImageEditorDto;
import com.example.widget_management.Exception.ResourceNotFoundException;
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
@RequestMapping("/api/imageEditor")
public class ImageEditorController {
    @Autowired
    EditorService editorService;

    @PostMapping("/createImageEditor")
    @ApiOperation(value = "Create a new image editor associated with a widget by providing the widget ID and image URL")
    public ResponseEntity<CustomResponse> createTagEditor(@RequestBody ImageEditorDto imageEditorDto) {
        try {
            boolean isCreated = editorService.createNewImageEditor(imageEditorDto);
            if (isCreated) {
                return new ResponseEntity<>(new CustomResponse("Image editor created successfully", true), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new CustomResponse("Widget not found with ID: " + imageEditorDto.getWidgetId(), false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error creating image editor: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getImageEditorByEditorId")
    @ApiOperation(value = "Retrieve the details of a specific image editor by its ID")
    public ResponseEntity<EditorDto> getImageEditorById(@RequestParam Long editorId) {
        try {
            return new ResponseEntity<>(editorService.getImageEditorById(editorId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getImageEditorByWidgetId")
    @ApiOperation(value = "Retrieve all image editors associated with a specific widget")
    public ResponseEntity<List<EditorDto>> getImageEditorByWidgetId(@RequestParam Long widgetId) {
        try {
            return new ResponseEntity<>(editorService.getImageEditorsByWidgetId(widgetId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateImageUrl")
    @ApiOperation(value = "Update the image URL of an image editor")
    public ResponseEntity<CustomResponse> updateImageUrl(@RequestParam Long editorId, @RequestParam String newImageUrl) {
        try {
            if (editorService.editImageUrl(editorId, newImageUrl)) {
                return new ResponseEntity<>(new CustomResponse("Image Url updated successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Image editor not found with ID: " + editorId, false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error updating image url: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteImageEditor")
    @ApiOperation(value = "Delete an image editor by its ID")
    public ResponseEntity<CustomResponse> deleteImageEditorById(@RequestParam Long editorId) {
        try {
            boolean isDeleted = editorService.deleteImageEditorByEditorId(editorId);
            if (isDeleted) {
                return new ResponseEntity<>(new CustomResponse( "Image Editor deleted successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Image Editor not found", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error deleting image editor: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }
}
