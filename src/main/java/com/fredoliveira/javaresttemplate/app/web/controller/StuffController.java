package com.fredoliveira.javaresttemplate.app.web.controller;

import java.util.List;

import com.fredoliveira.javaresttemplate.domain.entity.Stuff;
import com.fredoliveira.javaresttemplate.domain.service.StuffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stuffs")
public class StuffController {

  private final StuffService service;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public List<Stuff> findAll() {
    log.info("Request to get all stuffs.");
    return service.findAll();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public Stuff findById(@PathVariable Long id) {
    log.info("Request to get a stuff with id = {}.", id);
    return service.findBy(id);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public Stuff save(@RequestBody Stuff register) {
    log.info("Request to save a new stuff.");
    return service.save(register);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public Stuff update(@PathVariable Long id, @RequestBody Stuff register) {
    log.info("Request to updatet a stuff with id = {}.", id);
    return service.update(id, register);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable Long id) {
    log.info("Request to delete a stuff with id = {}.", id);
    service.delete(id);
  }

}
