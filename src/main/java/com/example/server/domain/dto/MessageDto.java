package com.example.server.domain.dto;

import com.example.server.domain.Message;
import com.example.server.domain.User;
import com.example.server.domain.util.MessageHelper;

public class MessageDto {
  private final Long id;
  private final String text;
  private final String tag;
  private final User author;
  private final String filename;
  private final Long likes;
  private final boolean meLiked;

  public MessageDto(Message message, Long likes, boolean meLiked) {
    this.id = message.getId();
    this.text = message.getText();
    this.tag = message.getTag();
    this.author = message.getAuthor();
    this.filename = message.getFilename();
    this.likes = likes;
    this.meLiked = meLiked;
  }

  public String getAuthorName() {
    return MessageHelper.getAuthorName(author);
  }

  public Long getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public String getTag() {
    return tag;
  }

  public User getAuthor() {
    return author;
  }

  public String getFilename() {
    return filename;
  }

  public Long getLikes() {
    return likes;
  }

  public boolean isMeLiked() {
    return meLiked;
  }

  @Override
  public String toString() {
    return "MessageDto{"
        + "id="
        + id
        + ", author="
        + author
        + ", likes="
        + likes
        + ", meLiked="
        + meLiked
        + '}';
  }
}
