package com.firesoftitan.play.titanbox.portals;

import com.firesoftitan.play.titanbox.portals.listeners.MainListener;
import com.firesoftitan.play.titanbox.portals.managers.ConfigManager;
import com.firesoftitan.play.titanbox.portals.managers.NetherPortalManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class TitanPortals extends JavaPlugin {
    public static ConfigManager configManager;
    public static TitanPortals instants;
    public static MainListener mainListener;
    public void onEnable() {
        instants = this;
        mainListener = new MainListener();
        mainListener.registerEvents();
        configManager = new ConfigManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                new NetherPortalManager();
            }
        }.runTaskLater(this, 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                NetherPortalManager.instants.save();
            }
        }.runTaskTimer(this, 12000, 12000);
    }
    public void onDisable()
    {
        this.saveALL();
    }
    public void saveALL()
    {

    }
    public static void sendMessageSystem(JavaPlugin plugin, String message)
    {
        sendMessageSystem(plugin, message, Level.INFO);
    }
    public static void sendMessageSystem(JavaPlugin plugin, String message, Level level)
    {
        String subName = plugin.getName().replace("TitanBox", "");
        plugin.getLogger().log(level,  ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
    public static void sendMessagePlayer(JavaPlugin plugin, Player player, List<String> messages)
    {
        String subName = plugin.getName().replace("TitanBox", "");
        String messageHeaderFooter = ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------" + ChatColor.RESET + ChatColor.GREEN + "[" + ChatColor.BLUE + "TitanBox" + ChatColor.GREEN + "](" + ChatColor.AQUA + subName + ChatColor.GREEN + ")" + ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------";
        if (player == null || !player.isOnline())
        {
            TitanPortals.sendMessageSystem(plugin, messageHeaderFooter);
            for(String s: messages) {
                TitanPortals.sendMessageSystem(plugin,ChatColor.translateAlternateColorCodes('&', s));
            }
            TitanPortals.sendMessageSystem(plugin,ChatColor.GRAY + "" +ChatColor.BOLD +  ChatColor.STRIKETHROUGH + "-------------" + "-------------");
            return;
        }

        player.sendMessage(messageHeaderFooter);
        for(String s: messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
        player.sendMessage(ChatColor.GRAY + "" +ChatColor.BOLD +  ChatColor.STRIKETHROUGH + "-------------" + "-------------");
    }
    public static void sendMessagePlayer(JavaPlugin plugin, Player player, String... messages)
    {
        List<String> mes = new ArrayList<>(Arrays.asList(messages));
        sendMessagePlayer(plugin, player, mes);
    }
    public static void sendMessagePlayer(JavaPlugin plugin, Player player, String message)
    {
        if (player == null || !player.isOnline())
        {
            TitanPortals.sendMessageSystem(plugin, message);
            return;
        }
        String subName = plugin.getName().replace("TitanBox", "");
        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.BLUE + "TitanBox" + ChatColor.GREEN + "]("+ ChatColor.AQUA + subName + ChatColor.GREEN + "): " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
}
