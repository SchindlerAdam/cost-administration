package com.schindler.costadministration.entities;

import com.schindler.costadministration.command.GoalCommand;
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

    public Goal(GoalCommand goalCommand) {
        this.goalName = goalCommand.getGoalName();
        this.goalPrice = goalCommand.getGoalPrice();
        this.isDeleted = goalCommand.getIsDeleted();
    }
}