package com.example.bookStore;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "books";


    public String createOrUpdateDocument(Book book) throws IOException {

        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(book.getId())
                .document(book)
        );
        if(response.result().name().equals("Created")){
            return new StringBuilder("Document has been successfully created.").toString();
        }else if(response.result().name().equals("Updated")){
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }

    public Book getDocumentById(String bookId) throws IOException{
        Book book = null;
        GetResponse<Book> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(bookId),
                Book.class
        );

        if (response.found()) {
            book = response.source();
            System.out.println("Book name " + book.getName());
        } else {
            System.out.println ("Book not found");
        }

        return book;
    }

    public String deleteDocumentById(String bookId) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(bookId));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Book with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("Book not found");
        return new StringBuilder("Book with id " + deleteResponse.id()+" does not exist.").toString();

    }

    public  List<Book> searchAllDocuments() throws IOException {

        SearchRequest searchRequest =  SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse =  elasticsearchClient.search(searchRequest, Book.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Book> books = new ArrayList<>();
        for(Hit object : hits){

            System.out.print(((Book) object.source()));
            books.add((Book) object.source());

        }
        return books;
    }
}
