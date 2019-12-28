package com.codegym.controllers;

import com.codegym.models.Book;
import com.codegym.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@CrossOrigin(maxAge = 3600)
@RestController
public class BookController {
    @Autowired
    BookService bookService;

//    -----------------------get book list----------------------------
    @RequestMapping(value = "/books/", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllBook(){
        List<Book> books = bookService.findAll();
        if(books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    // ---------------------get id of a customer------------------------
    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable ("id") Long id){
        Book book = bookService.findById(id);
        if(book == null){
            System.out.println("Book with id" + id +"not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Book>(book,HttpStatus.OK);

    }

    //----------------------------create a book-------------------------
    @RequestMapping(value = "/books/", method = RequestMethod.POST)
    public ResponseEntity<Void> createBook(@RequestBody Book book, UriComponentsBuilder ucBuilder){
        System.out.println("creating a new book: " + book.getName());
        bookService.save(book);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/books/{id}").buildAndExpand(book.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //----------------------update a book--------------------------
    @RequestMapping(value = "/books/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book){
        System.out.println("updated" + book.getName());
        Book currentBook = bookService.findById(id);
        if(currentBook == null){
            System.out.println("the book with id " + book.getId() + "not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }
        currentBook.setName(book.getName());
        currentBook.setRead(book.getRead());
        currentBook.setId(book.getId());
        bookService.save(currentBook);
        return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
    }

    //------------------- Delete a Customer --------------------------------------------------------

    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Book> deleteBook(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting book with id " + id);

        Book book = bookService.findById(id);
        if (book == null) {
            System.out.println("Unable to delete. book with id " + id + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        bookService.remove(id);
        return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
    }
}