package com.example.server.domain;

import com.example.server.domain.util.MessageHelper;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Message cant be empty")
  @Length(max = 2048, message = "Message is too long, more than 2kB")
  private String text;

  @Length(max = 255, message = "Tag cant be longer than 255")
  private String tag;

  private String filename;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User author;

  @ManyToMany
  @JoinTable(
      name = "message_likes",
      joinColumns = {@JoinColumn(name = "message_id")},
      inverseJoinColumns = {@JoinColumn(name = "user_id")})
  private Set<User> likes = new HashSet<>();

  public Message(String text, String tag, User user) {
    this.text = text;
    this.tag = tag;
    this.author = user;
  }

  public Message() {}

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getAuthorName() {
    return MessageHelper.getAuthorName(author);
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public String getText() {
    return text;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<User> getLikes() {
    return likes;
  }

  public void setLikes(Set<User> likes) {
    this.likes = likes;
  }
}
