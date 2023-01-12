package com.example.bookStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @PostMapping("/createOrUpdateDocument")
    public ResponseEntity<Object> createOrUpdateDocument(@RequestBody Book book) throws IOException {
        String response = elasticSearchQuery.createOrUpdateDocument(book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getDocument")
    public ResponseEntity<Object> getDocumentById(@RequestParam String bookId) throws IOException {
        Book book =  elasticSearchQuery.getDocumentById(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteDocumentById(@RequestParam String bookId) throws IOException {
        String response =  elasticSearchQuery.deleteDocumentById(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchDocument")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Book> books = elasticSearchQuery.searchAllDocuments();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}