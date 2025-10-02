package com.vaggelis.SpringSchool.service.image;

import com.vaggelis.SpringSchool.exception.image.ImageNotFoundException;
import com.vaggelis.SpringSchool.models.Image;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.IImageRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(Image.class, id));
    }


    @Transactional
    @Override
    public void deleteImageById() throws RuntimeException {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Image image;

        try {
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> {
                        return new RuntimeException("User not found");
                    });

            image = currentUser.getImage();

            if (image == null) throw new RuntimeException("Image not found");

            currentUser.setImage(null);
        } catch (RuntimeException e) {
            throw e;
        }

    }


    @Override
    public void addYourImage(MultipartFile file) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes());

            Image savedImage = imageRepository.save(image);
            currentUser.addImage(savedImage);
            userRepository.save(currentUser);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }


//    @Override
//    public void addYourImage(MultipartFile file) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Starting image upload for user: {}", currentUserEmail);
//
//        try {
//            User currentUser = userRepository.findByEmail(currentUserEmail)
//                    .orElseThrow(() -> {
//                        log.error("User with email {} not found", currentUserEmail);
//                        return new RuntimeException("User not found");
//                    });
//            log.info("User found: id={}, email={}", currentUser.getId(), currentUser.getEmail());
//
//            Image image = new Image();
//            image.setFileName(file.getOriginalFilename());
//            image.setFileType(file.getContentType());
//            image.setImage(file.getBytes());
//            log.info("Image prepared: filename={}, type={}, size={} bytes",
//                    image.getFileName(), image.getFileType(),
//                    image.getImage() != null ? image.getImage().length : 0);
//
//            Image savedImage = imageRepository.save(image);
//            log.info("Image saved: id={}, filename={}", savedImage.getId(), savedImage.getFileName());
//
//            currentUser.addImage(savedImage);
//            log.info("Image linked to user: userId={}, imageId={}", currentUser.getId(), savedImage.getId());
//
//            userRepository.save(currentUser);
//            log.info("User updated successfully with new image. userId={}", currentUser.getId());
//
//        } catch (IOException e) {
//            log.error("Failed to read file bytes: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to save image: " + e.getMessage());
//        }
//    }

    @Override
    public void updateImage(MultipartFile file) throws RuntimeException {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Image image;

        try {

            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            image = currentUser.getImage();

            if (image == null) throw new RuntimeException("Image not found");

            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes());
            imageRepository.save(image);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
