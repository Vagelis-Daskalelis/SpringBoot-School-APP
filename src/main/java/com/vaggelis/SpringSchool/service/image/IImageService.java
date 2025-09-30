package com.vaggelis.SpringSchool.service.image;

import com.vaggelis.SpringSchool.exception.image.ImageNotFoundException;
import com.vaggelis.SpringSchool.models.Image;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    Image getImageById(Long id)throws ImageNotFoundException;
    void deleteImageById(Long id)throws ImageNotFoundException;
    void addYourImage(MultipartFile file);
    void updateImage(MultipartFile file, Long id)throws ImageNotFoundException;
}
