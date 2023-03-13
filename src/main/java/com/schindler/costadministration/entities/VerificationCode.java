package com.schindler.costadministration.entities;

import com.schindler.costadministration.model.VerificationCodeModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long verificationId;

    @Column(name = "code")
    private String code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationCode(VerificationCodeModel model) {
        this.code = model.getCode();
        this.creationDate = model.getCreationDate();
        this.user = model.getUser();
    }
}
