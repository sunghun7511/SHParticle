package com.SHGroup.SHParticle;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import com.SHGroup.SHParticle.API.ParticleEffect;
import com.SHGroup.SHParticle.API.ParticleEffect.NoteColor;
import com.SHGroup.SHParticle.API.ParticleEffect.OrdinaryColor;
import com.SHGroup.SHParticle.API.ParticleEffect.ParticleColor;
import com.SHGroup.SHParticle.API.ParticleEffect.ParticleData;
import com.SHGroup.SHParticle.API.ParticleEffect.ParticleProperty;
import com.SHGroup.SHParticle.API.ReflectionUtils.PackageType;
import com.SHGroup.SHParticle.frames.FrameManager;

@SuppressWarnings({ "deprecation", "unchecked" })
public class ParticleMain extends JavaPlugin implements Listener {
	public HashMap<String, Double> temp = new HashMap<String, Double>();
	public HashMap<String, Integer> temp2 = new HashMap<String, Integer>();
	public static ArrayList<String> notsee = new ArrayList<String>();
	public static ArrayList<String> mynotsee = new ArrayList<String>();
	public static ArrayList<String> ismove = new ArrayList<String>();
	public int range = 16;

	
	
	
	private boolean guitwoopen = false;
	private boolean over7 = false;
	
	private FrameManager frames;
	private SHUti util;

	String pr = "§7§l[ §c§lSH Particle §7§l] §f§l";

	@Override
	public void onEnable() {
		int version = Integer.parseInt(PackageType.getServerVersion()
				.split("_")[1]);
		over7 = version >= 7;
		try {
			new File("plugins/SH Particle/").mkdirs();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			File f = new File("plugins/SH Particle/servername.txt");
			if (!f.exists()) {
				f.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.append("&7&l[ &c&lSH Particle &7&l] &f&l");
				bw.flush();
				bw.close();
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			this.pr = br.readLine().replace("&", "§");
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.getServer().getPluginManager().registerEvents(this, this);
		
		if (!(this.getServer().getVersion().contains("1.5") || this.getServer()
				.getVersion().contains("1.4"))) {
			guitwoopen = true;
		}
		util = new SHUti(pr);
		try {
			File trans = new File("plugins/SH Particle/amount.dat");
			if(trans.exists()) {
				transOldFiles();
			}
			for (String n : util.fileLoad("plugins/SH Particle/amount.dat")
					.split("\n")) {
				amount.put(n.split(":")[0], Integer.parseInt(n.split(":")[1]));
			}
		} catch (Exception ex) {
		}
		try {
			for (String n : util.fileLoad("plugins/SH Particle/data-1.dat")
					.split("\n")) {
				noweffect.put(n.split(":")[0], n.split(":")[1]);
			}
		} catch (Exception ex) {
		}
		try {
			for (String n : util.fileLoad("plugins/SH Particle/data-2.dat")
					.split("\n")) {
				effecttype
						.put(n.split(":")[0], Byte.parseByte(n.split(":")[1]));
			}
		} catch (Exception ex) {
		}
		try {
			for (String n : util.fileLoad("plugins/SH Particle/data-3.dat")
					.split("\n")) {
				effectup.put(n.split(":")[0], Byte.parseByte(n.split(":")[1]));
			}
		} catch (Exception ex) {
		}
		try {
			for (String n : util.fileLoad("plugins/SH Particle/frames.dat")
					.split("\n")) {
				frames.add(new Frame(n));
			}
		} catch (Exception ex) {
		}
		try {
			range = Integer.parseInt(util
					.fileLoad("plugins/SH Particle/range.dat"));
		} catch (Exception ex) {
		}
		try {
			String n = util.fileLoad("plugins/SH Particle/userdata/spread.dat");
			if (!(n == null || n.equals(""))) {
				for (String s : n.split("\n")) {
					speed.put(s.split("\\|")[0],
							Integer.parseInt(s.split("\\|")[1]));
				}
			}
		} catch (Exception ex) {
		}
		try {
			String n = util.fileLoad("plugins/SH Particle/userdata/notsee.dat");
			if (!(n == null || n.equals(""))) {
				for (String s : n.split("\n")) {
					notsee.add(s);
				}
			}
		} catch (Exception ex) {
		}
		try {
			String n = util
					.fileLoad("plugins/SH Particle/userdata/mynotsee.dat");
			if (!(n == null || n.equals(""))) {
				for (String s : n.split("\n")) {
					mynotsee.add(s);
				}
			}
		} catch (Exception ex) {
		}
		try {
			String n = util.fileLoad("plugins/SH Particle/userdata/ismove.dat");
			if (!(n == null || n.equals(""))) {
				for (String s : n.split("\n")) {
					ismove.add(s);
				}
			}
		} catch (Exception ex) {
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runa(this),
				1L, 1L);
		System.out.println("[SH Particle] 플러그인 활성화 완료");
	}

	@Override
	public void onDisable() {
		for (Player p : getOnline())
			Save(p.getName());
		Bukkit.getScheduler().cancelTasks(this);
		util.fileSave("plugins/SH Particle/", "range.dat",
				Integer.toString(range));
		{
			String n = "";
			for (Map.Entry<String, Integer> ent : amount.entrySet()) {
				n += (n.equals("") ? "" : "\n") + ent.getKey() + ":"
						+ ent.getValue();
			}
			util.fileSave("plugins/SH Particle/", "amount.dat", n);
		}
		{
			String n = "";
			for (Entry<String, String> ent : noweffect.entrySet()) {
				n += (n.equals("") ? "" : "\n") + ent.getKey() + ":"
						+ ent.getValue();
			}
			util.fileSave("plugins/SH Particle/", "data-1.dat", n);
		}
		{
			String n = "";
			for (Entry<String, Byte> ent : effecttype.entrySet()) {
				n += (n.equals("") ? "" : "\n") + ent.getKey() + ":"
						+ ent.getValue();
			}
			util.fileSave("plugins/SH Particle/", "data-2.dat", n);
		}
		{
			String n = "";
			for (Entry<String, Byte> ent : effectup.entrySet()) {
				n += (n.equals("") ? "" : "\n") + ent.getKey() + ":"
						+ ent.getValue();
			}
			util.fileSave("plugins/SH Particle/", "data-3.dat", n);
		}
		{
			String n = "";
			for (Frame f : frames) {
				n += (n.equals("") ? "" : "\n") + f.toString();
			}
			util.fileSave("plugins/SH Particle/", "frames.dat", n);
		}
		{
			String n = "";
			for (Map.Entry<String, Integer> ent : speed.entrySet()) {
				n += (n.equals("") ? "" : "\n") + ent.getKey() + "|"
						+ ent.getValue();
			}
			util.fileSave("plugins/SH Particle/userdata/", "spread.dat", n);
		}
		{
			String n = "";
			for (String s : notsee) {
				n += (n.equals("") ? "" : "\n") + s;
			}
			util.fileSave("plugins/SH Particle/userdata/", "notsee.dat", n);
		}
		{
			String n = "";
			for (String s : mynotsee) {
				n += (n.equals("") ? "" : "\n") + s;
			}
			util.fileSave("plugins/SH Particle/userdata/", "mynotsee.dat", n);
		}
		{
			String n = "";
			for (String s : ismove) {
				n += (n.equals("") ? "" : "\n") + s;
			}
			util.fileSave("plugins/SH Particle/userdata/", "ismove.dat", n);
		}
	}
	
	public FrameManager getFrameManager() {
		return frames;
	}
	
	public boolean isOver7() {
		return over7;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Load(e.getPlayer().getName());
		if (effect.get(e.getPlayer().getName()) == null) {
			effect.put(e.getPlayer().getName(), new ArrayList<String>());
		}
		if (noweffect.get(e.getPlayer().getName()) == null) {
			noweffect.put(e.getPlayer().getName(), "");
		}
		Save(e.getPlayer().getName());
	}

	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Save(e.getPlayer().getName());
	}

	@EventHandler
	public void kick(PlayerKickEvent e) {
		Save(e.getPlayer().getName());
	}

	@EventHandler
	public void onGo(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL) {
			return;
		}
		Player p = e.getPlayer();
		if (p.getItemInHand() != null) {
			try {
				if (p.getItemInHand().getType() == Material.ENCHANTED_BOOK
						&& p.getItemInHand().getItemMeta().getDisplayName()
								.equals(pr + ChatColor.GREEN + "§l파티클 사용권")) {
					String n = p.getItemInHand().getItemMeta().getLore().get(0);
					if (effect.get(p.getName()) == null) {
						effect.put(p.getName(), new ArrayList<String>());
					}
					addEffect(p.getName(), ChatColor.stripColor(n));
					p.setItemInHand(new ItemStack(Material.AIR));
				}
			} catch (Exception ex) {
			}
		}
	}

