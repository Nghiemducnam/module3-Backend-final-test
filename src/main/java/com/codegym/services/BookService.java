package com.codegym.services;

import com.codegym.models.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    void save(Book book);
    void remove(Long id);
}
