/*
 * THE MAIN CLASS OF THE PLUGIN
 * 
 * The whole plugin is initialized here, and simple server event handlers (player command events,
 * player exit events, etc)
 * 
 * 
 */

package com.mcquizbowl.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public final class mainClass extends JavaPlugin implements Listener{
	
	public ArrayList<String> playersInRoom = new ArrayList<String>();
	
    @Override
    public void onEnable() {
        // TODO Insert logic to be performed when the plugin is enabled
    	getServer().getPluginManager().registerEvents(this, this);
    	getLogger().info("onEnable has been invoked!");
    	
    	//Custom filter to remove unnecessary messages
    	 customFilter filter = new customFilter();
    	 
    	this.getServer().getLogger().setFilter(filter);
    	
    	//Command handlers- sends command data to roomCmds.class
    	this.getCommand("joinroom").setExecutor(new roomCmds(this));
    	this.getCommand("leaveroom").setExecutor(new roomCmds(this));
    	this.getCommand("startquestion").setExecutor(new roomCmds(this));
    	this.getCommand("buzz").setExecutor(new roomCmds(this));
    	this.getCommand("unbuzz").setExecutor(new roomCmds(this));
    	this.getCommand("ans").setExecutor(new roomCmds(this));
    	
    	this.getCommand("setdifficulty").setExecutor(new roomCmds(this));
    	this.getCommand("setcategory").setExecutor(new roomCmds(this));
    	
    	
    	
    }
    
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
   
    //When a player leaves.
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
    	
    	Player p = e.getPlayer();
    	Bukkit.getLogger().info(p.getName() + " has quit!");
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players reset " + p.getName());

    	
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), p.getName() + "left!");
    	Bukkit.getServer().dispatchCommand(p, "leaveroom");
    }
    
}