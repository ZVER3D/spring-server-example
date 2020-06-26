package com.example.server.service;

import com.example.server.domain.User;
import com.example.server.domain.dto.MessageDto;
import com.example.server.repos.MessageRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private final MessageRepo messageRepo;

  public MessageService(MessageRepo messageRepo) {
    this.messageRepo = messageRepo;
  }

  public Page<MessageDto> messageList(User user, String filter, Pageable pageable) {
    if (filter == null || filter.isEmpty()) {
      return messageRepo.findAll(user, pageable);
    }
    return messageRepo.findByTag(user, filter, pageable);
  }

  public Page<MessageDto> messageListForUser(User currentUser, User author, Pageable pageable) {
    return messageRepo.findByUser(currentUser, author, pageable);
  }
}
