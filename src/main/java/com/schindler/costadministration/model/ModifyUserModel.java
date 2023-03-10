package com.schindler.costadministration.model;

import lombok.Data;

@Data
public class ModifyUserModel {
    private String newUsername;
    private String newPassword;
}
