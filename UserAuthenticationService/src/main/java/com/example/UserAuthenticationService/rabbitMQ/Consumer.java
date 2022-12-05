package com.example.UserAuthenticationService.rabbitMQ;


import com.example.UserAuthenticationService.domain.User;
import com.example.UserAuthenticationService.exception.UserAlreadyExistsException;
import com.example.UserAuthenticationService.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @Autowired
    private UserService userService;
    @RabbitListener(queues = "user_queue")
    public void getDTOToQueueAndAddToDb(UserDTO userDTO) throws UserAlreadyExistsException {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setPassword(userDTO.getPassword());

        User user1 = userService.addUser(user);
        System.out.println("result = "+user1);
    }
}