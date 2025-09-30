package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.service.image.IImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image")
    public ResponseEntity<String> saveImages(
            @Parameter(description = "File to upload")
            @RequestParam("file") MultipartFile file) {
        try {
            imageService.addYourImage(file);
            return ResponseEntity.ok("Image added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed!");
        }
    }
}
