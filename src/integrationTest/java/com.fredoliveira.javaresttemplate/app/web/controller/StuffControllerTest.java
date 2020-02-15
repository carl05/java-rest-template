package com.fredoliveira.javaresttemplate.app.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredoliveira.javaresttemplate.JavaRestTemplateApplication;
import com.fredoliveira.javaresttemplate.domain.entity.Stuff;
import com.fredoliveira.javaresttemplate.domain.repository.StuffRepository;
import com.fredoliveira.javaresttemplate.mock.StuffMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.fredoliveira.javaresttemplate.domain.exception.ApiException.ApiExceptionType.NOT_FOUND;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(classes = {JavaRestTemplateApplication.class})
@ContextConfiguration
@DisplayName("Runs all tests for stuff management feature")
class StuffControllerTest {

  private static final String API_STUFFS_PATH = "/api/stuffs";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;
  
  @Autowired private StuffRepository stuffRepository;

  @BeforeEach void beforeEach() {
    stuffRepository.deleteAll();
  }

  @Nested
  @DisplayName("GET /api/stuffs")
  class FindAll {

    @Test
    @DisplayName("should get all stuffs with no result")
    void findAllWithThreeResult() throws Exception {
      mockMvc.perform(get(API_STUFFS_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("should get all stuffs with one result")
    void findAllWithFourResult() throws Exception {
      Stuff stuff = StuffMock.getOne();

      stuffRepository.save(stuff);

      mockMvc.perform(get(API_STUFFS_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(stuff.getId())))
        .andExpect(jsonPath("$[0].name", is(stuff.getName())));
    }

    @Test
    @DisplayName("should get all stuffs with three results")
    void findAllWithThreeResults() throws Exception {
      Stuff stuff1 = StuffMock.getOne();
      Stuff stuff2 = StuffMock.getOne();
      Stuff stuff3 = StuffMock.getOne();

      stuffRepository.save(stuff1);
      stuffRepository.save(stuff2);
      stuffRepository.save(stuff3);

      mockMvc.perform(get(API_STUFFS_PATH))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(3)))
          .andExpect(jsonPath("$[:3].id",
          hasItems(stuff1.getId(), stuff2.getId(), stuff3.getId())))
          .andExpect(jsonPath("$[:3].name",
          hasItems(stuff1.getName(), stuff2.getName(), stuff3.getName())));
    }

  }

  @Nested
  @DisplayName("GET /api/stuffs/{id}")
  class FindById {

    @Test
    @DisplayName("should get a stuff by id")
    void findByIdWithResult() throws Exception {
      Stuff stuff = StuffMock.getOne();

      stuffRepository.save(stuff);

      mockMvc.perform(get("/api/stuffs/" + stuff.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(stuff.getId())))
        .andExpect(jsonPath("$.name", is(stuff.getName())));
    }

    @Test
    @DisplayName("should not get a stuff by id when it does not exist")
    void findByIdWithNoResult() throws Exception {
      Stuff stuff = StuffMock.getOne();

      String message = String.format(
          "Stuff with id = %s was not found.",
          stuff.getId()
      );

      mockMvc.perform(get("/api/stuffs/" + stuff.getId()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.type", is(NOT_FOUND.toString())))
        .andExpect(jsonPath("$.message", is(message)));
    }

  }

  @Nested
  @DisplayName("POST /api/stuffs")
  class Save {

    @Test
    @DisplayName("should save a new stuff")
    void saveSuccessfully() throws Exception {
      Stuff stuff = StuffMock.getOne();

      MockHttpServletRequestBuilder request = post(API_STUFFS_PATH)
          .content(mapper.writeValueAsString(stuff))
          .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.name", is(stuff.getName())));
    }

  }

  @Nested
  @DisplayName("PUT /api/stuffs/{id}")
  class Update {

    @Test
    @DisplayName("should update a stuff")
    void updateSuccessfully() throws Exception {
      Stuff stuff = StuffMock.getOne();

      stuffRepository.save(stuff);

      stuff.setName("new stuff name");

      MockHttpServletRequestBuilder request = put("/api/stuffs/" + stuff.getId())
          .content(mapper.writeValueAsString(stuff))
          .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.name", is(stuff.getName())));
    }

    @Test
    @DisplayName("should not update a stuff when it does not exist in database")
    void updateWithFailureWhenItDoesNotExistInDatabase() throws Exception {
      Stuff stuff = StuffMock.getOne();

      stuffRepository.save(StuffMock.getOne());

      MockHttpServletRequestBuilder request = put("/api/stuffs/" + stuff.getId())
          .content(mapper.writeValueAsString(stuff))
          .contentType(MediaType.APPLICATION_JSON);

      String message = String.format(
          "Stuff with id = %s was not found.",
          stuff.getId()
      );

      mockMvc.perform(request)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.type", is(NOT_FOUND.toString())))
        .andExpect(jsonPath("$.message", is(message)));
    }

  }

  @Nested
  @DisplayName("DELETE /api/stuffs{id}")
  class Delete {

    @Test
    @DisplayName("should delete a stuff by id when it exists in database")
    void deleteByIdWhenItExistsInDatabase() throws Exception {
      Stuff stuff = StuffMock.getOne();

      stuffRepository.save(stuff);

      mockMvc.perform(delete("/api/stuffs/" + stuff.getId()))
        .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should delete a stuff by id when it does not exist in database")
    void deleteByIdWhenItDoesNotExistInDatabase() throws Exception {
      Stuff stuff = StuffMock.getOne();

      mockMvc.perform(delete("/api/stuffs/" + stuff.getId()))
        .andExpect(status().isNoContent());
    }

  }

}
