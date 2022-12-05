package com.example.UserProductService.service;

import com.example.UserProductService.domain.Product;
import com.example.UserProductService.domain.User;
import com.example.UserProductService.exception.ProductNotFoundException;
import com.example.UserProductService.exception.UserAlreadyExistsException;
import com.example.UserProductService.exception.UserNotFoundException;
import com.example.UserProductService.rabbitMQ.CommonUser;
import com.example.UserProductService.rabbitMQ.Producer;
import com.example.UserProductService.rabbitMQ.UserDTO;
import com.example.UserProductService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Producer producer;
    @Override
    public User addUser(User user) throws UserAlreadyExistsException {
        if(userRepository.findById(user.getUserId()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    @Override
    public User addUser1(CommonUser commonUser) {
        UserDTO userDTO = new UserDTO(commonUser.getUserId(), commonUser.getPassword());
        producer.sendDTOToQueue(userDTO);
        User user = new User(commonUser.getUserId(), commonUser.getUserName(), commonUser.getAddress(), commonUser.getPassword(), new ArrayList<>());

        return userRepository.insert(user);
    }

    @Override
    public User addProductForUser(String userId, Product product) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userRepository.findByUserId(userId);
        if(user.getProductList() == null){
            user.setProductList(Arrays.asList(product));
        }else{
            List<Product> products = user.getProductList();
            products.add(product);
            user.setProductList(products);
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteProductForUser(String userId,int productId) throws ProductNotFoundException, UserNotFoundException {
        boolean productIdIsPresent = false;
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Product> products = user.getProductList();
        productIdIsPresent = products.removeIf(x->x.getProductId()==productId);
        if(!productIdIsPresent)
        {
            throw new ProductNotFoundException();
        }
        user.setProductList(products);
        return userRepository.save(user);

    }

    @Override
    public List<Product> getProductForUser(String userId) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        return userRepository.findById(userId).get().getProductList();

    }
}
