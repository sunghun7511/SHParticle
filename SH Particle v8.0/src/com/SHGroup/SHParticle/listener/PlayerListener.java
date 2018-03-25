package com.SHGroup.SHParticle.listener;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.SHGroup.SHParticle.ParticleMain;

public class PlayerListener implements Listener {
	private ParticleMain main;

	public PlayerListener(ParticleMain main) {
		this.main = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		main.Load(e.getPlayer().getName());

		// TODO: Check Player Data

		main.Save(e.getPlayer().getName());
	}

	@EventHandler
	public void quit(PlayerQuitEvent e) {
		main.Save(e.getPlayer().getName());
	}

	@EventHandler
	public void kick(PlayerKickEvent e) {
		main.Save(e.getPlayer().getName());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onGo(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL) {
			return;
		}
		Player p = e.getPlayer();
		if (p.getItemInHand() != null) {
			try {
				if (p.getItemInHand().getType() == Material.ENCHANTED_BOOK && p.getItemInHand().getItemMeta()
						.getDisplayName().equals(pr + ChatColor.GREEN + "§l파티클 사용권")) {
					String n = p.getItemInHand().getItemMeta().getLore().get(0);
					if (effect.get(p.getName()) == null) {
						effect.put(p.getName(), new ArrayList<String>());
					}
					main.addEffect(p.getName(), ChatColor.stripColor(n));
					p.setItemInHand(new ItemStack(Material.AIR));
				}
			} catch (Exception ex) {
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (ParticleMain.ismove.contains(e.getPlayer().getName())) {
			Runa.showParticle(e.getPlayer());
		}
	}
}
