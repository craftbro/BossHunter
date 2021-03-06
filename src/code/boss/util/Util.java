package code.boss.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftMonster;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.boss.effect.ParticleEffect;
import code.boss.main.main;

public class Util {

	main plugin;
	
	private HashMap<Block, Material> rb = new HashMap<Block, Material>();
	
	public Util(main instance){
		plugin = instance;
	}
	
	   /**Gives a random player's name
		 * 
		 * @return a random player's name
		 * 
		 * */
	public String randomName(){
		return Bukkit.getOnlinePlayers()[new Random().nextInt(Bukkit.getOnlinePlayers().length)].getName();
	}
	
	/**Sets the target of any living entity to the nearest player
	 * 
	 * @param e the living entity
	 * 
	 * */
	public void setTargetToNearest(LivingEntity e){
		Player p = getNearest(e);
		
		if(p != null){
		CraftLivingEntity pl = (CraftPlayer)p;
		CraftMonster le = (CraftMonster)e;
		le.getHandle().setGoalTarget(pl.getHandle());
		}
	}
	
	/**Gives a player exp AND money
	 * 
	 * @param p the player
	 * @param xp the amount of xp
	 * @param money the amount of money
	 * @param why the reason why (this will display: "+10 XP and +10 Coins for (why)")
	 * 
	 * */
	public void giveExpAndMoney(Player p, int xp, int money, String why){
		plugin.level.giveExp(p, xp, null);
		plugin.eco.giveMoney(p, money);		
		p.sendMessage(ChatColor.GOLD+"+"+ChatColor.RED+xp+ChatColor.GRAY+" XP"+ChatColor.GOLD+" And +"+ChatColor.RED+money+ChatColor.GRAY+" Coins"+ChatColor.GOLD+" For "+why);
		 p.playSound(p.getEyeLocation(), Sound.CHICKEN_EGG_POP, 100, 1);
	}
	
	/**Changes a String so it will apear in the middle of the chat
	 * 
	 * @param s the string to be modified
	 * 
	 * */
	public String middleOfset(String s){
		int i = 32;
	
		i -= s.length()/2;
		
		
		String p = "";
		
		for(int o=0; o<=i; o++){
			p+=" ";
		}
		
		return p+s;
		
	}
	
	/**Gets the nearest player to that entity
	 * 
	 * @param e the entity
	 * 
	 * */
	public Player getNearest(Entity e){
		Player p = null;
		
		double nearest = 999999999999999d;
		
		for(Player pl : Bukkit.getOnlinePlayers()){
			if(pl.getGameMode() != GameMode.CREATIVE && pl.getLocation().distance(e.getLocation()) < nearest){
				p = pl;
				nearest = pl.getLocation().distance(e.getLocation());
			}
		}
		
		return p;
	}

