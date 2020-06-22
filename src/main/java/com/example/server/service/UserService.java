package com.example.server.service;

import com.example.server.domain.Role;
import com.example.server.domain.User;
import com.example.server.repos.UserRepo;
import com.sun.mail.smtp.SMTPSenderFailedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
  private final MailSender mailSender;
  private final UserRepo userRepo;

  public UserService(UserRepo userRepo, MailSender mailSender) {
    this.userRepo = userRepo;
    this.mailSender = mailSender;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepo.findByUsername(username);
  }

  public boolean addUser(User user) {
    User usr = userRepo.findByUsername(user.getUsername());
    if (usr != null) {
      return false;
    }

    user.setActive(false);
    user.setRoles(Collections.singleton(Role.USER));
    user.setActivationCode(UUID.randomUUID().toString());

    userRepo.save(user);

    if (!StringUtils.isEmpty(user.getEmail())) {
      try {
        sendMessage(user);
      } catch (SMTPSenderFailedException e) {
        userRepo.delete(user);
        return false;
      }
    }

    return true;
  }

  private void sendMessage(User user) throws SMTPSenderFailedException {
    String message =
        String.format(
            "Hello, %s! \n"
                + "Welcome to The Chat, \n"
                + "Please, visit this link to activate your account: http://localhost:8080/activate/%s",
            user.getUsername(), user.getActivationCode());

    mailSender.send(user.getEmail(), "Account activation", message);
  }

  public boolean activateUser(String code) {
    User user = userRepo.findByActivationCode(code);
    if (user == null) {
      return false;
    }

    user.setActivationCode(null);
    user.setActive(true);
    userRepo.save(user);

    return true;
  }

  public List<User> findAll() {
    return userRepo.findAll();
  }

  public void saveUser(User user, String username, Map<String, String> form) {
    user.setUsername(username);

    Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
    user.getRoles().clear();
    for (String key : form.keySet()) {
      if (roles.contains(key)) {
        user.getRoles().add(Role.valueOf(key));
      }
    }

    userRepo.save(user);
  }

  public void updateProfile(User user, String password, String email) {
    if (email != null && !email.equals(user.getEmail()) && !email.isEmpty()) {
      user.setEmail(email);
      user.setActivationCode(UUID.randomUUID().toString());
      user.setActive(false);
      try {
        sendMessage(user);
      } catch (SMTPSenderFailedException e) {
        return;
      }
    }

    if (password != null && !password.isEmpty()) {
      user.setPassword(password);
    }

    userRepo.save(user);
  }
}
