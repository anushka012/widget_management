package com.example.widget_management.Controller;

import com.example.widget_management.Dto.EditorDto;
import com.example.widget_management.Dto.TagEditorDto;
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
@RequestMapping("/api/tagEditor")
public class TagEditorController {
    @Autowired
    EditorService editorService;
    @PostMapping("/createTagEditor")
    @ApiOperation(value = "Create a new tag editor associated with a widget by providing the widget ID and tag name")
    public ResponseEntity<CustomResponse> createTagEditor(@RequestBody TagEditorDto tagEditorDto) {
        try {
            boolean isCreated = editorService.createNewTagEditor(tagEditorDto);
            if (isCreated) {
                return new ResponseEntity<>(new CustomResponse("Tag editor created successfully", true), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new CustomResponse("Widget not found with ID: " + tagEditorDto.getWidgetId(), false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error creating tag editor: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getTagEditorByEditorId")
    @ApiOperation(value = "Retrieve the details of a specific tag editor by its ID")
    public ResponseEntity<EditorDto> getTagEditorById(@RequestParam Long editorId) {
        try {
            return new ResponseEntity<>(editorService.getTagEditorById(editorId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getTagEditorByWidgetId")
    @ApiOperation(value = "Retrieve all tag editors associated with a specific widget")
    public ResponseEntity<List<EditorDto>> getTagEditorByWidgetId(@RequestParam Long widgetId) {
        try {
            return new ResponseEntity<>(editorService.getTagEditorsByWidgetId(widgetId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateTagName")
    @ApiOperation(value = "Update the tag name of a tag editor")
    public ResponseEntity<CustomResponse> updateTagName(@RequestParam Long editorId, @RequestParam String newTagName) {
        try {
            if (editorService.editTagName(editorId, newTagName)) {
                return new ResponseEntity<>(new CustomResponse("Tag name updated successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Tag editor not found with ID: " + editorId, false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error updating tag name: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteTagEditor")
    @ApiOperation(value = "Delete a tag editor by its ID")
    public ResponseEntity<CustomResponse> deleteTagEditorById(@RequestParam Long editorId) {
        try {
            boolean isDeleted = editorService.deleteTagEditorByEditorId(editorId);
            if (isDeleted) {
                return new ResponseEntity<>(new CustomResponse( "Tag Editor deleted successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse("Tag Editor not found", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponse("Error deleting tag editor: " + e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }
}
