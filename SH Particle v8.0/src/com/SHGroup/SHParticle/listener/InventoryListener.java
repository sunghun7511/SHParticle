package com.SHGroup.SHParticle.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.SHGroup.SHParticle.ParticleMain;
import com.SHGroup.SHParticle.API.ParticleEffect;

public class InventoryListener implements Listener {
	private ParticleMain main;

	private InventoryListener(ParticleMain main) {
		this.main = main;
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
						if (ParticleEffect.fromName(
								ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) == null) {
							return;
						}
						ParticleEffect effect = ParticleEffect
								.fromName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
						if (!effect.isSupported()) {
							p.closeInventory();
							util.msg(p, "\"" + effect.getName() + "\" 파티클은 현재 서버 버젼에서 사용할 수 없습니다!");
							return;
						}
						if (!over7 && (effect == ParticleEffect.BLOCK_CRACK || effect == ParticleEffect.BLOCK_DUST
								|| effect == ParticleEffect.ITEM_CRACK)) {
							util.msg(p, "\"" + effect.getName() + "\" 파티클은 현재 사용할 수 없습니다!");
							return;
						}
						setEffect(p.getName(), effect.getName());
						p.closeInventory();
						if (!guitwoopen) {
							util.msg(p, "\"" + effect.getName() + "\" 파티클이 설정되었습니다!");
							return;
						}
					} else {
						ParticleEffect effect = fromName(
								ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
						setEffect(p.getName(), effect.getName());
						p.closeInventory();
						if (!guitwoopen) {
							util.msg(p, "\"" + effect.getName() + "\" 파티클이 설정되었습니다!");
							return;
						}
					}
					Inventory inv = Bukkit.createInventory(null, 9, "§0§l파티클 위치 선택");
					ItemStack i = new ItemStack(Material.PISTON_MOVING_PIECE);
					ItemStack up = util.createItem(Material.SKULL_ITEM, 1, ChatColor.YELLOW + "§l머리 위");
					ItemStack down = util.createItem(Material.DIAMOND_CHESTPLATE, 1, ChatColor.GREEN + "§l몸 주위");
					ItemStack ddown = util.createItem(Material.LEATHER_BOOTS, 1, ChatColor.GREEN + "§l발 주위");
					inv.setContents(new ItemStack[] { i, i, up, i, down, i, ddown, i, i });
					p.openInventory(inv);
				} else if (e.getCurrentItem().getType() == Material.MAGMA_CREAM
						&& e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "§l제거")) {
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
						&& e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "§l몸 주위")) {
					effectup.put(p.getName(), (byte) 1);
				} else if (e.getCurrentItem().getType() == Material.SKULL_ITEM
						&& e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "§l머리 위")) {
					effectup.put(p.getName(), (byte) 0);
				} else if (e.getCurrentItem().getType() == Material.LEATHER_BOOTS
						&& e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "§l발 주위")) {
					effectup.put(p.getName(), (byte) 2);
				} else {
					effectup.put(p.getName(), (byte) 0);
					return;
				}
				util.msg(p, "\"" + noweffect.get(p.getName()) + "\" 파티클이 설정되었습니다!");
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
				String display = e.getCurrentItem().getItemMeta().getDisplayName();
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
				} else if (type == Material.IRON_CHESTPLATE && display.equals("§f§l몸통 (원)")) {
					if (loc == 1 && shape == 0) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 몸 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 1);
					effecttype.put(p.getName(), (byte) 0);
				} else if (type == Material.GHAST_TEAR && display.equals("§f§l날개 1")) {
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
				} else if (type == Material.GHAST_TEAR && display.equals("§f§l날개 2")) {
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
				} else if (type == Material.GOLD_CHESTPLATE && display.equals("§f§l몸통 (별)")) {
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
				} else if (type == Material.DIAMOND_CHESTPLATE && display.equals("§f§l몸통 (삼각형)")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Triangle")) {
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
				} else if (type == Material.IRON_BOOTS && display.equals("§f§l발 (원)")) {
					if (loc == 2 && shape == 0) {
						util.msg(p, "이미 해당 위치에서 파티클이 표시되고 있습니다");
						return;
					}
					util.msg(p, "이제 당신의 파티클은 발 주변에 있습니다.");
					effectup.put(p.getName(), (byte) 2);
					effecttype.put(p.getName(), (byte) 0);
				} else if (type == Material.GOLD_BOOTS && display.equals("§f§l발 (별)")) {
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
				} else if (type == Material.DIAMOND_BOOTS && display.equals("§f§l발 (삼각형)")) {
					if (!p.isOp() && !p.hasPermission("SHParticle.Shape.Triangle")) {
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
}
