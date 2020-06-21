package com.example.server.repos;

import java.util.List;

import com.example.server.domain.Message;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Integer> {
  List<Message> findByTag(String tag);
}