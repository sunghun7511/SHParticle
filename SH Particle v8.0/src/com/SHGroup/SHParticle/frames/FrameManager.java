package com.SHGroup.SHParticle.frames;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import com.SHGroup.SHParticle.data.FrameData;

public class FrameManager {
	private JavaPlugin plugin;
	private ArrayList<FrameData> frames = new ArrayList<FrameData>();
	
	public FrameManager(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	public FrameData[] getFrames() {
		return (FrameData[]) frames.toArray();
	}
}
