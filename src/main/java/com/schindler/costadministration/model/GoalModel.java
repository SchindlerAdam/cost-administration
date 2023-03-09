package com.schindler.costadministration.model;

import lombok.Data;

@Data
public class GoalModel {
    private String goalName;
    private Double goalPrice;
    private Boolean isDeleted;
}
