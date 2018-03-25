package com.SHGroup.SHParticle.command;

import java.awt.Frame;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.SHGroup.SHParticle.ParticleMain;
import com.SHGroup.SHParticle.API.ParticleEffect;

public class CommandParticle implements CommandExecutor{
	
	private ParticleMain main;
	
	public CommandParticle(ParticleMain main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
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
}
