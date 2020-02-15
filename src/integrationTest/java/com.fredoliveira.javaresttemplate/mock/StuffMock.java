package com.fredoliveira.javaresttemplate.mock;

import java.security.SecureRandom;

import com.fredoliveira.javaresttemplate.domain.entity.Stuff;


public class StuffMock {

  public static Stuff getOne() {
    return Stuff
      .builder()
      .id(new SecureRandom().nextLong())
      .name("Something")
      .build();
  }

}
