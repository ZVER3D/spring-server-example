package com.example.server;

import com.example.server.controllers.MessageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("u3")
@TestPropertySource("/application-test.properties")
@Sql(
    value = {"/create-user-before.sql", "/create-messages-before.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
    value = {"/create-messages-after.sql", "/create-user-after.sql"},
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MessageControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private MessageController controller;

  @Test
  public void mainPage() throws Exception {
    this.mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//*[@id='navbarSupportedContent']/div").string("u3"));
  }

  @Test
  public void messageList() throws Exception {
    this.mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//ul[@id='messages']/li").nodeCount(4));
  }

  @Test
  public void filterMessages() throws Exception {
    this.mockMvc
        .perform(get("/").param("filter", "my-tag"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//ul[@id='messages']/li").nodeCount(3))
        .andExpect(xpath("//ul[@id='messages']/li[@data-id=1]").exists())
        .andExpect(xpath("//ul[@id='messages']/li[@data-id=2]").exists())
        .andExpect(xpath("//ul[@id='messages']/li[@data-id=3]").exists());
    ;
  }

  @Test
  public void addMessage() throws Exception {
    MockHttpServletRequestBuilder multipart =
        multipart("/")
            .file("file", "123".getBytes())
            .param("text", "fifth message")
            .param("tag", "new")
            .with(csrf());

    this.mockMvc
        .perform(multipart)
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

    this.mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//ul[@id='messages']/li").nodeCount(5))
        .andExpect(xpath("//ul[@id='messages']/li[@data-id=10]").exists())
        .andExpect(xpath("//ul[@id='messages']/li[@data-id=10]/div/span").string("fifth message"));
  }
}
