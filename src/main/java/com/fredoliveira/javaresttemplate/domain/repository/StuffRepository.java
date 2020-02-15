package com.fredoliveira.javaresttemplate.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fredoliveira.javaresttemplate.domain.entity.Stuff;

public interface StuffRepository {

  List<Stuff> findAll();

  Optional<Stuff> findById(Long id);

  Stuff save(Stuff stuff);

  void delete(Long id);

  void deleteAll();
}
