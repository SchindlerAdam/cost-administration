package com.schindler.costadministration.entities;

import com.schindler.costadministration.model.TokenModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenId;

    private String tokenContent;

    private boolean isExpiredByLogout;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(TokenModel tokenModel) {
        this.tokenContent = tokenModel.getToken();
        this.isExpiredByLogout = tokenModel.isExpiredByLogout();
        this.user = tokenModel.getUser();
    }

}
