package me.ardryck.paladins.homes.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ardryck.paladins.homes.HomesPlugin;
import me.ardryck.paladins.homes.controller.UserController;
import me.ardryck.paladins.homes.database.Database;
import me.ardryck.paladins.homes.model.User;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService implements Service {

    private final UserController userController;
    private final Database database;

    private final Gson gson;

    public UserService(HomesPlugin plugin, UserController userController, Database database) {
        this.userController = userController;
        this.database = database;

        this.gson = new GsonBuilder().setPrettyPrinting().create();

        load();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 20 * 60 * 5, 20 * 60 * 5);

    }

    @Override
    public void load() {

        ResultSet resultSet = database.sendQuery("SELECT * FROM homes;");

        try {

            while (resultSet.next()) {

                String value = resultSet.getString("value");

                userController.add(this.gson.fromJson(value, User.class));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void insert(User user) {

        database.sendCommand("INSERT INTO homes (name, value) VALUES ('" + user.getName() + "', '" + this.gson.toJson(user) + "')");

    }

    @Override
    public synchronized void update(User user) {

        database.sendCommand("UPDATE homes SET value = '" + this.gson.toJson(user) + "' WHERE name = '" + user.getName() + "'");

    }

    @Override
    public synchronized void delete(User user) {

        database.sendCommand("DELETE FROM homes WHERE name = '" + user.getName() + "'");

    }

    public void save() {
        for (User value : userController.getMap().values()) {
            update(value);
        }
    }
}
