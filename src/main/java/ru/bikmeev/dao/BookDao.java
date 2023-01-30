package ru.bikmeev.dao;

import ru.bikmeev.model.Book;

import java.util.List;

public interface BookDao {
    void insert(Book book);
    Book getById(int id);
    List<Book> getAll();
}
