package com.example.UserProductService.service;

import com.example.UserProductService.domain.Product;
import com.example.UserProductService.domain.User;
import com.example.UserProductService.exception.ProductNotFoundException;
import com.example.UserProductService.exception.UserAlreadyExistsException;
import com.example.UserProductService.exception.UserNotFoundException;
import com.example.UserProductService.rabbitMQ.CommonUser;

import java.util.List;

public interface UserService {
    User addUser(User user) throws UserAlreadyExistsException;
    User addUser1(CommonUser commonUser);
    User addProductForUser(String userId, Product product) throws UserNotFoundException;
    User deleteProductForUser(String userId,int productId) throws ProductNotFoundException, UserNotFoundException;
    List<Product> getProductForUser(String userId) throws UserNotFoundException;
}