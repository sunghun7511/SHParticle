package com.SHGroup.SHParticle;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Frame {
	public String world;
	public double x,y,z;
	public String par;
	public Frame(String n){
		this.world = n.split(":")[0];
		this.x = Double.parseDouble(n.split(":")[1]);
		this.y = Double.parseDouble(n.split(":")[2]);
		this.z = Double.parseDouble(n.split(":")[3]);
		this.par = n.split(":")[4];
	}
	public Frame(String world, double x, double y, double z, String par){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.par = par;
	}
	public Frame(Location l, String par){
		this.world = l.getWorld().getName();
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
		this.par = par;
	}
	public void setLocation(Location l){
		this.world = l.getWorld().getName();
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
	}
	public Location getLocation(){
		return new Location(Bukkit.getWorld(world), x, y, z);
	}
	public String toString(){
		return world + ":" + Double.toString(x) + ":" + Double.toString(y) + ":" + Double.toString(z) + ":" + par;
	}
}
