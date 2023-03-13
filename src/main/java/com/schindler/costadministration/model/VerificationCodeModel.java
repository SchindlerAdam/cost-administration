package com.schindler.costadministration.model;

import com.schindler.costadministration.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCodeModel {

    private String code;
    private Date creationDate;

    private User user;

}
