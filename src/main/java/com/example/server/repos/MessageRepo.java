package com.example.server.repos;

import com.example.server.domain.Message;
import com.example.server.domain.User;
import com.example.server.domain.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepo extends CrudRepository<Message, Long> {
  @Query(
      "select new com.example.server.domain.dto.MessageDto("
          + "m, "
          + "count(ml), "
          + "sum(case when ml = :user then 1 else 0 end) > 0"
          + ") "
          + "from Message as m left join m.likes ml "
          + "group by m ")
  Page<MessageDto> findAll(@Param("user") User user, Pageable pageable);

  @Query(
      "select new com.example.server.domain.dto.MessageDto("
          + "m, "
          + "count(ml), "
          + "sum(case when ml = :user then 1 else 0 end) > 0"
          + ") "
          + "from Message as m left join m.likes ml "
          + "where m.tag = :tag "
          + "group by m ")
  Page<MessageDto> findByTag(@Param("user") User user, @Param("tag") String tag, Pageable pageable);

  @Query(
      "select new com.example.server.domain.dto.MessageDto("
          + "m, "
          + "count(ml), "
          + "sum(case when ml = :user then 1 else 0 end) > 0"
          + ") "
          + "from Message as m left join m.likes ml "
          + "where m.author = :author"
          + " group by m ")
  Page<MessageDto> findByUser(
      @Param("user") User user, @Param("author") User author, Pageable pageable);
}
