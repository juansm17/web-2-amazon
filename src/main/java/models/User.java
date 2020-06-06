package models;

import java.sql.Date;
import java.sql.Timestamp;

public class  User {
  private Integer id;
  private String username;
  private String password;
  private String name;
  private String lastName;
  private String email;

  public Integer getId() { return id; }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getLastName() {
    return lastName;
  }

  public void setPassword(String password) { this.password = password; }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Integer id) { this.id = id; }
}

