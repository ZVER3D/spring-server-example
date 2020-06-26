package com.example.server.service;

import com.example.server.domain.Message;
import com.example.server.domain.User;
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

  public Page<Message> messageList(String filter, Pageable pageable) {
    if (filter == null || filter.isEmpty()) {
      return messageRepo.findAll(pageable);
    }
    return messageRepo.findByTag(filter, pageable);
  }

  public Page<Message> messageListForUser(User author, Pageable pageable) {
    return messageRepo.findByUser(author, pageable);
  }
}
