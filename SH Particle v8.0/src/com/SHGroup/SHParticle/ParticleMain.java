package com.SHGroup.SHParticle;

import java.awt.Frame;
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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.SHGroup.SHParticle.API.ParticleEffect;
import com.SHGroup.SHParticle.API.ReflectionUtils.PackageType;
import com.SHGroup.SHParticle.data.DataManager;
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
	private DataManager datas;
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
	
	public boolean canOpenGUI2Times() {
		return guitwoopen;
	}
	
	public FrameManager getFrameManager() {
		return frames;
	}
	
	public boolean isOver7() {
		return over7;
	}
	
	public DataManager getDataManager() {
		return datas;
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