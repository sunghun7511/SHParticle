package com.SHGroup.SHParticle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.SHGroup.SHParticle.API.ParticleEffect;
import com.SHGroup.SHParticle.API.ParticleEffect.NoteColor;
import com.SHGroup.SHParticle.API.ParticleEffect.OrdinaryColor;
import com.SHGroup.SHParticle.API.ParticleEffect.ParticleColor;
import com.SHGroup.SHParticle.API.ParticleEffect.ParticleData;
import com.SHGroup.SHParticle.API.ParticleEffect.ParticleProperty;
import com.SHGroup.SHParticle.data.FrameData;
import com.SHGroup.SHParticle.data.PlayerData;

public class MainScheduler extends TimerTask {
	private final double max = 6.3;
	private final double min = 0.3;
	
	private final ParticleMain main;

	public MainScheduler(ParticleMain main) {
		this.main = main;
	}

	byte i = 0;

	public void run() {
		if (i >= 20) {
			i = 0;
			new Thread() {
				@Override
				public void run() {
					for (Player p : main.getOnline()) {
						if (p.getOpenInventory() != null) {
							if (p.getOpenInventory().getTopInventory()
									.getTitle().equals("§0§l파티클 설정")) {
								main.updateInventory(p);
							}
						}
					}
				}
			}.start();
		}
		i += 1;
		for (Player p : main.getOnline()) {
			if (com.SHGroup.SHParticle.ParticleMain.ismove.contains(p.getName())) {
				continue;
			}
			showParticle(p);
		}
		for (FrameData f : main.getFrameManager().getFrames()) {
			if (main.isOver7()) {
				try {
					sendEffect_over7(
							f.getEffectType(), null,
							f.getLocation(), 256, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					f.getEffectType().display(0.0f, 0.0f, 0.0f, 0,
							1, f.getLocation(), 256, null, null, null);
				} catch (Exception e) {
				}
			}
		}
	}

	private boolean x = false, o = true;
	private boolean[][] wing_shape = {
			{ x, x, x, x, o, o, o, o, o, o, o, o, o, x, x, x, x, o },
			{ o, x, x, x, x, o, o, o, o, o, o, o, x, x, x, x, o, o },
			{ o, o, x, x, x, x, o, x, o, x, o, x, x, x, x, o, o, o },
			{ o, o, o, o, x, x, o, x, x, x, o, x, x, o, o, o, o, o },
			{ o, o, o, o, x, x, x, x, x, x, x, x, x, o, o, o, o, o },
			{ o, o, o, o, x, x, x, x, x, x, x, x, x, o, o, o, o, o },
			{ o, o, o, o, o, x, x, x, o, x, x, x, o, o, o, o, o, o },
			{ o, o, o, o, o, x, x, o, o, o, x, x, o, o, o, o, o, o },
			{ o, o, o, o, x, x, o, o, o, o, o, x, x, o, o, o, o, o } };

	public void showParticle(Player p) {
		try {
			PlayerData pld = PlayerData.getPlayerData(p);
			
			if (pld.getNowEffect() != null
					&& pld.getShapeType() > 0) {
				Location l = p.getLocation();
				ParticleEffect pa = null;
				ParticleEffect effect = pld.getNowEffect();
				
				if (effect == null) {
					return;
				}
				
				ParticleColor c = null;
				if (pa != null && pa.hasProperty(ParticleProperty.COLORABLE)) {
					if (pa == ParticleEffect.NOTE) {
						Random r = new Random();
						int color = r.nextInt(24);
						c = new NoteColor(color);
					} else if ((pa == ParticleEffect.SPELL_MOB
							|| pa == ParticleEffect.SPELL_MOB_AMBIENT || pa == ParticleEffect.REDSTONE)) {
						Random r = new Random();
						c = new OrdinaryColor(r.nextInt(256), r.nextInt(256),
								r.nextInt(256));
					}
				}
				ParticleData pd = null;
				
				if (pld.getAmount() < 1) {
					pld.setAmount(1);
				}
				if (pld.getSpeed() < 0) {
					pld.setSpeed(0);
				}
				
				if (((int) main.effectup.get(p.getName())) != 0) {
					if (!main.effecttype.containsKey(p.getName())) {
						main.effecttype.put(p.getName(), (byte) 0);
					}
					if (main.temp.get(p.getName()) == null
							|| main.temp.get(p.getName()) < main.min
							|| main.temp.get(p.getName()) >= 55.0d) {
						main.temp.put(p.getName(), main.min);
					}
					switch ((int) main.effecttype.get(p.getName())) {
					case 1: {
						if (main.temp.get(p.getName()) >= 55.0d) {
							main.temp.put(p.getName(), main.min);
							return;
						}
						if (main.temp.get(p.getName()) >= 50.0d) {
							main.temp.put(p.getName(),
									main.temp.get(p.getName()) + 1.0d);
							return;
						}
						main.temp.put(p.getName(), 50.0d);
						double temp = ((int) (System.currentTimeMillis() / 10L)) / 10.0D % 360.0D;
						double ow = Math.cos(Math.toRadians(240.0D + temp));
						double dow = Math.cos(Math.toRadians(120.0D + temp));
						double sdf = Math.sin(Math.toRadians(0.0D + temp));
						double ydo = Math.sin(Math.toRadians(120.0D + temp));
						Location location = p.getLocation().clone();
						double cod = Math.cos(Math.toRadians(0.0D + temp));
						double ovw = Math.sin(Math.toRadians(240.0D + temp));
						if (((int) main.effectup.get(p.getName())) == 1) {
							location.add(0.0D, 1.0D, 0.0D);
						} else {
							location.add(0.0D, 0.1D, 0.0D);
						}
						if (main.isOver7()) {
							s_over7(p, location, dow, ydo, cod, sdf, effect,
									null, p);
							s_over7(p, location, cod, sdf, ow, ovw, effect,
									null, p);
							s_over7(p, location, ow, ovw, dow, ydo, effect,
									null, p);
						} else {
							s(p, location, dow, ydo, cod, sdf, pa, c, pd, p);
							s(p, location, cod, sdf, ow, ovw, pa, c, pd, p);
							s(p, location, ow, ovw, dow, ydo, pa, c, pd, p);
						}
						sdf = Math.sin(Math.toRadians(180.0D + temp));
						ydo = Math.sin(Math.toRadians(300.0D + temp));
						location = p.getLocation().clone();
						ovw = Math.sin(Math.toRadians(420.0D + temp));
						if (((int) main.effectup.get(p.getName())) == 1) {
							location.add(0.0D, 1.0D, 0.0D);
						} else {
							location.add(0.0D, 0.1D, 0.0D);
						}
						dow = Math.cos(Math.toRadians(300.0D + temp));
						cod = Math.cos(Math.toRadians(180.0D + temp));
						ow = Math.cos(Math.toRadians(420.0D + temp));
						if (main.over7) {
							s_over7(p, location, ow, ovw, dow, ydo, effect,
									null, p);
							s_over7(p, location, dow, ydo, cod, sdf, effect,
									null, p);
							s_over7(p, location, cod, sdf, ow, ovw, effect,
									null, p);
						} else {
							s(p, location, ow, ovw, dow, ydo, pa, c, pd, p);
							s(p, location, dow, ydo, cod, sdf, pa, c, pd, p);
							s(p, location, cod, sdf, ow, ovw, pa, c, pd, p);
						}
					}
						return;
					case 2: {
						if (main.temp.get(p.getName()) >= 55.0d) {
							main.temp.put(p.getName(), main.min);
							return;
						}
						if (main.temp.get(p.getName()) >= 50.0d) {
							main.temp.put(p.getName(),
									main.temp.get(p.getName()) + 1.0d);
							return;
						}
						main.temp.put(p.getName(), 50.0d);
						double sd = Math.cos(Math.toRadians(0.0D)) * 0.7D;
						double vl = Math.cos(Math.toRadians(240.0D)) * 0.7D;
						double od = Math.cos(Math.toRadians(120.0D)) * 0.7D;
						Location location = p.getLocation().clone();
						double cl = Math.sin(Math.toRadians(240.0D)) * 0.7D;
						double dw = Math.sin(Math.toRadians(0.0D)) * 0.7D;
						if (((int) main.effectup.get(p.getName())) == 1) {
							location.add(0.0D, 1.0D, 0.0D);
						} else {
							location.add(0.0D, 0.1D, 0.0D);
						}
						double ld = Math.sin(Math.toRadians(120.0D)) * 0.7D;
						if (main.over7) {
							s_over7(p, location, sd, dw, vl, cl, effect, null,
									p);
							s_over7(p, location, vl, cl, od, ld, effect, null,
									p);
							s_over7(p, location, od, ld, sd, dw, effect, null,
									p);
						} else {
							s(p, location, sd, dw, vl, cl, pa, c, pd, p);
							s(p, location, vl, cl, od, ld, pa, c, pd, p);
							s(p, location, od, ld, sd, dw, pa, c, pd, p);
						}
						return;
					}
					case 3: {
						main.temp.put(p.getName(), main.temp.get(p.getName()) + 1.0d);
						if (((int)(double)main.temp.get(p.getName())) % 4 != 0) {
							return;
						}
						if (main.effectup.get(p.getName()) == 1) {
							Location location = p.getLocation();
							double space = 0.2D;
							double defX;
							double x = defX = location.getX() - space
									* wing_shape[0].length / 2.0D + space;
							double y = location.clone().getY() + 2.0D;
							double angle = -(location.getYaw() + 180.0F) / 60.0F;
							angle += (location.getYaw() < -180.0F ? 3.25D
									: 2.985D);
							for (int i = 0; i < wing_shape.length; i++) {
								for (int j = 0; j < wing_shape[i].length; j++) {
									if (wing_shape[i][j] != o) {
										Location target = location.clone();
										target.setX(x);
										target.setY(y);
										Vector v = target.toVector().subtract(
												location.toVector());
										Vector v2 = getBackVector(location);
										v = rotateAroundAxisY(v, angle);
										v2.setY(0).multiply(-0.2D);
										location.add(v);
										location.add(v2);
										if (main.isOver7()) {
											sendEffect_over7(effect, p,
													location, null);
										} else {
											pa.display(0.0f, 0.0f, 0.0f, 0, 1,
													location, main.range, c,
													pd, p);
										}
										location.subtract(v2);
										location.subtract(v);
									}
									x += space;
								}
								y -= space;
								x = defX;
							}
						} else {
							Location loc = p.getLocation().clone();
							loc.setPitch(0.0F);
							loc.add(0.0D, 1.8D, 0.0D);
							loc.add(loc.getDirection().multiply(-0.2D));
							Location loc1R = loc.clone();
							loc1R.setYaw(loc1R.getYaw() + 110.0F);
							Location loc2R = loc1R.clone().add(
									loc1R.getDirection().multiply(1));
							Location loc3R = loc1R.clone().add(
									loc1R.getDirection().multiply(0.8D));
							Location loc4R = loc1R.clone().add(
									loc1R.getDirection().multiply(0.6D));
							Location loc5R = loc1R.clone().add(
									loc1R.getDirection().multiply(0.4D));
							Location loc6R = loc1R.clone().add(
									loc1R.getDirection().multiply(0.2D));
							if (main.over7) {
								sendEffect_over7(effect, p,
										loc2R.add(0.0D, 0.8D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc3R.add(0.0D, 0.6D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc4R.add(0.0D, 0.4D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc4R.add(0.0D, 0.4D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc5R.clone().add(0.0D, -0.2D, 0.0D),
										null);
								sendEffect_over7(effect, p,
										loc6R.add(0.0D, -0.2D, 0.0D), null);
							} else {
								pa.display(0, 0, 0, 0, 1,
										loc2R.add(0.0D, 0.8D, 0.0D), 30.0D,
										null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc3R.add(0.0D, 0.6D, 0.0D), 30.0D,
										null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc4R.add(0.0D, 0.4D, 0.0D), 30.0D,
										null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc5R.clone().add(0.0D, -0.2D, 0.0D),
										30.0D, null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc6R.add(0.0D, -0.2D, 0.0D), 30.0D,
										null, null, p);
							}

							int zu = 0;
							while (zu <= 1) {
								zu++;
								if (main.over7) {
									sendEffect_over7(effect, p,
											loc2R.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc3R.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc4R.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc5R.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc6R.add(0.0D, -0.2D, 0.0D), null);
								} else {
									pa.display(0, 0, 0, 0, 1,
											loc2R.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc3R.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc4R.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc5R.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc6R.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
								}
							}
							Location loc1L = loc.clone();
							loc1L.setYaw(loc1L.getYaw() - 110.0F);
							Location loc2L = loc1L.clone().add(
									loc1L.getDirection().multiply(1));
							Location loc3L = loc1L.clone().add(
									loc1L.getDirection().multiply(0.8D));
							Location loc4L = loc1L.clone().add(
									loc1L.getDirection().multiply(0.6D));
							Location loc5L = loc1L.clone().add(
									loc1L.getDirection().multiply(0.4D));
							Location loc6L = loc1L.clone().add(
									loc1L.getDirection().multiply(0.2D));
							if (main.over7) {
								sendEffect_over7(effect, p,
										loc2L.add(0.0D, 0.8D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc3L.add(0.0D, 0.6D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc4L.add(0.0D, 0.4D, 0.0D), null);
								sendEffect_over7(effect, p,
										loc5L.clone().add(0.0D, -0.2D, 0.0D),
										null);
								sendEffect_over7(effect, p,
										loc6L.add(0.0D, -0.2D, 0.0D), null);
							} else {
								pa.display(0, 0, 0, 0, 1,
										loc2L.add(0.0D, 0.8D, 0.0D), 30.0D,
										null, null, p);

								pa.display(0, 0, 0, 0, 1,
										loc3L.add(0.0D, 0.6D, 0.0D), 30.0D,
										null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc4L.add(0.0D, 0.4D, 0.0D), 30.0D,
										null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc5L.clone().add(0.0D, -0.2D, 0.0D),
										30.0D, null, null, p);
								pa.display(0, 0, 0, 0, 1,
										loc6L.add(0.0D, -0.2D, 0.0D), 30.0D,
										null, null, p);
							}
							zu = 0;
							while (zu <= 1) {
								zu++;
								if (main.over7) {
									sendEffect_over7(effect, p,
											loc2L.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc3L.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc4L.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc5L.add(0.0D, -0.2D, 0.0D), null);
									sendEffect_over7(effect, p,
											loc6L.add(0.0D, -0.2D, 0.0D), null);
								} else {
									pa.display(0, 0, 0, 0, 1,
											loc2L.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc3L.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc4L.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc5L.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
									pa.display(0, 0, 0, 0, 1,
											loc6L.add(0.0D, -0.2D, 0.0D),
											30.0D, null, null, p);
								}
							}
						}
						return;
					}
					default:
						main.temp.put(p.getName(),
								(main.temp.get(p.getName()) + main.min));
						if (main.temp.get(p.getName()) > main.max) {
							main.temp.put(p.getName(), main.min);
						}
						l = l.add(new Vector(
								Math.cos(main.temp.get(p.getName())),
								(main.effectup.get(p.getName()) == (byte) 1 ? 1.0
										: 0.0), Math.sin(main.temp.get(p
										.getName()))));
					}
				} else {
					l = l.add(0.0d, 2.0d, 0.0d);
				}
				if (main.isOver7()) {
					sendEffect_over7(effect, p, l, null);
					return;
				}
				pa.display(0.0f, 0.0f, 0.0f,
						(main.speed.get(p.getName())) / 10.0f,
						main.amount.get(p.getName()), l, main.range, c, pd, p);
			}
		} catch (IllegalArgumentException e) {

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Vector getBackVector(Location loc) {
		float newZ = (float) (loc.getZ() + 1.0D * Math.sin(Math.toRadians(loc
				.getYaw() + 90.0F)));
		float newX = (float) (loc.getX() + 1.0D * Math.cos(Math.toRadians(loc
				.getYaw() + 90.0F)));
		return new Vector(newX - loc.getX(), 0.0D, newZ - loc.getZ());
	}

	public Vector rotateAroundAxisY(Vector v, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}
	public void sendEffect_over7(
			ParticleEffect effect, PlayerData pld,
			Location l, Color c) {
		sendEffect_over7(effect, pld, l , main.range, c);
	}

	public void sendEffect_over7(
			ParticleEffect effect, PlayerData pld,
			Location l, int range, Color c) {
		if (player != null) {
			if (!main.amount.containsKey(player.getName())) {
				main.amount.put(player.getName(), 1);
			}
			if (!main.speed.containsKey(player.getName())) {
				main.speed.put(player.getName(), 0);
			}
		}
		ArrayList<Player> receivers = new ArrayList<Player>();
		String worldName = l.getWorld().getName();
		for (Player pls : main.getOnline()) {
			if (!pls.getWorld().getName().equals(worldName)
					|| pls.getLocation().distanceSquared(l) > (main.range * main.range)) {
				continue;
			}
			if (com.SHGroup.SHParticle.ParticleMain.notsee.contains(pls.getName())) {
				continue;
			}
			if (player != null) {
				if (com.SHGroup.SHParticle.ParticleMain.mynotsee.contains(player
						.getName()) && pls.getName().equals(player.getName())) {
					continue;
				}
			}
			receivers.add(pls);
		}
	}

	public void s_over7(Location location, double x1,
			double y1, double x2, double y2,
			ParticleEffect effect, Color c,
			PlayerData pld) {
		double lengthX = x1 - x2;
		double lengthY = y1 - y2;

		double ratio = 0.0D;
		while (ratio < 1.0D) {
			Location l = new Location(location.getWorld(), location.getX() + x1
					- lengthX * ratio, location.getY(), location.getZ() + y1
					- lengthY * ratio);
			sendEffect_over7(effect, pld, l, c);
			ratio += 0.1D;
		}
	}

	public void s(PlayerData pld, Location location, double x1,
			double y1, double x2, double y2, ParticleEffect effect,
			ParticleColor c, ParticleData pd, Player targetplayer) {
		if (pld.getAmount() < 1) {
			pld.setAmount(1);
		}
		if (pld.getSpeed() < 0) {
			pld.setSpeed(0);
		}
		double lengthX = x1 - x2;
		double lengthY = y1 - y2;

		double ratio = 0.0D;
		while (ratio < 1.0D) {
			Location l = new Location(location.getWorld(), location.getX() + x1
					- lengthX * ratio, location.getY(), location.getZ() + y1
					- lengthY * ratio);
			effect.display(0.0f, 0.0f, 0.0f,
					(pld.getSpeed()) / 10.0f,
					pld.getAmount(), l, main.range, c, pd,
					targetplayer);
			ratio += 0.1D;
		}
	}
}
