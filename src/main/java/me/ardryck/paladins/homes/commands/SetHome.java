package me.ardryck.paladins.homes.commands;

import me.ardryck.paladins.homes.controller.UserController;
import me.ardryck.paladins.homes.HomesPlugin;
import me.ardryck.paladins.homes.enums.Type;
import me.ardryck.paladins.homes.model.Home;
import me.ardryck.paladins.homes.model.User;
import me.ardryck.paladins.homes.service.UserService;
import me.ardryck.paladins.homes.util.Locations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class SetHome implements CommandExecutor {

    private final HomesPlugin plugin;

    private final UserController userController;
    private final UserService userService;

    public SetHome(HomesPlugin plugin, UserController userController, UserService userService) {
        this.plugin = plugin;
        this.userController = userController;
        this.userService = userService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cComando apenas para jogadores!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("&6&lHOMES &8&l⇨ §eUtilize /sethome <nome>.");
            return true;
        }

        String home = args[0];

        if (home.length() > 10) {
            player.sendMessage("&6&lHOMES &8&l⇨ §eUtilize no máximo 10 caracteres!");
            return true;
        }

        User user = userController.getByName(player.getName()).orElse(new User(
                player.getName(),
                new HashMap<>()
        ));

        int amount = 0;

        for (String homes : plugin.getConfig().getStringList("Homes")) {

            String[] split = homes.split(":");

            String permission = split[0];
            int max = Integer.parseInt(split[1]);

            if (player.hasPermission(permission)) amount = max;
        }

        if (user.getHomes().size() == amount) {
            player.sendMessage("&6&lHOMES &8&l⇨ §eVocê só pode criar no máximo " + amount + " homes!");
            return true;
        }

        if (user.getHomes().containsKey(home)) {
            player.sendMessage("&6&lHOMES &8&l⇨ §eVocê já possui uma home com este nome!");
            return true;
        }

        String location = Locations.serialize(player.getLocation());

        user.getHomes().put(home, new Home(home, location, Type.PRIVATE));

        CompletableFuture.runAsync(() -> { if (!userController.getMap().containsKey(player.getName())) { userController.add(user); userService.insert(user); }});

        player.sendMessage("&6&lHOMES &8&l⇨ §eHome §f" + home + "§e setada com sucesso!");

        return true;
    }
}
