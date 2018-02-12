package com.SHGroup.SHParticle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SHUti {
	private String pr;
	public SHUti(String pr){
		this.pr = pr;
	}
	public Integer rand(Integer i){
		return new Random().nextInt(i);
	}
	@SuppressWarnings("deprecation")
	public void upInv(Player player){
		player.updateInventory();
	}
	public String getPerfix(){
		return pr;
	}
	public ArrayList<String> createArray(String... arrys) {
		ArrayList<String> arr = new ArrayList<String>();
		for(String n: arrys){
			arr.add(n);
		}
		return arr;
	}
	public boolean isSame(ItemStack i1,ItemStack i2){
		try{
			if(!i1.hasItemMeta()){
				return false;
			}
		}catch(Exception ex){
			return false;
		}
		try{
			if(!i2.hasItemMeta()){
				return false;
			}
		}catch(Exception ex){
			return false;
		}
		try{
			if(i1.getType() != i2.getType()){
				return false;
			}
		}catch(Exception ex){}
		try{
			if(!i1.getItemMeta().getDisplayName().equals(i2.getItemMeta().getDisplayName())){
				return false;
			}
		}catch(Exception ex){}
		try{
			if(i1.getItemMeta().getLore() != null){
				for(int i = 0 ; i < i1.getItemMeta().getLore().size() ; i++){
					if(!i1.getItemMeta().getLore().get(i).equals(i2.getItemMeta().getLore().get(i))){
						return false;
					}
				}
			}
		}catch(Exception ex){
			return false;
		}
		try{
			if(!i1.getEnchantments().equals(i2.getEnchantments())){
				return false;
			}
		}catch(Exception ex){}
		return true;
	}
	@SuppressWarnings("deprecation")
	public ItemStack createItem(int typeId, int amount, String name, ArrayList<String> lore){
		ItemStack i = new ItemStack(typeId, amount);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		i.setItemMeta(im);
		return i;
	}
	@SuppressWarnings("deprecation")
	public ItemStack createItem(int typeId, int amount, String name){
		ItemStack i = new ItemStack(typeId, amount);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}
	public ItemStack createItem(Material type, int amount, String name, ArrayList<String> lore){
		ItemStack i = new ItemStack(type, amount);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		i.setItemMeta(im);
		return i;
	}
	public ItemStack createItem(Material type, int amount, String name){
		ItemStack i = new ItemStack(type, amount);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}
	public void RandomFirework(Location l){
	      Firework fw = (Firework)l.getWorld().spawnEntity(l, EntityType.FIREWORK);
	      FireworkMeta fm = fw.getFireworkMeta();
	      Random r = new Random();
	      int fType = r.nextInt(5) + 1;
	      FireworkEffect.Type type = FireworkEffect.Type.BALL;
	      switch (fType) {
	      case 1:
	      default:
	        type = FireworkEffect.Type.BALL;
	        break;
	      case 2:
	        type = FireworkEffect.Type.BURST;
	        break;
	      case 3:
	        type = FireworkEffect.Type.CREEPER;
	        break;
	      case 4:
	        type = FireworkEffect.Type.STAR;
	        break;
	      case 5:
	        type = FireworkEffect.Type.BALL_LARGE;
	      }
	      FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.RED).with(type).trail(r.nextBoolean()).build();
	      fm.addEffect(effect);
	      int power = r.nextInt(2) + 1;
	      fm.setPower(power);
	      fw.setFireworkMeta(fm);
	}
	public boolean isOp(Player p){
		return p.isOp();
	}
	public boolean hasPerm(Player p, String permissions){
		return p.hasPermission(permissions);
	}
	public String addColor(String content){
		return content.replace("&", "§");
	}
	public void msg(Player player, String content){
		player.sendMessage(this.getPerfix() + content);
		return;
	}
	public void msg(CommandSender sender, String content){
		sender.sendMessage(this.getPerfix() + content);
		return;
	}
	public void teleport(Player player, Location loc){
		player.teleport(loc);
		return;
	}
	public void teleport(Player player, Entity target){
		player.teleport(target);
		return;
	}
	public void print(String content){
		Bukkit.getConsoleSender().sendMessage(content);
		return;
	}
	public void bc(String content){
		Bukkit.broadcastMessage(this.getPerfix() + content);
		return;
	}
	public void fileSave(String Folder, String File, String content){
		   File f = new File(Folder + (Folder.endsWith("\\")?"":"\\") + File);
		   File folder = new File(Folder);
		   try{
			   if(!f.exists()){
				   folder.mkdirs();
				   f.createNewFile();
			   }
			   BufferedWriter BW = new BufferedWriter(new FileWriter(f));
			   BW.append(content);	  
			   BW.flush();
			   BW.close();
		   }catch(IOException localIOException){
		   }
	}
	public String fileLoad(String Filename){
		File f = new File(Filename);
		String content = "";
		try {
			if (!f.exists()) {
				f.createNewFile();
				return "";
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			boolean a = true;
			while ((s=br.readLine())!=null) {
				if(a){
					content = s;
					a = false;
					continue;
				}
				content += "\n" + s;
			}
			br.close();
		}
		catch (IOException localIOException) {
		}
		return content;
	}
}
