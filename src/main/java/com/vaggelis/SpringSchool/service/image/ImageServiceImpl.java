package com.vaggelis.SpringSchool.service.image;

import com.vaggelis.SpringSchool.dto.ImageDTO;
import com.vaggelis.SpringSchool.exception.image.ImageNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Image;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.IImageRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements IImageService{

    private final IImageRepository imageRepository;
    private final IUserRepository userRepository;

    @Override
    public Image getImageById(Long id) throws ImageNotFoundException {
        return null;
    }

    @Override
    public void deleteImageById(Long id) throws ImageNotFoundException {

    }

//    @Override
//    public void addYourImage(MultipartFile file) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        try {
//            User currentUser = userRepository.findByEmail(currentUserEmail)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            Image image = new Image();
//            image.setFileName(file.getOriginalFilename());
//            image.setFileType(file.getContentType());
//            image.setImage(file.getBytes());  // assuming image field is byte[]
//
//            Image savedImage = imageRepository.save(image);
//
//            // Add the managed entity directly to the user
//            currentUser.addImage(savedImage);
//
//            // Save user so the relationship gets persisted
//            userRepository.save(currentUser);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to save image: " + e.getMessage());
//        }
//    }


    @Override
    public void addYourImage(MultipartFile file) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Adding image for user with email: {}", currentUserEmail);

        try {
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> {
                        log.error("User with email {} not found", currentUserEmail);
                        return new RuntimeException("User not found");
                    });

            log.debug("Found user: {}", currentUser.getId());

            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes());

            log.debug("Prepared image entity. fileName={}, fileType={}, size={} bytes",
                    image.getFileName(), image.getFileType(), file.getBytes().length);

            Image savedImage = imageRepository.save(image);
            log.info("Image saved in DB with id={}", savedImage.getId());

            currentUser.addImage(savedImage);
            log.debug("Image with id={} added to user id={}", savedImage.getId(), currentUser.getId());

            userRepository.save(currentUser);
            log.info("User {} updated with new image {}", currentUser.getId(), savedImage.getId());

        } catch (IOException e) {
            log.error("Failed to read file bytes: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }


    @Override
    public void updateImage(MultipartFile file, Long id) throws ImageNotFoundException {

    }
}
