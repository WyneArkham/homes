package me.ardryck.paladins.homes;

import me.ardryck.paladins.homes.commands.Home;
import me.ardryck.paladins.homes.commands.SetHome;
import me.ardryck.paladins.homes.controller.UserController;
import me.ardryck.paladins.homes.database.Database;
import me.ardryck.paladins.homes.lib.listeners.InventoryListener;
import me.ardryck.paladins.homes.database.MySQL;
import me.ardryck.paladins.homes.database.SQLite;
import me.ardryck.paladins.homes.listener.BaseListener;
import me.ardryck.paladins.homes.menu.HomesMenu;
import me.ardryck.paladins.homes.service.UserService;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class HomesPlugin extends JavaPlugin {

    private Database database;

    private UserController userController;
    private UserService userService;

    public HomesMenu menu;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        startConnection();

        userController = new UserController();
        userService = new UserService(this, userController, database);

        menu = new HomesMenu(this, userController, userService);

        getServer().getPluginManager().registerEvents(new BaseListener(), this);

        new InventoryListener(this);

        getCommand("home").setExecutor(new Home(this, userController));
        getCommand("sethome").setExecutor(new SetHome(this, userController, userService));

    }

    @Override
    public void onDisable() {
        userService.save();
    }

    public void startConnection() {

        ConfigurationSection cs = getConfig().getConfigurationSection("Connection");

        if (cs.getString("Type").equalsIgnoreCase("MySQL"))
            database = new MySQL(this, cs.getString("Host"), cs.getInt("Port"), cs.getString("Database"), cs.getString("Username"), cs.getString("Password"));
        else
            database = new SQLite(this, "database.db");

        database.openConnection();
        database.sendCommand("CREATE TABLE IF NOT EXISTS homes (name VARCHAR(16), value LONGTEXT);");

    }
}
