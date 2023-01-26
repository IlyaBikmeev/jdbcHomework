package ru.bikmeev.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.bikmeev.dao.BookDao;
import ru.bikmeev.model.Book;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private final BookDao dao;

    @GetMapping
    public List<Book> allBooks() {
        System.out.println(dao);
        return dao.getAll();
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable int id) {
        return dao.getById(id);
    }

    @PostMapping
    public String addBook(@RequestBody Book book) {
        dao.insert(book);
        return "Success";
    }
}