	/**Adds a lore to an itemstack, please note that this will clear the arraylist
	 * 
	 * @param a the itemstack	
	 * @param s the lore to be added (this arraylist will bet cleared)
	 * 
	 * */
public void addLore(ItemStack a, List<String> s){
		
		
		String s1 = null;
		
		String s2 = null;
		
		for(String ss : s){
			if(ss.contains("@")){
				s.set(s.indexOf(ss), "");
				s1= "";
				s2 = s.get(s.indexOf(s1)+1);
				break;
			}
		}
		
		if(s1 != null){
		
		final String l = s2.replace(" ", "");
		
	 while(s1.length() < l.length()){
		 s1+="-";
	 }
	 
		
	 
	 s1 = ChatColor.BLUE+s1;
	 
	 s.set(s.indexOf(""), s1);
		
		}
		
		ItemMeta meta = a.getItemMeta();
		meta.setLore(s);
		a.setItemMeta(meta);
		
		s.clear();
		
	}

/**Makes a block fall and reappear after 4 seconds
 * 
 * @param b the block
 * 
 * */
     public void fall(final Block b){
    	final Entity f = b.getWorld().spawnFallingBlock(b.getLocation(), b.getTypeId(), b.getData());
    	 final Material m = b.getType();
    	 
    	 
    	 rb.put(b, b.getType());
    	 
    	 b.setType(Material.AIR);
    	 
    	 Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				f.remove();
				b.setType(m);
		//		b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());
				rb.remove(b);
			}
    		 
    	 }, 80);
     }
     /**Heals an living entity. This deals with the heal-gets-to-much issue
 	 * 
 	 * @param e the entity to heal
 	 * @param heal the amount to heal
 	 * 
 	 * */
    public void heal(LivingEntity e, int heal){
    	if(e.getHealth()+heal <= e.getMaxHealth()){
    		e.setHealth(e.getHealth()+heal);
    	}else{
    		e.setHealth(e.getMaxHealth());
    	}
    }
    
    /**Draws a line from 1 location to another, and executes a runnable when completed
	 * 
	 * @param from the location where the line starts
	 * @param to the location the lin ends
	 * @param eff the effect that is played every time the line advances
	 * @param run the runnable that gets executed when 'to' is reached
	 * 
	 * */
    public void lineRunnable(final Location from, final Location to, final ParticleEffect eff, final Runnable run){
    	
    	
    	new BukkitRunnable(){
    		
    		final Vector direction = to.toVector().subtract(from.toVector()).normalize();
    		Location l = from.clone();


    		@Override
    		public void run() {
    			
    			for(int i=0; i<=3; i++){
    			
    			// TODO Auto-generated method stub
    	
    			if(l.getBlock().getType() == Material.AIR){

    			
    		
    			
    			eff.animateAtLocation(l, 100, 10);
    			l.add(direction);
    			}else{
    				run.run();
    				
    				cancel();
    				
    			}
    			}
    		}
    		
    	}.runTaskTimer(plugin, 0, 1);
    }

    /**Draws a line from 1 location to another
	 * 
	 * @param from the location where the line starts
	 * @param to the location the lin ends
	 * @param eff the effect that is played every time the line advances
	 * 
	 * */
   public void line(final Location from, final Location to, final ParticleEffect eff){
	
	
	new BukkitRunnable(){
		
		final Vector direction = to.toVector().subtract(from.toVector()).normalize();
		Location l = from.clone();


		@Override
		public void run() {
			
			// TODO Auto-generated method stub
	
			if(l.getBlock().getType() == Material.AIR){

			
		
			
			eff.animateAtLocation(l, 100, 10);
			l.add(direction);
			}else{
				
				cancel();
				
			}
		
		}
		
	}.runTaskTimer(plugin, 0, 1);
}
   /**Copies a folder and everything in it
	 * 
	 * @param src the folder to copy
	 * @param dest the file to copy it to
	 * 
	 * */
   public static void copyFolder(File src, File dest)
	        throws IOException{
	 
	        if(src.isDirectory()){
	 
	            //if directory not exists, create it
	            if(!dest.exists()){
	               dest.mkdir();
	               System.out.println("Directory copied from " 
	                              + src + "  to " + dest);
	            }
	 
	            //list all the directory contents
	            String files[] = src.list();
	 
	            for (String file : files) {
	               //construct the src and dest file structure
	               File srcFile = new File(src, file);
	               File destFile = new File(dest, file);
	               //recursive copy
	               copyFolder(srcFile,destFile);
	            }
	 
	        }else{
	            //if file, then copy it
	            //Use bytes stream to support all file types
	            if (src.getName().equals("uid.dat")) return;
	            InputStream in = new FileInputStream(src);
	                OutputStream out = new FileOutputStream(dest); 
	 
	                byte[] buffer = new byte[1024];
	 
	                int length;
	                //copy the file content in bytes 
	                while ((length = in.read(buffer)) > 0){
	                   out.write(buffer, 0, length);
	                }
	 
	                in.close();
	                out.close();
	                System.out.println("File copied from " + src + " to " + dest);
	        }
	    }
	 
   /**Copies over worldB to BOSS
	 * 
	 * */
   public World copyWorld(){
		File folder = new File("worldB");
		File n = new File("BOSS");
		
		if(n.exists()){
			deleteFolder(n);
			System.out.print("MEHEHEHEHEHHEHEH");
		}
		
		if(!n.exists()){
			n.mkdir();
		}
		

		
		
	try {
		copyFolder(folder, n);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		
		World wp = Bukkit.createWorld(new WorldCreator("BOSS"));
	
		

		
		return wp;
		

		
	}

   /**Deletes a folder
	 * 
	 * @param folder folder to delete
	 * 
	 * */
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles(); // get Files
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) { // for each file
	            if(f.isDirectory()) {
	                deleteFolder(f); // if isDir delete everything in it
	            } else {
	                f.delete(); // else delete File
	            }
	        }
	    }
	 //   folder.delete(); // delete Folder
	}
   
    /**Gives a random player's name in a fancy format
	 * 
	 * @return a random player's name in a fancy format
	 * 
	 * */
	public String randomNameFormat(){
		return ChatColor.GOLD+"["+ChatColor.DARK_GREEN+randomName()+ChatColor.GOLD+"]: "+ChatColor.YELLOW;
	}
	
	
	public void shutdown(){
		for(Block b : rb.keySet()){
			b.setType(Material.COBBLESTONE);
			b.getWorld().save();
			rb.remove(b);
		}
	}
	
	   /**Broadcast's a message and a sound on delay
		 * 
		 * @param m the message to broadcast
		 * @param s the soudn to broadcast
		 * @param pi the pitch for the sound
		 * @param d the delay
		 * */
	public void broadcastDelaySound(final String m, final Sound s, final int pi, int d){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
			//		plugin.bar.displayLoadingBar("Test", "oops", p, Integer.MAX_VALUE, false);
					
				p.sendMessage(m);
				if(s != null){
				p.playSound(p.getEyeLocation(), s, 100, pi);
				}
				}
			}
			
		}, d);
	}
	
}
