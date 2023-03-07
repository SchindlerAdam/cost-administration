package com.schindler.costadministration.command;

import lombok.Data;

@Data
public class GoalCommand {
    private String goalName;
    private Double goalPrice;
    private Boolean isDeleted;
}
