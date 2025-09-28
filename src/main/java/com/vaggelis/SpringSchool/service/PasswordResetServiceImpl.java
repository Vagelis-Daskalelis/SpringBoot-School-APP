package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.models.PasswordResetToken;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.IPasswordResetTokenRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements IPasswordResetService{

    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;

    /**Gives you a token for creating a new password
     *
     * @param email
     */
    @Override
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepo.save(resetToken);

        String link = "http://localhost:8089/reset-password?token=" + token;
        sendEmail(user.getEmail(), link);

    }


    /**creates a new password after giving the token and your new password
     *
     * @param token
     * @param newPassword
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (prt.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepo.delete(prt); // Invalidate token

    }


    // Dummy email sender for demo purposes
    // Writes the password to the console
    private void sendEmail(String to, String link){
        System.out.println("Sending password reset link to " + to);
        System.out.println("Link: " + link);
    }
}