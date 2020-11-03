package com.icode.notificationservice.model;

import lombok.Data;

@Data
public class ResetPasswordModel {

    private String email;
    private String resetToken;
}
