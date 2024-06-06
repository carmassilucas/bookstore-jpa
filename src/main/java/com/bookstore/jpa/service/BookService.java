package com.bookstore.jpa.service;

import com.bookstore.jpa.dto.BookRecordDto;
import com.bookstore.jpa.model.BookModel;
import com.bookstore.jpa.model.ReviewModel;
import com.bookstore.jpa.repository.AuthorRepository;
import com.bookstore.jpa.repository.BookRepository;
import com.bookstore.jpa.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookModel> getAllBooks() {
        return this.bookRepository.findAll();
    }

    @Transactional
    public BookModel saveBook(BookRecordDto bookRecordDto) {
        BookModel book = new BookModel();
        book.setTitle(bookRecordDto.title());
        book.setPublisher(this.publisherRepository.findById(bookRecordDto.publisherId()).get());
        book.setAuthors(this.authorRepository.findAllById(bookRecordDto.authorIds()).stream().collect(Collectors.toSet()));

        ReviewModel review = new ReviewModel();
        review.setComment(bookRecordDto.reviewComment());
        review.setBook(book);

        book.setReview(review);

        return this.bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        this.bookRepository.deleteById(id);
    }
}
