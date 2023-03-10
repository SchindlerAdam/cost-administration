package com.schindler.costadministration.entities;

import com.schindler.costadministration.model.CostModel;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Cost(CostModel costModel) {
        this.costName = costModel.getCostName();
        this.costAmount = costModel.getCostAmount();
        this.costDate = LocalDateTime.now();
    }

}