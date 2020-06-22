package com.example.server.service;

import com.example.server.domain.Role;
import com.example.server.domain.User;
import com.example.server.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

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

    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    user.setActivationCode(UUID.randomUUID().toString());

    userRepo.save(user);

    if (!StringUtils.isEmpty(user.getEmail())) {
      String message =
          String.format(
              "Hello, %s! \n"
                  + "Welcome to The Chat, \n"
                  + "Please, visit this link: http://localhost:8080/activate/%s",
              user.getUsername(), user.getActivationCode());

      mailSender.send(user.getEmail(), "Account activation", message);
    }

    return true;
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
}
