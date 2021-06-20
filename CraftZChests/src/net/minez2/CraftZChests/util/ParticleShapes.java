package net.minez2.CraftZChests.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

import net.minez2.CraftZChests.Main;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class ParticleShapes {
	
	Main plugin = Main.getPlugin(Main.class);
	private int taskID;
	
	public void playParticleShape(String particle, String shape, Location loc) {
		
		if(shape.equalsIgnoreCase("helix")) {
			this.playHelix(particle, loc);
			return;
		}
		
		if(shape.equalsIgnoreCase("rising_halo") || shape.equalsIgnoreCase("risinghalo")) {
			this.playRisingHalo(particle, loc);
			return;
		}
		
		if(shape.equalsIgnoreCase("Z")) {
			this.playZ(particle, loc);
			return;
		}
		
		if(shape.equalsIgnoreCase("normal")) {
			this.playNormal(particle, loc);
			return;
		}
		
		if(shape.equalsIgnoreCase("burst")) {
			this.playBurst(particle, loc);
			return;
		}
		
		if(shape.equalsIgnoreCase("godlyexplosion")) {
			this.playGodlyExplosion(particle, loc);
			return;
		}
		
		if(shape.equalsIgnoreCase("completeshowoff")) {
			this.playCompleteShowoff(particle, loc);
			return;
		}
		
	}
	


	private void endTask(int taskID) {
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	/*
	 * Different shapes are all listed below!
	 */
	
	//Rising_Halo
	private void playRisingHalo(String particle, Location loc) {
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			double height = 0;
			Location first, second, third, fourth, fifth, sixth;
			
			@Override
			public void run() {
				
				height += .1;
				
				first = loc.clone().add(Math.cos(0) + .5, height, Math.sin(0) + .5);
				second = loc.clone().add(Math.cos(Math.PI / 3) + .5, height, Math.sin(Math.PI/3) + .5);
				third = loc.clone().add(Math.cos((2*Math.PI) / 3) + .5, height, Math.sin((2*Math.PI)/3) + .5);
				fourth = loc.clone().add(Math.cos(Math.PI) + .5, height, Math.sin(Math.PI) + .5);
				fifth = loc.clone().add(Math.cos((4 * Math.PI) / 3) + .5, height, Math.sin((4 * Math.PI) / 3) + .5);
				sixth = loc.clone().add(Math.cos((5 * Math.PI) / 3) + .5, height, Math.sin((5 * Math.PI) / 3) + .5);
				
				loc.getWorld().spawnParticle(Particle.valueOf(particle), first, 0);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), second, 0);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), third, 0);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), fourth, 0);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), fifth, 0);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), sixth, 0);
				
				if(height >= 1.5) {
					endTask(taskID);
				}
				
			}
			
		}, 0L, 2L);
	}
	
	//Helix
	private void playHelix(String particle, Location loc) {
		

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			Location first, second;

			@Override
			public void run() {
				
				var += Math.PI / 16;
				
				first = loc.clone().add(Math.cos(var) + .5, Math.sin(var) + 1, Math.sin(var) + .5);
				second = loc.clone().add(Math.cos(var + Math.PI) + .5, Math.sin(var) + 1, Math.sin(var + Math.PI) + .5);
				
				loc.getWorld().spawnParticle(Particle.valueOf(particle), first, 0);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), second, 0);
				if(var >= ((7/4) * Math.PI)) {
					endTask(taskID);
				}
				
			}
			
		}, 0L, 2L);
		
	}
	
	
	//Normal
	private void playNormal(String particle, Location loc) {
		

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			Location first, second, third, fourth;

			@Override
			public void run() {
				
				var++;
				
				first = loc.clone().add(.5,  1, .5);
				second = loc.clone().add(.5,  .1 , .5);
				third = loc.clone().add(.5, 1, .5);
				fourth = loc.clone().add(.5,  1,  .5);
				
				//Pick random off set for each direction
				first.setX(first.getX() + Math.random() + .2);
				first.setY(first.getY() + Math.random());
				first.setZ(first.getZ() + Math.random() + .2);
				
				second.setX(second.getX() + Math.random() + .2);
				second.setY(second.getY() + Math.random());
				second.setZ(second.getZ() - Math.random() - .2);
				
				third.setX(third.getX() - Math.random() - .2);
				third.setY(third.getY() + Math.random());
				third.setZ(third.getZ() + Math.random() + .2);
				
				fourth.setX(fourth.getX() - Math.random() - .2);
				fourth.setY(fourth.getY() + Math.random());
				fourth.setZ(fourth.getZ() - Math.random() - .2);
				
				
				
				loc.getWorld().spawnParticle(Particle.valueOf(particle), first, 0, -.01, .01, -.01, 2);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), second, 0, -.01, -.01, .01, 2);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), third, 0, .01, .01, -.01, 2);
				loc.getWorld().spawnParticle(Particle.valueOf(particle), fourth, 0, .01, -.01, .01, 2);
				if(var >= 10) {
					endTask(taskID);
				}
				
			}
			
		}, 0L, 2L);
		
	}
	
	//Umbrella
	private void playUmbrella(String particle, Location loc) {
		

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			Location start;

			@Override
			public void run() {
				

				start = loc.clone().add( .5,  1,  .5);
				
				//Make trail from 1 in y to 2 in y
				for(int i = 0; i <= var && i <= 15; ++i) {
					Location spawnLoc = start.clone();
					spawnLoc.setY(start.getY() + (i * .1));
					loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0);
				}
				//Create a lil outward explosion in x and z (maybe a lil y)
				double rad = 0;
				
				if(var >= 15) {
					for(int j = 0; j <= var - 15 && j <= 15; ++j) {
						Location spawnLoc = loc.clone().add(.5, 2, .5);
						spawnLoc.setY(spawnLoc.getY() - (j * .1));
						
						Location first, second, third, fourth, fifth, sixth, seventh, eighth;
						
						first = spawnLoc.clone().add(.1 * rad, 0, .1 * rad);
						second = spawnLoc.clone().add(.1 * rad, 0, 0);
						third = spawnLoc.clone().add(0, 0, .1 * rad);
						fourth = spawnLoc.clone().add(-.1 * rad, 0, 0);
						fifth = spawnLoc.clone().add(-.1 * rad, 0, .1 *rad);
						sixth = spawnLoc.clone().add(0, 0, -.1 * rad);
						seventh = spawnLoc.clone().add(-.1 * rad, 0, -.1 * rad);
						eighth = spawnLoc.clone().add(.1 * rad, 0, -.1 * rad);
						
						++rad;
						loc.getWorld().spawnParticle(Particle.valueOf(particle), first, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), second, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), third, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), fourth, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), fifth, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), sixth, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), seventh, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), eighth, 0);
					}
				}
				
				++var;
				
				if(var > 25) {
					endTask(taskID);
				}
				
			}
			
		}, 0L, 2L);
		
	}
	
	//Z
	private void playZ(String particle, Location loc) {
		

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			//Start 2 above and 1 in x
			Location startLoc = loc.clone().add(1, 2, .5);

			@Override
			public void run() {
				
				//Make trail from 1 in x to 0 in x
				for(int i = 0; i <= var && i <= 10; ++i) {
					Location spawnLoc = startLoc.clone();
					spawnLoc.setX(startLoc.getX() - (i * .1));
					loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0);
				}
				//Make trail from 0 in x and 2 in y to 1 in x and 1 in y
				if(var >= 10) {
					for(int j = 0; j <= var - 10 && j <= 10; ++j) {
						Location spawnLoc2 = loc.clone().add(0, 2, .5);
						spawnLoc2.setX(spawnLoc2.getX() + (j * .1));
						spawnLoc2.setY(spawnLoc2.getY() - (j * .1));
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc2, 0);
					}
				}
				//Make trail from 1 in x to 0 in x
				if(var >= 20) {
					for(int z = 0; z <= var - 20 && z <= 10; ++z) {
						Location spawnLoc = loc.clone().add(1, 1, .5);
						spawnLoc.setX(spawnLoc.getX() - (z * .1));
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0);
					}
				}

				
				++var;

				if(var >= 31) {
					endTask(taskID);
				}
				
			}
			
		}, 0L, 2L);
		
	}
	
	//Burst
	private void playBurst(String particle, Location loc) {
		

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			Location start;

			@Override
			public void run() {
				

				start = loc.clone().add( .5,  1,  .5);
				
				//Make trail from 1 in y to 2.5 in y
				for(int i = 0; i <= var && i <= 15; ++i) {
					Location spawnLoc = start.clone();
					spawnLoc.setY(start.getY() + (i * .1));
					loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0);
				}
				//Lil pause
				if(var >= 15) {
				for(int i = 0; i <= var - 15 && i <= 5; ++i) {
					Location spawnLoc = start.clone().add(0, 1.5, 0);
					loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0);
				}
				}
				
				//Create a lil outward explosion in x and z (maybe a lil y)
				
				if(var >= 20) {
					for(int j = 0; j <= var - 20 && j <= 20; ++j) {
						Location spawnLoc = loc.clone().add(.5, 2.5, .5);
						
						
						
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, .8, 0, 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, 0 ,0 , .8);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, -.8 , 0 , 0);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, 0 , 0, -.8);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, .57, 0, .57);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, -.57, 0, -.57);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, .57, 0, -.57);
						loc.getWorld().spawnParticle(Particle.valueOf(particle), spawnLoc, 0, -.57, 0, .57);
					}
				}
				
				++var;
				
				if(var > 30) {
					endTask(taskID);
				}
				
			}
			
		}, 0L, 2L);
		
	}
	
	private void playGodlyExplosion(String particle, Location loc) {
		
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			double count = 0;
			Location first, second;

			@Override
			public void run() {
				
				//Narrow spiral from god				
				if(count <= 100) {
					var += Math.PI / 8;
					double delta_y = count * .2;
					
				for(int i = 0; i <= count && i <= 50; ++i) {
					first = loc.clone().add(Math.cos(var) + .5, (10 - delta_y), Math.sin(var) + .5);
					second = loc.clone().add(Math.cos(var + Math.PI) + .5, (10 - delta_y), Math.sin(var + Math.PI) + .5);	
					loc.getWorld().spawnParticle(Particle.valueOf(particle), first, 0);
					loc.getWorld().spawnParticle(Particle.valueOf(particle), second, 0);
				}
				}

				//3 expanding circles outwards
				if(count >= 50 && count % 5 == 0 && count < 65) {
					Location spawnLoc = loc.clone().add(.5, 0, .5);
					createExpandingCircle(Particle.valueOf(particle), spawnLoc);
				}
				
				if(count >= 66) {
					endTask(taskID);
				}
				++count;
			}
			
		}, 0L, 2L);
		
	}
	
	//CompleteShowOff
	private void playCompleteShowoff(String particle, Location loc) {
		
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			double var = 0;
			double count = 0;
			Location first, second;

			@Override
			public void run() {
				
				//4 Chains from god
				
					// Chain 1
				Location chain1 = loc.clone().add(3.5, 10, 3.5);
					for(int i = 0; i < 30; ++i) {
						Location spawnLoc = chain1.clone().add(-.35*i, -1 * i, -.35 * i);
						loc.getWorld().spawnParticle(Particle.REDSTONE, spawnLoc, 0, .94, .11, .47);
					}
					//Chain 2
					if(count >= 20) {
						Location chain2 = loc.clone().add(-2.5, 10, 3.5);
						for(int i = 0; i < 30; ++i) {
							Location spawnLoc = chain2.clone().add(.35*i, -1 * i, -.35 * i);
							loc.getWorld().spawnParticle(Particle.REDSTONE, spawnLoc, 0, .94, .11, .47);
						}
					}
					//Chain 3
					if(count >= 40) {
						Location chain3 = loc.clone().add(-2.5, 10, -2.5);
						for(int i = 0; i < 30; ++i) {
							Location spawnLoc = chain3.clone().add(.35*i, -1 * i, .35 * i);
							loc.getWorld().spawnParticle(Particle.REDSTONE, spawnLoc, 0, .94, .11, .47);
						}
					}
					//Chain 4
					if(count >= 60) {
						Location chain4 = loc.clone().add(3.5, 10, -2.5);
						for(int i = 0; i < 30; ++i) {
							Location spawnLoc = chain4.clone().add(-.35*i, -1 * i, .35 * i);
							loc.getWorld().spawnParticle(Particle.REDSTONE, spawnLoc, 0, .94, .11, .47);
						}
					}
				//Narrow spiral from god				
				if(count >= 80 && count <= 130) {
					var += Math.PI / 8;
					double delta_y = (count - 80) * .2;
					
				for(int i = 0; i <= 50; ++i) {
					first = loc.clone().add(Math.cos(var) + .5, (10 - delta_y), Math.sin(var) + .5);
					second = loc.clone().add(Math.cos(var + Math.PI) + .5, (10 - delta_y), Math.sin(var + Math.PI) + .5);	
					loc.getWorld().spawnParticle(Particle.valueOf(particle), first, 0);
					loc.getWorld().spawnParticle(Particle.valueOf(particle), second, 0);
				}
				}
				
				
				//3 expanding circles outwards
				if(count >=  130 && count < 145 && count % 5 == 0) {
					Location spawnLoc = loc.clone().add(.5, 0, .5);
					createExpandingCircle(Particle.valueOf(particle), spawnLoc);
				}
				
				
				
				if(count >= 150) {
					endTask(taskID);
				}
				++count;
			}
			
		}, 0L, 2L);
		
	}
	
	/*
	 * Other functions that are helpful
	 */
	private void createExpandingCircle(Particle particle, Location loc) {
		
		//Spawn 50 particles in a circle - .1 radius
		
		
		for(int i = 0; i <= 50; ++i) {
			double vec = ((i * Math.PI) / 25);
			Location spawnLoc = loc.clone().add(.1*Math.cos(vec), 0, .1*Math.sin(vec));
			
			loc.getWorld().spawnParticle(particle, spawnLoc, 0, .6*Math.cos(vec), 0 , .6*Math.sin(vec));
		}
		
	}

}
