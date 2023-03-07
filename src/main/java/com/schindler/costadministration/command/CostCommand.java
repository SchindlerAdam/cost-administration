package com.schindler.costadministration.command;

import lombok.Data;

@Data
public class CostCommand {
    private String costName;
    private Double costAmount;
}
