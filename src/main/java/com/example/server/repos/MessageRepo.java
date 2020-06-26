package com.example.server.repos;

import com.example.server.domain.Message;
import com.example.server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepo extends CrudRepository<Message, Long> {
  Page<Message> findAll(Pageable pageable);

  Page<Message> findByTag(String tag, Pageable pageable);

  @Query("from Message as m where m.author = :author")
  Page<Message> findByUser(@Param("author") User author, Pageable pageable);
}
