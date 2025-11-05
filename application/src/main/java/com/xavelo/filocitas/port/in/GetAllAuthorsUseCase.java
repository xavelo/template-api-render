package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.author.Author;

import java.util.List;

public interface GetAllAuthorsUseCase {

    List<Author> getAuthors();
}
