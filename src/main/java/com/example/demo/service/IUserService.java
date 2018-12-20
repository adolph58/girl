package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    void saveUser(User book);
   
    User findOne(long id);

    void delete(long id);

    List<User> findByName(String name);

    List<User> findByAddress(String address);

}
