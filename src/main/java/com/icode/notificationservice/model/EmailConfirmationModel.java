package com.icode.notificationservice.model;

import lombok.Data;

@Data
public class EmailConfirmationModel {

    private String email;
    private String confirmationToken;
}
