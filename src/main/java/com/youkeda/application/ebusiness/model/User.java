package com.youkeda.application.ebusiness.model;

import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

public class User extends BaseID<Long> {

   private String userName;

    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull
    private String password;

    private String mobile;

    private String email;

    private  String name;

    private String gender;

 public String getUserName() {
  return userName;
 }

 public void setUserName(String userName) {
  this.userName = userName;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public String getMobile() {
  return mobile;
 }

 public void setMobile(String mobile) {
  this.mobile = mobile;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getGender() {
  return gender;
 }

 public void setGender(String gender) {
  this.gender = gender;
 }
}
