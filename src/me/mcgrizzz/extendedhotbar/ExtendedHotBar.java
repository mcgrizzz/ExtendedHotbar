package me.mcgrizzz.extendedhotbar;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;


public class ExtendedHotBar extends JavaPlugin implements Listener{
	
	boolean sneak = true;
	boolean command = true;
	ArrayList<UUID> eh = new ArrayList<UUID>();
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		this.getConfig().addDefault("Sneak_on_survival", true);
		sneak = this.getConfig().getBoolean("Sneak_on_survival");
		this.getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@Override
	public void onDisable(){
		eh.clear();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args){
		if(!cmd.getLabel().equalsIgnoreCase("extendedhotbar"))return true;
		if(sender instanceof Player && sender.hasPermission("extendedhotbar.use")){
			Player p = (Player)sender;
			boolean t = (eh.contains(p.getUniqueId())) ? eh.remove(p.getUniqueId()) : !eh.add(p.getUniqueId());
			p.sendMessage(ChatColor.GREEN + "ExtendedHotbar \n" + (t ? ChatColor.DARK_GREEN + " > Enabled <" : ChatColor.DARK_RED + " > Disabled < "));
			return true;
		}
		return true;
	}
	
	public void shiftInventory(Player p, int shift){
		ItemStack[] items = new ItemStack[36];
		for(int i = 0; i<36; i++){
			int slot = (i+9*shift + 36)%36;
			items[slot] = p.getInventory().getItem(i);
					
		}
		p.getInventory().setContents(items);
	
	}
	
	@EventHandler
	public void slotChange(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		if(!player.getGameMode().equals(GameMode.CREATIVE)){
			if(sneak){
				if(!player.isSneaking())return;
			}
		}

		if(!player.hasPermission("extendedhotbar.use") || eh.contains(player.getUniqueId()))return;
		if(event.getNewSlot() > 8)return;
		
		if((event.getPreviousSlot() == 0 || event.getPreviousSlot() == 1) && (event.getNewSlot() == 8 || event.getNewSlot() == 7)){
			shiftInventory(player, -1);
		}
		if((event.getPreviousSlot() == 8 || event.getPreviousSlot() == 7) && (event.getNewSlot() == 0 || event.getNewSlot() == 1)){
			shiftInventory(player, +1);
		}
	}

}
