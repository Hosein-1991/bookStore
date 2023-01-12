package com.example.bookStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class UIController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @GetMapping("/")
    public String viewHomePage(Model model) throws IOException {
        model.addAttribute("listBookDocuments",elasticSearchQuery.searchAllDocuments());
        return "index";
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("book") Book book) throws IOException {
        elasticSearchQuery.createOrUpdateDocument(book);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {

        Book book = elasticSearchQuery.getDocumentById(id);
        model.addAttribute("book", book);
        return "updateBookDocument";
    }

    @GetMapping("/showNewBookForm")
    public String showNewEmployeeForm(Model model) {
        // create model attribute to bind form data
        Book book = new Book();
        model.addAttribute("book", book);
        return "newBookDocument";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(value = "id") String id) throws IOException {

        this.elasticSearchQuery.deleteDocumentById(id);
        return "redirect:/";
    }
}