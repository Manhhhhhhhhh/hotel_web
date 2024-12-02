package com.web.hotel.exception;

public class UserErrorException extends RuntimeException {
  public UserErrorException(String message) {
    super(message);
  }
}
