package com.example.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class LoginTest {
  @Autowired private MockMvc mockMvc;

  @Test
  public void contextLoads() throws Exception {
    mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Chat")))
        .andExpect(content().string(containsString("Login")));
  }

  @Test
  public void accessDenied() throws Exception {
    mockMvc
        .perform(get("/user-list"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }

  @Test
  @Sql(
      value = {"/create-user-before.sql"},
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = {"/create-user-after.sql"},
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void correctLogin() throws Exception {
    mockMvc
        .perform(formLogin().user("u3").password("1"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  @Sql(
      value = {"/create-user-before.sql"},
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      value = {"/create-user-after.sql"},
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void badCredentials() throws Exception {
    mockMvc
        .perform(post("/login").param("user", "u3"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }
}