	private void addEffect(String name, String n) {
		ArrayList<String> list = effect.get(name);
		if (over7) {
			if (fromName(n.toUpperCase()) == null) {
				util.msg(Bukkit.getPlayerExact(name), "파티클 이름이 올바르지 않습니다.");
				return;
			}
		} else {
			if (ParticleEffect.fromName(n) == null) {
				util.msg(Bukkit.getPlayerExact(name), "파티클 이름이 올바르지 않습니다.");
				return;
			}
		}
		util.msg(Bukkit.getPlayerExact(name), n + ChatColor.WHITE
				+ "§l 효과가 추가되었습니다.(/SHP 명령어를 사용하여 변경)");
		if (!list.contains(n)) {
			list.add(n);
		}
		effect.put(name, list);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (ParticleMain.ismove.contains(e.getPlayer().getName())) {
			Runa.showParticle(e.getPlayer());
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String name = e.getInventory().getName();
		if (name.equals("§0§l파티클 선택")) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			try {
				ItemStack currentItem = e.getCurrentItem();
				if (currentItem == null) {
					return;
				}
				if (currentItem.getType() == Material.AIR) {
					return;
				}
				if (!currentItem.hasItemMeta()) {
					return;
				}
				if (e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
					if (!over7) {
						if (ParticleEffect.fromName(ChatColor.stripColor(e
								.getCurrentItem().getItemMeta()
								.getDisplayName())) == null) {
							return;
						}
						ParticleEffect effect = ParticleEffect
								.fromName(ChatColor.stripColor(e
										.getCurrentItem().getItemMeta()
										.getDisplayName()));
						if (!effect.isSupported()) {
							p.closeInventory();
							util.msg(p, "\"" + effect.getName()
									+ "\" 파티클은 현재 서버 버젼에서 사용할 수 없습니다!");
							return;
						}
						if (!over7
								&& (effect == ParticleEffect.BLOCK_CRACK
										|| effect == ParticleEffect.BLOCK_DUST || effect == ParticleEffect.ITEM_CRACK)) {
							util.msg(p, "\"" + effect.getName()
									+ "\" 파티클은 현재 사용할 수 없습니다!");
							return;
						}
						setEffect(p.getName(), effect.getName());
						p.closeInventory();
						if (!guitwoopen) {
							util.msg(p, "\"" + effect.getName()
									+ "\" 파티클이 설정되었습니다!");
							return;
						}
					} else {
						ParticleEffect effect = fromName(ChatColor
								.stripColor(e.getCurrentItem().getItemMeta()
										.getDisplayName()));
						/*if (effect.getMinVersion().newerThan(
								Version.getVersion())) {
							p.closeInventory();
							util.msg(p, "\"" + effect.getName()
									+ "\" 파티클은 현재 서버 버젼에서 사용할 수 없습니다!");
							return;
						}*/
						setEffect(p.getName(), effect.getName());
						p.closeInventory();
						if (!guitwoopen) {
							util.msg(p, "\"" + effect.getName()
									+ "\" 파티클이 설정되었습니다!");
							return;
						}
					}
					Inventory inv = Bukkit.createInventory(null, 9,
							"§0§l파티클 위치 선택");
					ItemStack i = new ItemStack(Material.PISTON_MOVING_PIECE);
					ItemStack up = util.createItem(Material.SKULL_ITEM, 1,
							ChatColor.YELLOW + "§l머리 위");
					ItemStack down = util.createItem(
							Material.DIAMOND_CHESTPLATE, 1, ChatColor.GREEN
									+ "§l몸 주위");
					ItemStack ddown = util.createItem(Material.LEATHER_BOOTS,
							1, ChatColor.GREEN + "§l발 주위");
					inv.setContents(new ItemStack[] { i, i, up, i, down, i,
							ddown, i, i });
					p.openInventory(inv);
				} else if (e.getCurrentItem().getType() == Material.MAGMA_CREAM
						&& e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(ChatColor.RED + "§l제거")) {
					noweffect.put(p.getName(), "");
					util.msg(p, "파티클이 제거되었습니다.");
					p.closeInventory();
				}
			} catch (Exception ex) {
			}
		} else if (name.equals("§0§l파티클 위치 선택")) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			try {
				if (e.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE
						&& e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(ChatColor.GREEN + "§l몸 주위")) {
					effectup.put(p.getName(), (byte) 1);
				} else if (e.getCurrentItem().getType() == Material.SKULL_ITEM
						&& e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(ChatColor.YELLOW + "§l머리 위")) {
					effectup.put(p.getName(), (byte) 0);
				} else if (e.getCurrentItem().getType() == Material.LEATHER_BOOTS
						&& e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(ChatColor.GREEN + "§l발 주위")) {
					effectup.put(p.getName(), (byte) 2);
				} else {
					effectup.put(p.getName(), (byte) 0);
					return;
				}
				util.msg(p, "\"" + noweffect.get(p.getName())
						+ "\" 파티클이 설정되었습니다!");
				p.closeInventory();
				return;
			} catch (Exception ex) {
			}
		} else if (name.equals("§0§l파티클 설정")) {
			e.setCancelled(true);
			try {
				if (e.getCurrentItem() == null) {
					return;
				}
				if (e.getCurrentItem().getType() == Material.AIR) {
					return;
				}
				Material type = e.getCurrentItem().getType();
				String display = e.getCurrentItem().getItemMeta()
						.getDisplayName();
				Player p = (Player) e.getWhoClicked();
				byte loc = effectup.get(p.getName());
				byte shape = effecttype.get(p.getName());
				if (type == Material.IRON_HELMET && display.equals("§f§l머리 위")) {
					if (loc == 0) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					effectup.put(p.getName(), (byte) 0);
					util.msg(p, "이제 파티클은 당신의 머리 위에서 나옵니다.");
				} else if (type == Material.IRON_CHESTPLATE
						&& display.equals("§f§l몸통 (원)")) {
					if (loc == 1 && shape == 0) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 몸 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 1);
					effecttype.put(p.getName(), (byte) 0);
				} else if (type == Material.GHAST_TEAR
						&& display.equals("§f§l날개 1")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Wing1")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Wing1)");
						return;
					}
					if (loc == 1 && shape == 3) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 날개 1 모양입니다.");
					effectup.put(p.getName(), (byte) 1);
					effecttype.put(p.getName(), (byte) 3);
				} else if (type == Material.GHAST_TEAR
						&& display.equals("§f§l날개 2")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Wing2")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Wing2)");
						return;
					}
					if (loc == 2 && shape == 3) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 날개 2 모양입니다.");
					effectup.put(p.getName(), (byte) 2);
					effecttype.put(p.getName(), (byte) 3);
				} else if (type == Material.GOLD_CHESTPLATE
						&& display.equals("§f§l몸통 (별)")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Star")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Star)");
						return;
					}
					if (loc == 1 && shape == 1) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 몸 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 1);
					effecttype.put(p.getName(), (byte) 1);
				} else if (type == Material.DIAMOND_CHESTPLATE
						&& display.equals("§f§l몸통 (삼각형)")) {
					if (!p.isOp()
							&& !p.hasPermission("SHParticle.Shape.Triangle")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Triangle)");
						return;
					}
					if (loc == 1 && shape == 2) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 몸 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 1);
					effecttype.put(p.getName(), (byte) 2);
				} else if (type == Material.IRON_BOOTS
						&& display.equals("§f§l발 (원)")) {
					if (loc == 2 && shape == 0) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 발 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 2);
					effecttype.put(p.getName(), (byte) 0);
				} else if (type == Material.GOLD_BOOTS
						&& display.equals("§f§l발 (별)")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Star")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Star)");
						return;
					}
					if (loc == 2 && shape == 1) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 발 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 2);
					effecttype.put(p.getName(), (byte) 1);
				} else if (type == Material.DIAMOND_BOOTS
						&& display.equals("§f§l발 (삼각형)")) {
					if (!p.isOp()
							&& !p.hasPermission("SHParticle.Shape.Triangle")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Triangle)");
						return;
					}
					if (loc == 2 && shape == 2) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 발 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 2);
					effecttype.put(p.getName(), (byte) 2);
				} else if (display.equals("§b§l파티클 §c§l차단 §f§l설정")) {
					if (ParticleMain.notsee.contains(p.getName())) {
						util.msg(p, "이제 모든 파티클을 볼 수 있습니다.");
						ParticleMain.notsee.remove(p.getName());
					} else {
						util.msg(p, "이제 모든 파티클을 보지 않습니다.");
						ParticleMain.notsee.add(p.getName());
					}
				} else if (display.equals("§f§l자신의 §b§l파티클 §c§l차단 §f§l설정")) {
					if (ParticleMain.mynotsee.contains(p.getName())) {
						util.msg(p, "이제 자신의 파티클을 볼 수 있습니다.");
						ParticleMain.mynotsee.remove(p.getName());
					} else {
						util.msg(p, "이제 자신의 파티클을 보지 않습니다.");
						ParticleMain.mynotsee.add(p.getName());
					}
				} else if (display.equals("§b§l파티클§f§l이 나오는 §a§l시점 §f§l설정")) {
					if (ParticleMain.ismove.contains(p.getName())) {
						util.msg(p, "이제 주기적으로 파티클이 나오게 됩니다.");
						ParticleMain.ismove.remove(p.getName());
					} else {
						util.msg(p, "이제 움직일때 파티클이 나오게 됩니다.");
						ParticleMain.ismove.add(p.getName());
					}
				}
				updateInventory(p);
			} catch (Exception ex) {
			}
		}
	}

	private void setEffect(String name, String n) {
		if (!effectup.containsKey(name)) {
			effectup.put(name, (byte) 0);
		}
		noweffect.put(name, n);
	}

	public void Load(String n) {
		File f = new File("plugins/SH Particle/data/" + n + ".dat");
		try {
			if (!f.exists()) {
				new File("plugins/SH Particle/data/").mkdirs();
				f.createNewFile();
				ArrayList<String> list = new ArrayList<String>();
				list.add("");
				effect.put(n, list);
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			ArrayList<String> list = new ArrayList<String>();
			while ((s = br.readLine()) != null) {
				try {
					list.add(ParticleEffect.fromId(Integer.parseInt(s)).getName());
				} catch (Exception e) {
					list.add(s);
				}
			}
			effect.put(n, list);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Save(String n) {
		File f = new File("plugins/SH Particle/data/" + n + ".dat");
		File folder = new File("plugins/SH Particle/data/");
		try {
			if (!f.exists()) {
				folder.mkdirs();
				f.createNewFile();
			}
			BufferedWriter BW = new BufferedWriter(new FileWriter(f));
			String to = "";
			if (effect.get(n) != null) {
				for (String b : effect.get(n)) {
					to += (to.equals("") ? "" : "\n") + b;
				}
			}
			BW.append(to);
			BW.flush();
			BW.close();
		} catch (Exception Exception) {
			System.out.println(Exception);
		}
	}

	public void updateInventory(Player p) {
		if (p.getOpenInventory().getTopInventory().getTitle()
				.equals("§0§l파티클 설정")) {
			Inventory i = p.getOpenInventory().getTopInventory();
			boolean isup = Integer.parseInt(PackageType.getServerVersion()
					.split("_")[1]) > 6;
			if (!effectup.containsKey(p.getName())) {
				effectup.put(p.getName(), (byte) 0);
			}
			if (!effecttype.containsKey(p.getName())) {
				effecttype.put(p.getName(), (byte) 0);
			}
			byte loc = effectup.get(p.getName());
			byte shape = effecttype.get(p.getName());
			ItemStack nullitem = util.createItem(isup ? 160 : 102, 1, "§c§l§r",
					util.createArray("§7Dev by SHGroup(sunghun7511)"));
			ItemStack head = util.createItem(Material.IRON_HELMET, 1,
					"§f§l머리 위", util.createArray("§c§l머리 위로 파티클이 표시됩니다.",
							loc == 0 ? "§a§l( 선택됨 )" : ""));
			;
			ItemStack body_1 = util.createItem(Material.IRON_CHESTPLATE, 1,
					"§f§l몸통 (원)", util.createArray("§c§l몸통 중심으로 파티클이",
							"§c§l원을 그리며 한바퀴를 돕니다.",
							loc == 1 && shape == 0 ? "§a§l( 선택됨 )" : ""));
			;
			ItemStack body_2 = util.createItem(Material.GOLD_CHESTPLATE, 1,
					"§f§l몸통 (별)", util.createArray("§c§l몸통 중심으로 파티클이",
							"§c§l별을 지속적으로 그립니다.",
							loc == 1 && shape == 1 ? "§a§l( 선택됨 )" : ""));
			ItemStack body_3 = util.createItem(Material.DIAMOND_CHESTPLATE, 1,
					"§f§l몸통 (삼각형)", util.createArray("", "§c§l몸통 중심으로 파티클이",
							"§c§l삼각형을 지속적으로 그립니다.",
							loc == 1 && shape == 2 ? "§a§l( 선택됨 )" : ""));
			ItemStack wing_1 = util.createItem(Material.GHAST_TEAR, 1,
					"§f§l날개 1", util.createArray("§c§l몸 뒤로 날개가 형성됩니다.",
							"§c§l1과 2는 모양이 다릅니다.",
							loc == 1 && shape == 3 ? "§a§l( 선택됨 )" : ""));
			;
			ItemStack wing_2 = util.createItem(Material.GHAST_TEAR, 1,
					"§f§l날개 2", util.createArray("§c§l몸 뒤로 날개가 형성됩니다.",
							"§c§l1과 2는 모양이 다릅니다.",
							loc == 2 && shape == 3 ? "§a§l( 선택됨 )" : ""));
			;
			ItemStack foot_1 = util.createItem(Material.IRON_BOOTS, 1,
					"§f§l발 (원)", util.createArray("§c§l발을 중심으로 파티클이",
							"§c§l원을 그리며 한바퀴를 돕니다.",
							loc == 2 && shape == 0 ? "§a§l( 선택됨 )" : ""));
			;
			// SHParticle.Shape.Wing2
			ItemStack foot_2 = util.createItem(Material.GOLD_BOOTS, 1,
					"§f§l발 (별)", util.createArray("§c§l발을 중심으로 파티클이",
							"§c§l별을 지속적으로 그립니다.",
							loc == 2 && shape == 1 ? "§a§l( 선택됨 )" : ""));
			ItemStack foot_3 = util.createItem(Material.DIAMOND_BOOTS, 1,
					"§f§l발 (삼각형)", util.createArray("", "§c§l발을 중심으로 파티클이",
							"§c§l삼각형을 지속적으로 그립니다.",
							loc == 2 && shape == 2 ? "§a§l( 선택됨 )" : ""));
			boolean notsee = ParticleMain.notsee.contains(p.getName());
			ItemStack not_see = null;
			if (isup) {
				not_see = util.createItem(159, 1, "§b§l파티클 §c§l차단 §f§l설정", util
						.createArray("", notsee ? "§a§l클릭시 모든 파티클을 볼 수 있게 됩니다."
								: "§c§l클릭시 모든 파티클을 보지 않습니다.", ""));
				if (notsee) {
					not_see.setDurability((short) 14);
				} else {
					not_see.setDurability((short) 5);
				}
			} else {
				not_see = util.createItem((notsee ? Material.REDSTONE_LAMP_OFF
						: Material.REDSTONE_LAMP_ON), 1,
						"§b§l파티클 §c§l차단 §f§l설정", util.createArray("",
								notsee ? "§a§l클릭시 모든 파티클을 볼 수 있게 됩니다."
										: "§c§l클릭시 모든 파티클을 보지 않습니다.", ""));
			}
			boolean mynotsee = ParticleMain.mynotsee.contains(p.getName());
			ItemStack mynot_see = null;
			if (isup) {
				mynot_see = util.createItem(159, 1,
						"§f§l자신의 §b§l파티클 §c§l차단 §f§l설정", util.createArray("",
								mynotsee ? "§a§l클릭시 자신의 파티클을 볼 수 있게 됩니다."
										: "§c§l클릭시 자신의 파티클을 보지 않습니다.", ""));
				if (mynotsee) {
					mynot_see.setDurability((short) 14);
				} else {
					mynot_see.setDurability((short) 5);
				}
			} else {
				mynot_see = util.createItem(
						(mynotsee ? Material.REDSTONE_LAMP_OFF
								: Material.REDSTONE_LAMP_ON), 1,
						"§f§l자신의 §b§l파티클 §c§l차단 §f§l설정", util.createArray("",
								mynotsee ? "§a§l클릭시 자신의 파티클을 볼 수 있게 됩니다."
										: "§c§l클릭시 자신의 파티클을 보지 않습니다.", ""));
			}
			boolean move = ParticleMain.ismove.contains(p.getName());
			ItemStack moveitem = null;
			if (isup) {
				moveitem = util.createItem(159, 1,
						"§b§l파티클§f§l이 나오는 §a§l시점 §f§l설정", util.createArray("",
								move ? "§a§l클릭시 주기적으로 파티클이 나오게 됩니다."
										: "§c§l클릭시 움직일때 파티클이 나오게 됩니다.", ""));
				if (move) {
					moveitem.setDurability((short) 14);
				} else {
					moveitem.setDurability((short) 5);
				}
			} else {
				moveitem = util.createItem((move ? Material.REDSTONE_LAMP_OFF
						: Material.REDSTONE_LAMP_ON), 1,
						"§b§l파티클§f§l이 나오는 §a§l시점 §f§l설정", util.createArray("",
								move ? "§a§l클릭시 주기적으로 파티클이 나오게 됩니다."
										: "§c§l클릭시 움직일때 파티클이 나오게 됩니다.", ""));
			}
			for (int t = 0; t < i.getSize(); t++) {
				i.setItem(t, nullitem);
			}
			i.setItem(12, head);
			i.setItem(16, not_see);
			i.setItem(19, body_1);
			i.setItem(20, wing_1);
			i.setItem(21, body_2);
			i.setItem(22, wing_2);
			i.setItem(23, body_3);
			i.setItem(25, mynot_see);
			i.setItem(28, foot_1);
			i.setItem(30, foot_2);
			i.setItem(32, foot_3);
			i.setItem(34, moveitem);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (label.equalsIgnoreCase("SHP")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					util.msg(p, "--=--=-- SH Particle --=--=--");
					util.msg(p, "/shp open | 파티클 선택창을 엽니다.");
					util.msg(p, "/shp s | 파티클 설정 GUI를 엽니다.");
					util.msg(p, "/shp 퍼짐 <1~10> | 퍼지는 정도를 설정합니다.");
					util.msg(p, "--=--=--=--=--=--=--=--");
					return true;
				}
				if (args[0].equalsIgnoreCase("head")
						|| args[0].equalsIgnoreCase("0")
						|| args[0].equalsIgnoreCase("머리")) {
					util.msg(p, "이제 당신의 파티클은 머리 위에 있습니다.");
					effectup.put(p.getName(), (byte) 0);
				} else if (args[0].equalsIgnoreCase("body")
						|| args[0].equalsIgnoreCase("1")
						|| args[0].equalsIgnoreCase("몸통")) {
					util.msg(p, "이제 당신의 파티클은 몸 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 1);
				} else if (args[0].equalsIgnoreCase("foot")
						|| args[0].equalsIgnoreCase("2")
						|| args[0].equalsIgnoreCase("발")) {
					util.msg(p, "이제 당신의 파티클은 발 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 2);
				} else if (args[0].equalsIgnoreCase("circle")
						|| args[0].equalsIgnoreCase("3")
						|| args[0].equalsIgnoreCase("원")) {
					util.msg(p, "이제 당신의 파티클모양은 원입니다.");
					effecttype.put(p.getName(), (byte) 0);
				} else if (args[0].equalsIgnoreCase("star")
						|| args[0].equalsIgnoreCase("4")
						|| args[0].equalsIgnoreCase("별")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Star")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Star)");
						return true;
					}
					util.msg(p, "이제 당신의 파티클모양은 별입니다.");
					effecttype.put(p.getName(), (byte) 1);
				} else if (args[0].equalsIgnoreCase("triangle")
						|| args[0].equalsIgnoreCase("5")
						|| args[0].equalsIgnoreCase("t")
						|| args[0].equalsIgnoreCase("삼각형")) {
					if (!p.isOp()
							&& !p.hasPermission("SHParticle.Shape.Triangle")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Triangle)");
						return true;
					}
					util.msg(p, "이제 당신의 파티클모양은 삼각형입니다.");
					effecttype.put(p.getName(), (byte) 2);
				} else if (args[0].equalsIgnoreCase("wing1")
						|| args[0].equalsIgnoreCase("6")
						|| args[0].equalsIgnoreCase("w1")
						|| args[0].equalsIgnoreCase("날개1")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Wing1")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Wing1)");
						return true;
					}
					util.msg(p, "이제 당신의 파티클모양은 날개 1입니다.");
					effectup.put(p.getName(), (byte) 1);
					effecttype.put(p.getName(), (byte) 3);
				} else if (args[0].equalsIgnoreCase("wing2")
						|| args[0].equalsIgnoreCase("7")
						|| args[0].equalsIgnoreCase("w2")
						|| args[0].equalsIgnoreCase("날개2")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Wing2")) {
						util.msg(p, "권한이 없습니다. (SHParticle.Shape.Wing2)");
						return true;
					}
					util.msg(p, "이제 당신의 파티클모양은 날개 2입니다.");
					effectup.put(p.getName(), (byte) 2);
					effecttype.put(p.getName(), (byte) 3);
				} else if (args[0].equals("설정")
						|| args[0].equalsIgnoreCase("s")
						|| args[0].equalsIgnoreCase("setting")
						|| args[0].equalsIgnoreCase("settings")) {
					Inventory i = Bukkit
							.createInventory(null, 45, "§0§l파티클 설정");
					p.openInventory(i);
				} else if (args[0].equalsIgnoreCase("퍼짐")) {
					if (args.length != 2) {
						util.msg(p, "명령어가 올바르지 않습니다.");
						return true;
					}
					if (!p.isOp() && !p.hasPermission("SHParticle.SetSpeed")) {
						util.msg(p, "권한이 없습니다. (SHParticle.SetSpeed)");
						return true;
					}
					int speed = 1;
					try {
						speed = Integer.parseInt(args[1]);
					} catch (Exception ex) {
						speed = -1;
					}
					if (speed < 1 || speed > 10) {
						util.msg(p, "숫자가 올바르지 않습니다. (1~10)");
						return true;
					}
					this.speed.put(p.getName(), speed);
					util.msg(p, "퍼짐이 " + Integer.toString(speed)
							+ "으로 설정되었습니다.");
				} else {
					Inventory i = Bukkit.createInventory(null, over7 ? 54 : 45,
							"§0§l파티클 선택");
					int now = 0;
					if (effect.get(p.getName()) == null) {
						Load(p.getName());
						if (effect.get(p.getName()) == null) {
							effect.put(p.getName(), new ArrayList<String>());
						}
					}
					ArrayList<String> list = effect.get(p.getName());
					if (over7) {
						for (ParticleEffect e : ParticleEffect
								.values()) {
							if (e != null) {
								if (list.contains(e.getName())) {
									ArrayList<String> lst = new ArrayList<String>();
									lst.add(ChatColor.GREEN
											+ "§l[이 파티클을 가지고 있습니다.]");
									lst.add("§7[Dev by SHGroup]");
									i.setItem(now, util.createItem(
											Material.ENCHANTED_BOOK, 1, "§6§l"
													+ e.getName(), lst));
								} else {
									ArrayList<String> lst = new ArrayList<String>();
									lst.add(ChatColor.RED
											+ "§l[이 파티클을 가지고 있지 않습니다.]");
									lst.add("§7[Dev by SHGroup]");
									i.setItem(now, util.createItem(
											Material.PAPER, 1,
											"§6§l" + e.getName(), lst));
								}
								now += 1;
							}
						}
					} else {
						for (ParticleEffect e : ParticleEffect.values()) {
							if (e != null) {
								if (list.contains(e.getName())) {
									ArrayList<String> lst = new ArrayList<String>();
									lst.add(ChatColor.GREEN
											+ "§l[이 파티클을 가지고 있습니다.]");
									lst.add("§7[Dev by SHGroup]");
									i.setItem(now, util.createItem(
											Material.ENCHANTED_BOOK, 1, "§6§l"
													+ e.getName(), lst));
								} else {
									ArrayList<String> lst = new ArrayList<String>();
									lst.add(ChatColor.RED
											+ "§l[이 파티클을 가지고 있지 않습니다.]");
									lst.add("§7[Dev by SHGroup]");
									i.setItem(now, util.createItem(
											Material.PAPER, 1,
											"§6§l" + e.getName(), lst));
								}
								now += 1;
							}
						}
					}
					i.setItem(now, util.createItem(Material.MAGMA_CREAM, 1,
							ChatColor.RED + "§l제거"));
					p.openInventory(i);
					updateInventory(p);
				}
				return true;
			} else {
				util.msg(sender, "플레이어만 사용할 수 있습니다.");
				return true;
			}
		}
		if (!sender.isOp()) {
			util.msg(sender, "권한이 없습니다.");
			util.msg(sender, "유저는 /SHP 명령어를 이용해주세요.");
			return true;
		}
		if (!sender.isOp()) {
			util.msg(sender, "권한이 없습니다.");
			return true;
		}
		if (args.length == 0 || args[0].equals("도움말")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				util.msg(p, "--=--=-- SH Particle --=--=--");
				util.msg(p, "/파티클 <파티클이름> (플레이어)");
				util.msg(p, "/파티클 전시");
				util.msg(p, "/파티클 개수설정 <1~50>");
				util.msg(p, "/파티클 개수설정 <1~50> <플레이어>");
				util.msg(p, "/파티클 범위설정 <3~16>");
				util.msg(p, "/파티클 초기화 <플레이어>");
				util.msg(p, "/파티클 추가 <파티클이름> (플레이어)");
				util.msg(p, "/파티클 모두주기 <플레이어>");
				util.msg(p, "/파티클 파티클목록");
				util.msg(p, "--=--=--=--=--=--=--=--");
				return true;
			} else {
				util.msg(sender, "--=--=-- SH Particle --=--=--");
				util.msg(sender, "/파티클 <파티클이름> <플레이어>");
				util.msg(sender, "/파티클 파티클목록");
				util.msg(sender, "/파티클 전시");
				util.msg(sender, "/파티클 초기화 <플레이어>");
				util.msg(sender, "/파티클 추가 <파티클이름> <플레이어>");
				util.msg(sender, "/파티클 모두주기 <플레이어>");
				util.msg(sender, "/파티클 개수설정 <1~50> <플레이어>");
				util.msg(sender, "/파티클 범위설정 <3~16>");
				util.msg(sender, "--=--=--=--=--=--=--=--");
				return true;
			}
		} else if (args[0].equals("파티클목록")) {
			util.msg(sender, "--=--=-- 파티클 목록 --=--=--");
			if (!over7) {
				ParticleEffect[] p = ParticleEffect.values();
				String temp = "";
				for (int i = 0; i < p.length; i++) {
					if (temp.equals("")) {
						temp = p[i].getName() + "(id:" + p[i].getId() + "), ";
					} else {
						util.msg(sender,
								temp + p[i].getName() + "(id:" + p[i].getId()
										+ ")");
						temp = "";
					}
				}
			} else {
				ParticleEffect[] p = ParticleEffect
						.values();
				String temp = "";
				for (int i = 0; i < p.length; i++) {
					if (temp.equals("")) {
						temp = p[i].getName() + ", ";
					} else {
						util.msg(sender, temp + p[i].getName());
						temp = "";
					}
				}
			}
			util.msg(sender, "--=--=--=--=--=--=--=--");
			return true;
		} else if (args[0].equals("개수설정")) {
			if (args.length != 2 && args.length != 3) {
				util.msg(sender, "/파티클 개수설정 <개수(1~100)> (플레이어)");
				return true;
			}
			String n = sender.getName();
			if (args.length == 3) {
				if (!sender.isOp()) {
					util.msg(sender, "당신은 권한이 없습니다.");
					return true;
				}
				n = args[2];
			} else {
				if (!(sender instanceof Player)) {
					util.msg(sender, "플레이어만 사용할 수 있습니다.");
					return true;
				}
				if (!sender.hasPermission("SHParticle.setAmount")) {
					util.msg(sender, "당신은 권한이 없습니다.");
					return true;
				}
			}
			int amount = 1;
			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				util.msg(sender, "개수가 올바르지 않습니다.");
				return true;
			}
			if (amount < 1 || amount > 50) {
				util.msg(sender, "개수가 올바르지 않습니다.(1 ~ 50)");
				return true;
			}
			this.amount.put(n, amount);
			util.msg(sender, "파티클의 개수가 " + Integer.toString(amount)
					+ "개가 되었습니다!");
		} else if (args[0].equals("범위설정")) {
			if (args.length != 2) {
				util.msg(sender, "/파티클 범위설정 <범위(3~16)>");
				return true;
			}
			if (!sender.isOp()) {
				util.msg(sender, "당신은 권한이 없습니다.");
				return true;
			}
			int range = 1;
			try {
				range = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				util.msg(sender, "개수가 올바르지 않습니다.");
				return true;
			}
			if (range < 3 || range > 16) {
				util.msg(sender, "범위가 올바르지 않습니다.(3 ~ 16)");
				return true;
			}
			this.range = range;
			Bukkit.broadcastMessage(util.getPerfix() + "파티클의 범위가 "
					+ Integer.toString(range) + "가 되었습니다!");
		} else if (args[0].equals("추가")) {
			if (args.length != 3) {
				util.msg(sender, "/파티클 추가 <파티클/파티클이름> <플레이어>");
				return true;
			}
			ParticleEffect pa = null;
			ParticleEffect effect = null;
			if (over7) {
				if ((effect = fromName(args[1])) == null) {
					util.msg(sender, "파티클 이름이 올바르지 않습니다.");
					return true;
				}
			} else {
				try {
					try {
						pa = ParticleEffect.fromId(Integer.parseInt(args[1]));
					} catch (Exception ex) {
						pa = ParticleEffect.fromName(args[1]);
					}
					if (pa == null) {
						pa = ParticleEffect.fromName(args[1]);
					}
				} catch (Exception ex) {
					util.msg(sender, "파티클 이름이 올바르지 않습니다.");
					return true;
				}
				if (pa == null) {
					util.msg(sender, "파티클 이름이 올바르지 않습니다.");
					return true;
				}
			}
			if (!this.effect.containsKey(args[2])) {
				util.msg(sender, "플레이어가 존재하지 않습니다.");
				return true;
			}
			ArrayList<String> arr = this.effect.get(args[2]);
			if (arr.contains(pa.getId())) {
				util.msg(sender, "해당 플레이어는 이미 그 파티클을 가지고 있습니다.");
				return true;
			}
			if (over7)
				arr.add(effect.getName());
			else
				arr.add(pa.getName());
			this.effect.put(args[2], arr);
			if (Bukkit.getPlayerExact(args[2]) != null) {
				util.msg(Bukkit.getPlayerExact(args[2]), "당신에게 "
						+ (over7 ? effect.getName() : pa.getName())
						+ " 파티클이 지급되었습니다.");
			}
			util.msg(sender,
					args[2] + "님에게 "
							+ (over7 ? effect.getName() : pa.getName())
							+ "파티클을 지급하였습니다.");
		} else if (args[0].equals("초기화")) {
			if (args.length != 2) {
				util.msg(sender, "/파티클 초기화 <플레이어>");
				return true;
			}
			if (!effect.containsKey(args[1])) {
				util.msg(sender, "존재하지 않는 플레이어입니다.");
				return true;
			}
			effect.put(args[1], new ArrayList<String>());
			noweffect.put(args[1], "");
			util.msg(sender, args[1] + "님의 파티클 목록이 초기화되었습니다.");
			if (Bukkit.getPlayerExact(args[1]) != null) {
				util.msg(Bukkit.getPlayerExact(args[1]),
						"당신의 파티클이 모두 초기화되었습니다.");
			}
		} else if (args[0].equals("전시")) {
			if (args.length == 1) {
				util.msg(sender, "───── 파티클 전시 ─────");
				util.msg(sender, "/파티클 전시 목록 (페이지)");
				util.msg(sender, "/파티클 전시 추가 <파티클이름>");
				util.msg(sender, "/파티클 전시 이동 <색인>");
				util.msg(sender, "/파티클 전시 편집 <색인> <파티클이름>");
				util.msg(sender, "/파티클 전시 미세조정 <색인> <월드> <x> <y> <z>");
				util.msg(sender, "/파티클 전시 제거 <색인>");
				util.msg(sender, "(좌표 입력이 없으면 현재 위치이고, 입력시 소수점 가능합니다.)");
				util.msg(sender, "───── 파티클 전시 ─────");
				return true;
			}
			if (args[1].equals("목록")) {
				if (frames.isEmpty()) {
					util.msg(sender, "한개의 전시도 생성되지 않았습니다.");
					return true;
				}
				int page = 1;
				try {
					page = Integer.parseInt(args[2]);
				} catch (Exception e) {
					page = 1;
				}
				util.msg(sender, "───── 파티클 전시 목록 ─────");
				for (int i = (page - 1) * 5; i < Math.min(page * 5,
						frames.size()); i++) {
					util.msg(sender, "[" + i + "] : "
							+ frames.get(i).toString());
				}
				util.msg(sender, "───── 파티클 전시 목록 ─────");
			} else if (args[1].equals("추가")) {
				if (!(sender instanceof Player)) {
					util.msg(sender, "플레이어만 사용이 가능합니다.");
					return true;
				}
				Player p = (Player) sender;
				if (args.length != 3) {
					util.msg(p, "/파티클 전시 추가 <파티클이름>");
					return true;
				}
				String name = null;
				if (over7) {
					ParticleEffect effect = fromName(args[2]);
					if (effect == null) {
						util.msg(p, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					name = effect.getName();
				} else {
					ParticleEffect par;
					try {
						par = ParticleEffect.fromId(Integer.parseInt(args[0]));
					} catch (Exception ex) {
						try {
							par = ParticleEffect.fromName(args[2]);
						} catch (Exception x) {
							util.msg(p, "파티클 이름이 올바르지 않습니다.");
							return true;
						}
					}
					if (par == null) {
						util.msg(p, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					name = par.getName();
				}
				Frame f = new Frame(p.getLocation(), name);
				frames.add(f);
				util.msg(p, "성공적으로 파티클 전시가 등록되었습니다!");
				return true;
			} else if (args[1].equals("이동")) {
				if (!(sender instanceof Player)) {
					util.msg(sender, "플레이어만 사용이 가능합니다.");
					return true;
				}
				Player p = (Player) sender;
				if (args.length != 3) {
					util.msg(p, "/파티클 전시 이동 <색인>");
					return true;
				}
				int index = -1;
				try {
					index = Integer.parseInt(args[2]);
				} catch (Exception ex) {
					index = -1;
				}
				if (index < 0 || index >= frames.size()) {
					util.msg(sender, "색인이 올바르지 않습니다.");
					return true;
				}
				Frame f = frames.get(index);
				f.setLocation(p.getLocation());
				frames.set(index, f);
				util.msg(sender, "정상적으로 전시 위치가 변경되었습니다.");
				return true;
			} else if (args[1].equals("편집")) {
				if (args.length != 4) {
					util.msg(sender, "/파티클 전시 편집 <색인> <파티클이름>");
					return true;
				}
				int index = -1;
				try {
					index = Integer.parseInt(args[2]);
				} catch (Exception ex) {
					index = -1;
				}
				if (index < 0 || index >= frames.size()) {
					util.msg(sender, "색인이 올바르지 않습니다.");
					return true;
				}
				String name = null;
				if (over7) {
					ParticleEffect effect = fromName(args[2]);
					if (effect == null) {
						util.msg(sender, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					name = effect.getName();
				} else {
					ParticleEffect par;
					try {
						par = ParticleEffect.fromId(Integer.parseInt(args[0]));
					} catch (Exception ex) {
						try {
							par = ParticleEffect.fromName(args[2]);
						} catch (Exception x) {
							util.msg(sender, "파티클 이름이 올바르지 않습니다.");
							return true;
						}
					}
					if (par == null) {
						util.msg(sender, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					name = par.getName();
				}
				Frame f = frames.get(index);
				f.par = name;
				frames.set(index, f);
				util.msg(sender, "성공적으로 해당 전시의 파티클이 변경되었습니다.");
				return true;
			} else if (args[1].equals("미세조정")) {
				if (args.length != 7) {
					util.msg(sender, "/파티클 전시 미세조정 <색인> <월드> <x> <y> <z>");
					return true;
				}
				int index = -1;
				try {
					index = Integer.parseInt(args[2]);
				} catch (Exception ex) {
					index = -1;
				}
				if (index < 0 || index >= frames.size()) {
					util.msg(sender, "색인이 올바르지 않습니다.");
					return true;
				}
				String world = args[3];
				double x, y, z;
				try {
					x = Double.parseDouble(args[4]);
					y = Double.parseDouble(args[5]);
					z = Double.parseDouble(args[6]);
				} catch (Exception e) {
					util.msg(sender, "위치가 올바르지 않습니다.");
					return true;
				}
				if (Bukkit.getWorld(world) == null) {
					util.msg(sender, "해당 월드는 존재하지 않습니다.");
					return true;
				}
				Frame f = frames.get(index);
				f.world = world;
				f.x = x;
				f.y = y;
				f.z = z;
				frames.set(index, f);
				util.msg(sender, "전시 위치의 미세조정을 성공하였습니다.");
				return true;
			} else if (args[1].equals("제거")) {
				if (args.length != 3) {
					util.msg(sender, "/파티클 전시 제거 <색인>");
					return true;
				}
				int index = -1;
				try {
					index = Integer.parseInt(args[2]);
				} catch (Exception ex) {
					index = -1;
				}
				if (index < 0 || index >= frames.size()) {
					util.msg(sender, "색인이 올바르지 않습니다.");
					return true;
				}
				frames.remove(index);
				util.msg(sender, "성공적으로 해당 전시가 제거되었습니다.");
			} else {
				util.msg(sender, "명령어가 올바르지 않습니다!");
			}
			return true;
		} else if (args[0].equals("모두주기")) {
			if (args.length != 2) {
				util.msg(sender, "/파티클 모두주기 <플레이어>");
				return true;
			}
			ArrayList<String> list = new ArrayList<String>();
			if (over7) {
				for (ParticleEffect p : ParticleEffect
						.values()) {
					if (p != null) {
						list.add(p.getName());
					}
				}
			} else {
				for (ParticleEffect e : ParticleEffect.values()) {
					if (e != null) {
						list.add(e.getName());
					}
				}
			}
			effect.put(args[1], list);
			util.msg(sender, args[1] + "님에게 파티클을 모두 주었습니다.");
			if (Bukkit.getPlayerExact(args[1]) != null) {
				util.msg(Bukkit.getPlayerExact(args[1]),
						"당신은 이제 모든 파티클을 가지게 되었습니다.");
			}
		} else if (args.length == 1) {
			if (!(sender instanceof Player)) {
				util.msg(sender, "플레이어만 사용할 수 있습니다.");
				return true;
			}
			ArrayList<String> lst = new ArrayList<String>();
			Player p = (Player) sender;
			try {
				ParticleEffect par = null;
				ParticleEffect effect = null;
				if (over7) {
					effect = fromName(args[0]);
					if (effect == null) {
						util.msg(p, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					lst.add(ChatColor.GOLD + "§l" + effect.getName());
				} else {
					try {
						par = ParticleEffect.fromId(Integer.parseInt(args[0]));
					} catch (Exception ex) {
						par = ParticleEffect.fromName(args[0]);
					}
					if (par == null) {
						util.msg(p, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					lst.add(ChatColor.GOLD + "§l" + par.getName());
				}
				lst.add("§7[Dev by SHGroup]");
				ItemStack i = util.createItem(Material.ENCHANTED_BOOK, 1, pr
						+ ChatColor.GREEN + "§l파티클 사용권", lst);
				p.getInventory().addItem(i);
				util.upInv(p);
				util.msg(p, (over7 ? effect.getName() : par.getName())
						+ " 파티클북 지급이 완료되었습니다.");
			} catch (Exception ex) {
				util.msg(p, "파티클 이름이 올바르지 않습니다.");
			}
			return true;
		} else if (args.length == 2
				&& this.getServer().getPlayerExact(args[1]) != null) {
			ArrayList<String> lst = new ArrayList<String>();
			Player p = this.getServer().getPlayerExact(args[1]);
			try {
				ParticleEffect par = null;
				ParticleEffect effect = null;
				if (over7) {
					effect = fromName(args[0]);
					if (effect == null) {
						util.msg(p, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					lst.add(ChatColor.GOLD + "§l" + effect.getName());
				} else {
					try {
						par = ParticleEffect.fromId(Integer.parseInt(args[0]));
					} catch (Exception ex) {
						par = ParticleEffect.fromName(args[0]);
					}
					if (par == null) {
						util.msg(p, "파티클 이름이 올바르지 않습니다.");
						return true;
					}
					lst.add(ChatColor.GOLD + "§l" + par.getName());
				}
				lst.add("§7[Dev by SHGroup]");
				ItemStack i = util.createItem(Material.ENCHANTED_BOOK, 1, pr
						+ ChatColor.GREEN + "§l파티클 사용권", lst);
				p.getInventory().addItem(i);
				util.upInv(p);
				util.msg(p, (over7 ? effect.getName() : par.getName())
						+ " 파티클북이 지급되었습니다.");
				util.msg(sender, (over7 ? effect.getName() : par.getName())
						+ " 파티클북 지급이 완료되었습니다.");
			} catch (Exception ex) {
				util.msg(p, "파티클이 올바르지 않습니다.");
			}
		} else {
			sender.sendMessage("명령어가 올바르지 않습니다.");
			sender.sendMessage("/파티클 도움말");
		}
		return true;
	}

	public static ParticleEffect fromName(
			String name) {
		for (ParticleEffect ef : ParticleEffect
				.values()) {
			if (name.equalsIgnoreCase(ef.toString())
					|| name.equalsIgnoreCase(ef.getName()))
				return ef;
		}
		return null;
	}

	public List<Player> getOnline() {
		List<Player> pls = new ArrayList<>();
		try {
			Method method = Bukkit.class.getMethod("getOnlinePlayers");
			Object players = method.invoke(null);
			if (players instanceof Player[]) {
				Player[] oldPlayers = (Player[]) players;
				pls.addAll(Arrays.asList(oldPlayers));
			} else {
				Collection<Player> newPlayers = (Collection<Player>) players;
				pls.addAll(newPlayers);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pls;
	}
	public static List<Player> sgetOnline() {
		List<Player> pls = new ArrayList<>();
		try {
			Method method = Bukkit.class.getMethod("getOnlinePlayers");
			Object players = method.invoke(null);
			if (players instanceof Player[]) {
				Player[] oldPlayers = (Player[]) players;
				pls.addAll(Arrays.asList(oldPlayers));
			} else {
				Collection<Player> newPlayers = (Collection<Player>) players;
				pls.addAll(newPlayers);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pls;
	}

}