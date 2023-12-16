package me.ardryck.paladins.homes.service;

import me.ardryck.paladins.homes.model.User;

public interface Service {

    void load();

    void insert(User user);

    void update(User user);

    void delete(User user);
}
