package com.fredoliveira.javaresttemplate.data.repository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fredoliveira.javaresttemplate.domain.entity.Stuff;
import com.fredoliveira.javaresttemplate.domain.repository.StuffRepository;
import org.springframework.stereotype.Repository;


public @Repository class StuffRepositoryImpl implements StuffRepository {

  private List<Stuff> stuffs = new ArrayList<>(Arrays.asList(
    Stuff.builder().id(1L).name("Rock").build(),
    Stuff.builder().id(2L).name("Chair").build(),
    Stuff.builder().id(3L).name("Data").build()
  ));

  @Override public List<Stuff> findAll() {
    return stuffs;
  }

  @Override public Optional<Stuff> findById(Long id) {
    return stuffs
      .stream()
      .filter(stuff -> stuff.getId().equals(id))
      .findAny();
  }

  @Override public Stuff save(Stuff stuff) {
    stuff.setId(new SecureRandom().nextLong());
    stuffs.add(stuff);
    return stuff;
  }

  @Override public void delete(Long id) {
    findById(id)
      .ifPresent(stuff -> stuffs.remove(stuff));
  }

  @Override public void deleteAll() {
    stuffs = new ArrayList<>();
  }

}
