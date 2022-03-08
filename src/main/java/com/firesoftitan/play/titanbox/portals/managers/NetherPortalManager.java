package com.firesoftitan.play.titanbox.portals.managers;

import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import com.firesoftitan.play.titanbox.portals.TitanPortals;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class NetherPortalManager {
    private SaveManager configFile;
    public static NetherPortalManager instants;
    public NetherPortalManager() {
        configFile  = new SaveManager(TitanPortals.instants.getName(), "netherportals");
        instants = this;
    }
    public void save()
    {
        configFile.save();
    }
    public void updatePlayerWorldLocation(Player player)
    {
        if (player == null) return;
        if (player.getName() == null) return;
        String lastWorld = configFile.getString("player." + player.getName() + ".world");
        Location lastLocation = configFile.getLocation("player." + player.getName() + ".location");
        if (lastWorld == null || !(lastWorld.equals(player.getWorld().getName()))) {
            if (lastLocation == null || lastWorld == null)
            {
                configFile.set("player." + player.getName() + ".lastworld", player.getWorld().getName());
                configFile.set("player." + player.getName() + ".lastlocation", player.getLocation().clone());
            }
            else {
                configFile.set("player." + player.getName() + ".lastlocation", lastLocation.clone());
                configFile.set("player." + player.getName() + ".lastworld", lastWorld);
            }
            configFile.set("player." + player.getName() + ".world", player.getWorld().getName());
            configFile.set("player." + player.getName() + ".location", player.getLocation().clone());
        }
    }
    public Location getPlayerLastLocation(Player player)
    {
        return configFile.getLocation("player." + player.getName() + ".lastlocation");
    }
    public String getPlayerLastWorld(Player player)
    {
        String lastWorld = configFile.getString("player." + player.getName() + ".lastworld");
        return lastWorld;
    }
    public void addPortalLink(Location from, Location to)
    {
        int countFrom = configFile.getKeys("portals." + from.getWorld().getName()).size();
        configFile.set("portals." + from.getWorld().getName() + "." + countFrom + ".from", from.clone());
        configFile.set("portals." + from.getWorld().getName() + "." + countFrom + ".to", to.clone());
        configFile.set("portals." + from.getWorld().getName() + "." + countFrom + ".made", System.currentTimeMillis());

        int countTo = configFile.getKeys("portals." + to.getWorld().getName()).size();
        configFile.set("portals." + to.getWorld().getName() + "." + countTo + ".from", to.clone());
        configFile.set("portals." + to.getWorld().getName() + "." + countTo + ".to", from.clone());
        configFile.set("portals." + to.getWorld().getName() + "." + countTo + ".made", System.currentTimeMillis());
    }
    public Location getLinked(Player player, Location from)
    {
        String fromName = from.getWorld().getName();
        int countFrom = configFile.getKeys("portals." + fromName).size();
        List<Location> match = new ArrayList<Location>();
        for(int i = 0; i<countFrom; i++)
        {
            if (configFile.contains("portals." + fromName + "." + i + ".from") && configFile.contains("portals." + fromName + "." + i + ".to")) {
                Location location = configFile.getLocation("portals." + fromName + "." + i + ".from");
                double distance = location.distance(from);
                if (distance < 10) {
                    match.add(configFile.getLocation("portals." + fromName + "." + i + ".to"));
                }
            }
        }

        if (match.size() == 1) return match.get(0);
        String playerLastWorld = getPlayerLastWorld(player);
        if (playerLastWorld != null) {
            for (Location location : match) {
                if (location.getWorld().getName().equals(playerLastWorld)) return location;
            }
        }
        if (match.size() > 0) return match.get(0);
        return null;
    }
}
