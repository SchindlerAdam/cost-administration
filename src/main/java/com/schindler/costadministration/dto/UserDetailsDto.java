package com.schindler.costadministration.dto;

import com.schindler.costadministration.entities.Cost;
import com.schindler.costadministration.entities.Goal;
import com.schindler.costadministration.entities.Token;
import com.schindler.costadministration.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserDetailsDto {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private Integer balance;
    private List<Cost> costList = new ArrayList<>();
    private List<Goal> goalList = new ArrayList<>();
}
