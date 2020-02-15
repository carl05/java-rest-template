package com.fredoliveira.javaresttemplate.domain.exception;

public class StuffNotFoundException extends ApiException {

  public StuffNotFoundException(Long id) {
    super(
        ApiExceptionType.NOT_FOUND,
        "Stuff with id = " + id + " was not found."
    );
  }
  
}
