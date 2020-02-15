package com.fredoliveira.javaresttemplate.domain.service;

import java.util.List;

import com.fredoliveira.javaresttemplate.domain.entity.Stuff;
import com.fredoliveira.javaresttemplate.domain.exception.StuffNotFoundException;
import com.fredoliveira.javaresttemplate.domain.repository.StuffRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2 @Service public class StuffService {

  private final StuffRepository repository;

  public StuffService(StuffRepository repository) {
    this.repository = repository;
  }

  public List<Stuff> findAll() {
    return repository.findAll();
  }

  public Stuff findBy(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> {
        log.error("Stuff with id = {} was not found.", id);
        return new StuffNotFoundException(id);
      });
  }

  public Stuff save(Stuff stuff) {
    return repository.save(stuff);
  }

  public Stuff update(Long id, Stuff stuff) {
    return repository.findById(id)
      .map(repository::save)
      .orElseThrow(() -> {
        log.error(
            "Unable to save the stuff because it was not possible to get the stuff by id = {}.",
            stuff.getId()
        );
        return new StuffNotFoundException(stuff.getId());
      });
  }

  public void delete(Long id) {
    repository.delete(id);
  }
}
