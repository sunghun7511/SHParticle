package com.SHGroup.SHParticle.command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.SHGroup.SHParticle.ParticleMain;
import com.SHGroup.SHParticle.API.ParticleEffect;

public class CommandSHP implements CommandExecutor {

	private ParticleMain main;

	public CommandSHP(ParticleMain main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
			if (args[0].equalsIgnoreCase("head") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("머리")) {
				util.msg(p, "이제 당신의 파티클은 머리 위에 있습니다.");
				effectup.put(p.getName(), (byte) 0);
			} else if (args[0].equalsIgnoreCase("body") || args[0].equalsIgnoreCase("1")
					|| args[0].equalsIgnoreCase("몸통")) {
				util.msg(p, "이제 당신의 파티클은 몸 주변에 있습니다.");
				effectup.put(p.getName(), (byte) 1);
			} else if (args[0].equalsIgnoreCase("foot") || args[0].equalsIgnoreCase("2")
					|| args[0].equalsIgnoreCase("발")) {
				util.msg(p, "이제 당신의 파티클은 발 주변에 있습니다.");
				effectup.put(p.getName(), (byte) 2);
			} else if (args[0].equalsIgnoreCase("circle") || args[0].equalsIgnoreCase("3")
					|| args[0].equalsIgnoreCase("원")) {
				util.msg(p, "이제 당신의 파티클모양은 원입니다.");
				effecttype.put(p.getName(), (byte) 0);
			} else if (args[0].equalsIgnoreCase("star") || args[0].equalsIgnoreCase("4")
					|| args[0].equalsIgnoreCase("별")) {
				if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Star")) {
					util.msg(p, "권한이 없습니다. (SHParticle.Shape.Star)");
					return true;
				}
				util.msg(p, "이제 당신의 파티클모양은 별입니다.");
				effecttype.put(p.getName(), (byte) 1);
			} else if (args[0].equalsIgnoreCase("triangle") || args[0].equalsIgnoreCase("5")
					|| args[0].equalsIgnoreCase("t") || args[0].equalsIgnoreCase("삼각형")) {
				if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Triangle")) {
					util.msg(p, "권한이 없습니다. (SHParticle.Shape.Triangle)");
					return true;
				}
				util.msg(p, "이제 당신의 파티클모양은 삼각형입니다.");
				effecttype.put(p.getName(), (byte) 2);
			} else if (args[0].equalsIgnoreCase("wing1") || args[0].equalsIgnoreCase("6")
					|| args[0].equalsIgnoreCase("w1") || args[0].equalsIgnoreCase("날개1")) {
				if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Wing1")) {
					util.msg(p, "권한이 없습니다. (SHParticle.Shape.Wing1)");
					return true;
				}
				util.msg(p, "이제 당신의 파티클모양은 날개 1입니다.");
				effectup.put(p.getName(), (byte) 1);
				effecttype.put(p.getName(), (byte) 3);
			} else if (args[0].equalsIgnoreCase("wing2") || args[0].equalsIgnoreCase("7")
					|| args[0].equalsIgnoreCase("w2") || args[0].equalsIgnoreCase("날개2")) {
				if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Wing2")) {
					util.msg(p, "권한이 없습니다. (SHParticle.Shape.Wing2)");
					return true;
				}
				util.msg(p, "이제 당신의 파티클모양은 날개 2입니다.");
				effectup.put(p.getName(), (byte) 2);
				effecttype.put(p.getName(), (byte) 3);
			} else if (args[0].equals("설정") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("setting")
					|| args[0].equalsIgnoreCase("settings")) {
				Inventory i = Bukkit.createInventory(null, 45, "§0§l파티클 설정");
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
				util.msg(p, "퍼짐이 " + Integer.toString(speed) + "으로 설정되었습니다.");
			} else {
				Inventory i = Bukkit.createInventory(null, over7 ? 54 : 45, "§0§l파티클 선택");
				int now = 0;
				if (effect.get(p.getName()) == null) {
					Load(p.getName());
					if (effect.get(p.getName()) == null) {
						effect.put(p.getName(), new ArrayList<String>());
					}
				}
				ArrayList<String> list = effect.get(p.getName());
				if (over7) {
					for (ParticleEffect e : ParticleEffect.values()) {
						if (e != null) {
							if (list.contains(e.getName())) {
								ArrayList<String> lst = new ArrayList<String>();
								lst.add(ChatColor.GREEN + "§l[이 파티클을 가지고 있습니다.]");
								lst.add("§7[Dev by SHGroup]");
								i.setItem(now, util.createItem(Material.ENCHANTED_BOOK, 1, "§6§l" + e.getName(), lst));
							} else {
								ArrayList<String> lst = new ArrayList<String>();
								lst.add(ChatColor.RED + "§l[이 파티클을 가지고 있지 않습니다.]");
								lst.add("§7[Dev by SHGroup]");
								i.setItem(now, util.createItem(Material.PAPER, 1, "§6§l" + e.getName(), lst));
							}
							now += 1;
						}
					}
				} else {
					for (ParticleEffect e : ParticleEffect.values()) {
						if (e != null) {
							if (list.contains(e.getName())) {
								ArrayList<String> lst = new ArrayList<String>();
								lst.add(ChatColor.GREEN + "§l[이 파티클을 가지고 있습니다.]");
								lst.add("§7[Dev by SHGroup]");
								i.setItem(now, util.createItem(Material.ENCHANTED_BOOK, 1, "§6§l" + e.getName(), lst));
							} else {
								ArrayList<String> lst = new ArrayList<String>();
								lst.add(ChatColor.RED + "§l[이 파티클을 가지고 있지 않습니다.]");
								lst.add("§7[Dev by SHGroup]");
								i.setItem(now, util.createItem(Material.PAPER, 1, "§6§l" + e.getName(), lst));
							}
							now += 1;
						}
					}
				}
				i.setItem(now, util.createItem(Material.MAGMA_CREAM, 1, ChatColor.RED + "§l제거"));
				p.openInventory(i);
				updateInventory(p);
			}
			return true;
		} else {
			util.msg(sender, "플레이어만 사용할 수 있습니다.");
			return true;
		}
	}

}
