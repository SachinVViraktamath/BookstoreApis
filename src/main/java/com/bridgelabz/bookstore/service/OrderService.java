package com.bridgelabz.bookstore.service;



import java.util.List;

import com.bridgelabz.bookstore.entity.Order;
import com.bridgelabz.bookstore.exception.BookException;
import com.bridgelabz.bookstore.exception.UserException;

public interface OrderService {
	List<Order> orderTheBook(String token,Long cartId,String adressType) throws UserException ,BookException;

}
