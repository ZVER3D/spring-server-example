package com.example.server.service;

import com.example.server.domain.Role;
import com.example.server.domain.User;
import com.example.server.repos.UserRepo;
import com.sun.mail.smtp.SMTPSendFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
  private final MailService mailService;
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepo userRepo, MailService mailService, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.mailService = mailService;
    this.passwordEncoder = passwordEncoder;
  }

  @Value("${hostname}")
  private String hostname;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Bad credentials");
    }

    return user;
  }

  public boolean addUser(User user) {
    User usr = userRepo.findByUsername(user.getUsername());
    if (usr != null) {
      return false;
    }

    user.setActive(false);
    user.setRoles(Collections.singleton(Role.USER));
    user.setActivationCode(UUID.randomUUID().toString());
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    userRepo.save(user);

    if (!StringUtils.isEmpty(user.getEmail())) {
      try {
        sendMessage(user);
      } catch (SMTPSendFailedException e) {
        userRepo.delete(user);
        return false;
      }
    }

    return true;
  }

  private void sendMessage(User user) throws SMTPSendFailedException {
    String message =
        String.format(
            "Hi, %s! \n"
                + "Please, activate your account: http://%s/activate/%s",
            user.getUsername(), hostname, user.getActivationCode());

    mailService.send(user.getEmail(), "Activation", message);
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
    if (!StringUtils.isEmpty(email) && !email.equals(user.getEmail())) {
      user.setEmail(email);
      user.setActivationCode(UUID.randomUUID().toString());
      user.setActive(false);
      try {
        sendMessage(user);
      } catch (SMTPSendFailedException e) {
        return;
      }
    }

    if (!StringUtils.isEmpty(password)) {
      user.setPassword(passwordEncoder.encode(password));
    }

    userRepo.save(user);
  }

  public void subscribe(User currentUser, User user) {
    user.getSubscribers().add(currentUser);

    userRepo.save(user);
  }

  public void unsubscribe(User currentUser, User user) {
    user.getSubscribers().remove(currentUser);

    userRepo.save(user);
  }
}
