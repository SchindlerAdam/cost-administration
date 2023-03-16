package com.schindler.costadministration.repository;

import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.exception.exceptions.UserNotFoundException;
import com.schindler.costadministration.model.RegisterUserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    private User user;

    @BeforeEach
    void initUser() {
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        this.user = new User(userModel);
    }

    @Test
    void shouldFindVerifiedAndNotDeletedUserByEmail() {
        // GIVEN
        this.user.setIsVerified(true);

        // WHEN
        underTest.save(user);
        boolean expected = underTest.findUserByEmail("test@test.com").isPresent();

        // THEN
        assertThat(expected).isTrue();
    }

    @Test
    void shouldFindNotVerifiedUserByEmail() {
        // WHEN
        underTest.save(user);
        boolean expected = underTest.findNotVerifiedUserByEmail(this.user.getEmail()).isPresent();

        // THEN
        assertThat(expected).isTrue();
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        assertThatThrownBy(() -> underTest.findUserByEmail("fake@email.com").orElseThrow(UserNotFoundException::new)).isInstanceOf(UserNotFoundException.class).hasMessageContaining("Can not find a user with this email address!");
    }
}