package com.rest.app;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	BookRepository bookRepository;

	@GetMapping
	public List<Book> getAllBookRecods() {
		return bookRepository.findAll();
	}
	
	@GetMapping(value = "{bookId}")
	public Book getBookById(@PathVariable(value = "bookId") Long bookId) {
		return bookRepository.findById(bookId).get();
		
	}
	
	@PostMapping
	public Book createBookRecord(@RequestBody @Valid Book bookRecord) {
		return bookRepository.save(bookRecord);
	}
	
	@PutMapping
	public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException, javassist.NotFoundException {
		if (bookRecord == null || bookRecord.getBookId() == null) {
			throw new NotFoundException();
		}
		Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());

		if (!optionalBook.isPresent()) {
			throw new javassist.NotFoundException("BOOK with id "+bookRecord.getBookId() + "does not exist." );
		}

		Book existingBookRecord = optionalBook.get();
		existingBookRecord.setName(bookRecord.getName());
		existingBookRecord.setRating(bookRecord.getRating());
		existingBookRecord.setSummary(bookRecord.getSummary());
		
		return bookRepository.save(existingBookRecord);
	}
	
    @DeleteMapping(value = "bookId")
    public void deleteBookById(@PathVariable(value="bookId") Long bookId) throws NotFoundException, javassist.NotFoundException{
   	 if (!bookRepository.findById(bookId).isPresent()) {
   		 	throw new javassist.NotFoundException("BOOK ID "+ bookId + " not present ");
   	 }
   		 
   	 bookRepository.deleteById(bookId);
    }
	
	//TO DO DELETE endpoint using 

}
