package com.schindler.costadministration.entities;

import com.schindler.costadministration.command.CostCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cost")
@Getter
@Setter
@NoArgsConstructor
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long costId;

    @Column(name = "name")
    private String costName;

    @Column(name = "amount")
    private Double costAmount;

    @Column(name = "date")
    private LocalDateTime costDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Cost(CostCommand costCommand) {
        this.costName = costCommand.getCostName();
        this.costAmount = costCommand.getCostAmount();
        this.costDate = LocalDateTime.now();
    }

}