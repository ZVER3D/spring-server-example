package com.example.server.domain.util;

import com.example.server.domain.User;

public abstract class MessageHelper {
  public static String getAuthorName(User user) {
    return user != null ? user.getUsername() : "<anon>";
  }
}
