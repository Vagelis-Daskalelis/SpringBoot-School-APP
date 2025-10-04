package com.vaggelis.SpringSchool.service.password;

public interface IPasswordResetService {

    void createPasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}
