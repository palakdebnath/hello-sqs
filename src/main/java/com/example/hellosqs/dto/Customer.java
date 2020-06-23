package com.example.hellosqs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
  String id;
  String name;

  @JsonCreator
  public Customer(@JsonProperty("id") String id, @JsonProperty("name") String name) {
    this.id = id;
    this.name = name;
  }
  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Customer{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
  }
}
