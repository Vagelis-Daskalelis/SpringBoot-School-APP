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
public class ImageServiceImpl implements IImageService{

    private final IImageRepository imageRepository;
    private final IUserRepository userRepository;

    /**Get an image by the id
     *
     * @param id
     * @return Image
     * @throws ImageNotFoundException
     */
    @Override
    public Image getImageById(Long id) throws ImageNotFoundException {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(Image.class, id));
    }


    /**Deletes your user's image
     *
     * @throws RuntimeException
     */
    @Transactional
    @Override
    public void deleteImageById() throws RuntimeException {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Image image;

        try {
            //Checks if the users exists
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> {
                        return new RuntimeException("User not found");
                    });

            //Get the users image and if it exists it deletes it
            image = currentUser.getImage();

            if (image == null) throw new RuntimeException("Image not found");

            currentUser.setImage(null);
        } catch (RuntimeException e) {
            throw e;
        }

    }

    /**Adds an image to your user
     *
     * @param file
     */
    @Override
    public void addYourImage(MultipartFile file) {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            //Checks if the users exists
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //Creates an image and adds it to your user
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

    /**Updates your users image
     *
     * @param file
     * @throws RuntimeException
     */
    @Override
    public void updateImage(MultipartFile file) throws RuntimeException {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Image image;

        try {

            //Checks if the users exists
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //Get the users image and if it exists it deletes it
            image = currentUser.getImage();

            //Checks if user has an image and if yes updates it
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
