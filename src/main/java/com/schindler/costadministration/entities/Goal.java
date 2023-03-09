package com.schindler.costadministration.entities;

import com.schindler.costadministration.model.GoalModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(name = "name")
    private String goalName;

    @Column(name = "price")
    private Double goalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "deleted")
    private Boolean isDeleted = false;

    public Goal(GoalModel goalModel) {
        this.goalName = goalModel.getGoalName();
        this.goalPrice = goalModel.getGoalPrice();
        this.isDeleted = goalModel.getIsDeleted();
    }
}