package com.firesoftitan.play.titanbox.portals.listeners;

import com.firesoftitan.play.titanbox.portals.TitanPortals;
import com.firesoftitan.play.titanbox.portals.managers.NetherPortalManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainListener  implements Listener {

    List< HashMap<Location, Material>> portals = new ArrayList< HashMap<Location, Material>>();

    public MainListener(){

    }
    public void registerEvents(){
        PluginManager pm = TitanPortals.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanPortals.instants);
    }

    public void onPortalCreateEvent(PortalCreateEvent event)
    {
        HashMap<Location, Material> oldBlocks = new HashMap<Location, Material>();
        HashMap<Location, Material> savedPortal = new HashMap<Location, Material>();
        if (event.getReason() == PortalCreateEvent.CreateReason.NETHER_PAIR) {
            // this removes the new nether portal because if you cancel the event you cancel the teleport
            for(BlockState blockState :event.getBlocks())
            {
                oldBlocks.put(blockState.getLocation(),blockState.getLocation().getBlock().getType());
                savedPortal.put(blockState.getLocation(), blockState.getType());
            }
            portals.add(savedPortal);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Location location: oldBlocks.keySet())
                    {
                        location.getBlock().setType(oldBlocks.get(location));
                    }
                }
            }.runTaskLater(TitanPortals.instants, 3);

        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            int myPoral = -1;
            int size = 10;
            for (int i = 0; i <portals.size(); i++)
            {
                for(Location location: portals.get(0).keySet())
                {
                    if (location.getWorld().getName().equals(event.getTo().getWorld().getName())) {
                        double distance = location.distance(event.getTo());
                        if (distance < size) {
                            size = (int) distance;
                            myPoral = i;
                        }
                    }
                }
            }
            HashMap<Location, Material> savedPortal =  null;
            if (myPoral > -1) savedPortal = portals.get(myPoral);

            Location location = NetherPortalManager.instants.getLinked(player, event.getFrom());
            if (location != null)
            {
                event.setTo(location);
            }
            else
            {
                if (event.getFrom().getWorld().getEnvironment() != World.Environment.NETHER) {
                    NetherPortalManager.instants.addPortalLink(event.getFrom(), event.getTo());
                    if (myPoral > -1) {
                        makeNetherPortal(savedPortal);
                    }

                }
                else
                {
                    String world = NetherPortalManager.instants.getPlayerLastWorld(player);
                    if (world == null || world.equals(player.getWorld().getName())) {
                        //shouldn't ever see this message
                        TitanPortals.sendMessagePlayer(TitanPortals.instants, player, ChatColor.RED + "Can't create portals in nether at this time, must be in (an) overworld.");
                        TitanPortals.sendMessagePlayer(TitanPortals.instants, player, ChatColor.RED + "Type " + ChatColor.WHITE + "/spawn" + ChatColor.RED + " or " + ChatColor.WHITE + "/hub " + ChatColor.RED + "to leave");
                        event.setCancelled(true);
                    }
                    else
                    {
                        Location lastWorld = NetherPortalManager.instants.getPlayerLastLocation(player);
                        NetherPortalManager.instants.addPortalLink(event.getFrom(), lastWorld.clone());
                        event.setTo(lastWorld.clone());
                        if (myPoral > -1) {
                            makeNetherPortal(savedPortal);

                        }

                    }
                }
            }
            if (myPoral > -1) {
                savedPortal.clear();
                portals.remove(myPoral);
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                NetherPortalManager.instants.updatePlayerWorldLocation(player);
            }
        }.runTaskLater(TitanPortals.instants, 3);
    }
    private void makeNetherPortal(HashMap<Location, Material> savedPortal) {
        HashMap<Location, Material> finalSavedPortal = (HashMap<Location, Material>) savedPortal.clone();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location locationB : finalSavedPortal.keySet()) {
                    locationB.getBlock().setType(finalSavedPortal.get(locationB), false);
                }
            }
        }.runTaskLater(TitanPortals.instants, 20);
    }
}
