package com.schindler.costadministration.entities;

import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app-user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "balance")
    private Integer balance;

    @OneToMany(mappedBy = "user")
    private List<Cost> costList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Goal> goalList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Token> tokens = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "verified")
    private Boolean isVerified;
    @Column(name = "deleted")
    private Boolean isDeleted;

    public User(RegisterUserModel userCommand) {
        this.username = userCommand.getUsername();
        this.password = userCommand.getPassword();
        this.email = userCommand.getEmail();
        this.balance = 0;
        this.isDeleted = false;
        this.role = Role.USER;
        this.isVerified = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return this.isVerified;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getRealUserName() {
        return this.username;
    }

    public void setRealUserName(String userName) {
        this.username = userName;
    }
}
