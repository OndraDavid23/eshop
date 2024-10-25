package com.eshop.my_eshop.exception;

public class CartNotFoundException extends RuntimeException {
  public CartNotFoundException(String message) {
    super(message);
  }
}
