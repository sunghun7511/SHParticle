package com.SHGroup.SHParticle.data;

import org.bukkit.Location;

import com.SHGroup.SHParticle.API.ParticleEffect;

public class FrameData {
	private Location location;
	private ParticleEffect effectType;
	private int shapeType;
	
	public FrameData(Location loc, ParticleEffect effect, int shapeType) {
		this.location = loc;
		this.effectType = effect;
		this.shapeType = shapeType;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public ParticleEffect getEffectType() {
		return effectType;
	}
	public void setEffectType(ParticleEffect effectType) {
		this.effectType = effectType;
	}
	public int getShapeType() {
		return shapeType;
	}
	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}
}
