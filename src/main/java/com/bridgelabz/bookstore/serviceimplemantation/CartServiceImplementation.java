package com.bridgelabz.bookstore.serviceimplemantation;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.bridgelabz.bookstore.entity.Book;
import com.bridgelabz.bookstore.entity.CartDetails;
import com.bridgelabz.bookstore.entity.Users;
import com.bridgelabz.bookstore.exception.BookException;
import com.bridgelabz.bookstore.exception.UserException;
import com.bridgelabz.bookstore.repository.BookRepository;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.service.CartService;
import com.bridgelabz.bookstore.utility.JwtService;


@Service
public class CartServiceImplementation implements CartService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;


	



@Transactional
@Override
public List<CartDetails> getBooksfromCart(String token) throws UserException {
	long id = JwtService.parse(token);
	Users user = userRepository.findbyId(id).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "User is not exist"));
	List<CartDetails> cartBooks = user.getBooksCart();
	return cartBooks;
}
	


    @Transactional
	@Override
	public List<CartDetails> addBooksInTOTheCart(String token, Long bookId) throws UserException, BookException {
		ArrayList<Book> booklist = new ArrayList<>();
		long id = JwtService.parse(token);
		int quantity = 1;
		Users user = userRepository.findbyId(id).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "User is not exist"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookException(HttpStatus.NOT_FOUND, "User is not exist"));
		List<Book> books = null;
		for (CartDetails d : user.getBooksCart()) {
			books = d.getBooksList();
		}		
		CartDetails cart = new CartDetails();		
		if (books == null) {
			booklist.add(book);
			cart.setBooksList(booklist);
			cart.setBooksQuantity(quantity);
			user.getBooksCart().add(cart);			
			return userRepository.save(user).getBooksCart();
		}
		Optional<Book> cartbook = books.stream().filter(t -> t.getBookId() == bookId).findFirst();
		if (cartbook.isPresent()) {
			throw new BookException(HttpStatus.NOT_FOUND, "User is not exist");			
		} else {
			booklist.add(book);	
			cart.setBooksList(booklist);
			cart.setBooksQuantity(quantity);
			user.getBooksCart().add(cart);			
		}
		return userRepository.save(user).getBooksCart();
		
	}


     @Transactional
	@Override
	public List<CartDetails> removeBooksFromCart(Long bookId, String token) throws UserException, BookException {
		long id = JwtService.parse(token);

		Users user = userRepository.findbyId(id).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "User is not exist"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookException(HttpStatus.NOT_FOUND, "User is not exist"));
		for(CartDetails cart:user.getBooksCart()) {			
			boolean notExist = cart.getBooksList().stream().noneMatch(books -> books.getBookId() == bookId);			
			if (!notExist) {
				cart.getBooksList().remove(book);
				return userRepository.save(user).getBooksCart();			
			} 
		}
		return null;	
	}



     @Transactional
 	@Override
 	public List<CartDetails> addBooksQuantityToCart(String token, long bookId, long quantity) throws UserException,BookException {

     	long id = JwtService.parse(token);

     	Users user = userRepository.findbyId(id).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "User is not exist"));
 		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookException(HttpStatus.NOT_FOUND, "book is not exist"));
 		for(CartDetails cart:user.getBooksCart()) {				
 					boolean notExist = cart.getBooksList().stream().noneMatch(books -> books.getBookId() == bookId);
 					if (!notExist) {
 					if(quantity <= book.getNoOfBooks()) {
 					cart.setBooksQuantity(quantity);				
 					return userRepository.save(user).getBooksCart();
 					}
 				} 
 			}
 			
 		return null;

 	}

}