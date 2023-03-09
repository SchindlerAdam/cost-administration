package com.schindler.costadministration.model;

import com.schindler.costadministration.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenModel {

    private String token;
    private boolean isExpiredByLogout;
    private User user;
}
