package com.example.server.service;

import com.example.server.domain.Role;
import com.example.server.domain.User;
import com.example.server.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class UserServiceTest {
  private final UserRepo userRepo = mock(UserRepo.class);
  private final MailSender mailSender = mock(MailSender.class);
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

  private final UserService userService = new UserService(userRepo, mailSender, passwordEncoder);

  @Test
  void addUser() {
    User user = new User();
    user.setEmail("email@example.com");
    user.setUsername("John Doe");

    boolean isCreated = userService.addUser(user);
    assertTrue(isCreated);
    assertNotNull(user.getActivationCode());
    assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
    assertFalse(user.isActive());
    assertEquals(user.getUsername(), "John Doe");

    Mockito.verify(userRepo, Mockito.times(1)).save(user);
    Mockito.verify(mailSender, Mockito.times(1))
        .send(
            ArgumentMatchers.eq(user.getEmail()),
            ArgumentMatchers.eq("Activation"),
            ArgumentMatchers.contains(user.getActivationCode()));
  }

  @Test
  public void addUserFailed() {
    User user = new User();
    user.setUsername("John");
    user.setEmail("email@email.email");

    Mockito.doReturn(new User()).when(userRepo).findByUsername("John");

    boolean isUserCreated = userService.addUser(user);
    assertFalse(isUserCreated);

    Mockito.verify(userRepo, Mockito.never()).save(ArgumentMatchers.any(User.class));
    Mockito.verify(mailSender, Mockito.never())
        .send(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString());
  }

  @Test
  public void activateUser() {
    User user = new User();
    user.setActivationCode("activate");

    Mockito.doReturn(user).when(userRepo).findByActivationCode("activate");

    boolean isUserActivated = userService.activateUser("activate");
    assertTrue(isUserActivated);
    assertNull(user.getActivationCode());
    assertTrue(user.isActive());

    Mockito.verify(userRepo, Mockito.times(1)).save(user);
  }

  @Test
  public void activateUserFail() {
    User user = new User();
    user.setActivationCode("activate");

    Mockito.doReturn(user).when(userRepo).findByActivationCode("activate");

    boolean isUserActivated = userService.activateUser("bad code");
    assertFalse(isUserActivated);

    Mockito.verify(userRepo, Mockito.never()).save(any(User.class));
  }
}
