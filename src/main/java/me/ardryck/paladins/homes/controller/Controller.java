package me.ardryck.paladins.homes.controller;

import me.ardryck.paladins.homes.model.User;

import java.util.Optional;

public interface Controller {

    User get(String name);
    Optional<User> getByName(String name);

    void add(User user);

    void remove(User user);
}
