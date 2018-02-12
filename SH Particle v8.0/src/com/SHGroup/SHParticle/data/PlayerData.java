package com.SHGroup.SHParticle.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.SHGroup.SHParticle.API.ParticleEffect;

public class PlayerData {
	private final Player p;
	private final ArrayList<String> effect = new ArrayList<String>();
	private ParticleEffect noweffect = null;
	private int shapeType = 0;
	private int speed = 0;
	private int amount = 1;
	
	private final static HashMap<Player, PlayerData> instances = new HashMap<>();
	
	private PlayerData(Player p) {
		assert(instances.containsKey(p));
		
		this.p = p;
	}
	
	public static PlayerData getPlayerData(Player p) {
		if(instances.containsKey(p)) {
			return instances.get(p);
		}
		PlayerData pd = new PlayerData(p);
		instances.put(p, pd);
		
		return pd;
	}

	public ParticleEffect getNowEffect() {
		return noweffect;
	}

	public void setNowEffect(ParticleEffect nowEffect) {
		this.noweffect = nowEffect;
	}

	public int getShapeType() {
		return shapeType;
	}

	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Player getPlayer() {
		return p;
	}

	public ArrayList<String> getEffects() {
		return effect;
	}
}
