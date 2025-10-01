package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.exception.image.ImageNotFoundException;
import com.vaggelis.SpringSchool.models.Image;
import com.vaggelis.SpringSchool.service.image.IImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload an image",
            description = "Uploads an image file to the server"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image uploaded successfully",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Upload failed",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    public ResponseEntity<String> saveImages(
            @Parameter(
                    description = "Image file to upload",
                    required = true
            )
            @RequestParam("file") MultipartFile file) {
        try {
            imageService.addYourImage(file);
            return ResponseEntity.ok("Image added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed!");
        }
    }


    @Operation(summary = "Download image by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image file",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @GetMapping(value = "/download/{imageId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getFileType())) // e.g., image/png
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFileName() + "\"")
                    .body(resource);
        } catch (ImageNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete current user's image",
            description = "Deletes the image associated with the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image deleted successfully",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Image or user not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request or deletion failed",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    public ResponseEntity<String> deleteImage() {
        try {
            imageService.deleteImageById();
            return ResponseEntity.ok("Delete success!");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update current user's image",
            description = "Replaces the image associated with the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image updated successfully",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Image or user not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request or update failed",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    public ResponseEntity<String> updateImage(
            @Parameter(
                    description = "New image file to update",
                    required = true
            )
            @RequestParam("file") MultipartFile file) {
        try {
            imageService.updateImage(file);
            return ResponseEntity.ok("Update success!");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
