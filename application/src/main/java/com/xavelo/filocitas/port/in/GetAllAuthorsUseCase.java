package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.Author;

import java.util.List;

public interface GetAllAuthorsUseCase {

    List<Author> getAuthors();
}
