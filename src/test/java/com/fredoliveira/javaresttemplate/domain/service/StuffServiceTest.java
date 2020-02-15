package com.fredoliveira.javaresttemplate.domain.service;

import java.util.List;
import java.util.Optional;

import com.fredoliveira.javaresttemplate.domain.entity.Stuff;
import com.fredoliveira.javaresttemplate.domain.exception.StuffNotFoundException;
import com.fredoliveira.javaresttemplate.domain.repository.StuffRepository;
import com.fredoliveira.javaresttemplate.mock.StuffMock;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.util.Collections.nCopies;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Runs all tests for service layer of stuff entity")
class StuffServiceTest {

  private StuffService service;
  private StuffRepository stuffRepository;

  @BeforeEach void beforeEach() {
    stuffRepository = Mockito.mock(StuffRepository.class);
    service = new StuffService(stuffRepository);
  }

  @Test
  @DisplayName("should find all stuffs with empty result")
  void findAllWithEmptyResult() {
    Mockito.when(stuffRepository.findAll()).thenReturn(Lists.emptyList());

    List<Stuff> stuffs = service.findAll();

    assertTrue(stuffs.isEmpty());

    Mockito.verify(stuffRepository).findAll();
  }

  @Test
  @DisplayName("should find all stuffs with one result")
  void findAllWithOneResult() {
    Stuff stuff = StuffMock.getOne();

    Mockito.when(stuffRepository.findAll())
      .thenReturn(Lists.list(stuff));

    List<Stuff> stuffs = service.findAll();

    assertFalse(stuffs.isEmpty());

    stuffs.forEach(it -> {
      assertEquals(stuff.getId(), it.getId());
      assertEquals(stuff.getName(), it.getName());
    });

    Mockito.verify(stuffRepository).findAll();
  }

  @Test
  @DisplayName("should find all stuffs with three results")
  void findAllWithThreeResults() {
    List<Stuff> stuffs = nCopies(3, StuffMock.getOne());

    Mockito.when(stuffRepository.findAll())
      .thenReturn(stuffs);

    List<Stuff> result = service.findAll();

    assertFalse(result.isEmpty());

    for (int i = 0; i < stuffs.size(); i++) {
      assertEquals(stuffs.get(i).getId(), result.get(i).getId());
      assertEquals(stuffs.get(i).getName(), result.get(i).getName());
    }

    Mockito.verify(stuffRepository).findAll();
  }

  @Test
  @DisplayName("should find a stuff by id successfully")
  void findByIdSuccessfully() {
    Stuff stuff = StuffMock.getOne();

    Mockito.when(stuffRepository.findById(stuff.getId()))
      .thenReturn(Optional.of(stuff));

    Stuff result = service.findBy(stuff.getId());

    assertNotNull(result);
    assertEquals(stuff, result);

    Mockito.verify(stuffRepository).findById(stuff.getId());
  }

  @Test
  @DisplayName("should not find an stuff by id")
  void notFindById() {
    Stuff stuff = StuffMock.getOne();

    Mockito.when(stuffRepository.findById(stuff.getId())).thenReturn(Optional.empty());

    assertThrows(StuffNotFoundException.class, () -> service.findBy(stuff.getId()));

    Mockito.verify(stuffRepository).findById(stuff.getId());
  }

  @Test
  @DisplayName("should save a new stuff")
  void saveNewStuffSuccessfully() {
    Stuff stuff = StuffMock.getOne();
    Mockito.when(stuffRepository.save(stuff)).thenReturn(stuff);

    Stuff result = service.save(stuff);

    assertEquals(stuff, result);

    Mockito.verify(stuffRepository).save(stuff);
  }

  @Test
  @DisplayName("should update an stuff sucessfully")
  void updateStuffSucessfully() {
    Stuff stuff = StuffMock.getOne();

    Mockito.when(stuffRepository.findById(stuff.getId())).thenReturn(Optional.of(stuff));
    Mockito.when(stuffRepository.save(stuff)).thenReturn(stuff);

    Stuff result = service.update(stuff.getId(), stuff);

    assertEquals(stuff, result);

    Mockito.verify(stuffRepository).findById(stuff.getId());
    Mockito.verify(stuffRepository).save(stuff);
  }

  @Test
  @DisplayName("should not update an stuff when it does not exist")
  void notUpdateStuffWhenItDoesNotExist() {
    Stuff stuff = StuffMock.getOne();

    Mockito.when(stuffRepository.findById(stuff.getId())).thenReturn(Optional.empty());

    assertThrows(StuffNotFoundException.class, () -> service.update(stuff.getId(), stuff));

    Mockito.verify(stuffRepository).findById(stuff.getId());
    Mockito.verify(stuffRepository, Mockito.times(0)).save(stuff);
  }

  @Test
  @DisplayName("shoud delete an stuff successfully")
  void shouldDeleteStuffSuccessfully() {
    Stuff stuff = StuffMock.getOne();

    Mockito.doNothing().when(stuffRepository).delete(stuff.getId());

    service.delete(stuff.getId());

    Mockito.verify(stuffRepository).delete(stuff.getId());
  }

}
