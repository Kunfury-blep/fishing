package Tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.kunfury.blepFishing.Setup;

import Miscellaneous.Variables;
import Objects.BaseFishObject;
import Objects.TournamentObject;
import net.md_5.bungee.api.ChatColor;

public class Tournament {

	@SuppressWarnings("serial")
	public void ShowTourney(CommandSender sender) {
		
		final Inventory inv = Bukkit.createInventory(null, 54, " ---Active Tournaments---");
		Player player = (Player)sender;
		
		List<TournamentObject> tourneys = new SortTournaments().Sort();
		
		for(TournamentObject tourney : tourneys) {
			ItemStack item = null;
			ItemMeta meta = null;
			if(tourney.EndDate.isAfter(LocalDateTime.now())) { //Checks if the tournament has ended yet or not
				item = new ItemStack(Material.FISHING_ROD, 1);
				
				meta = item.getItemMeta();
				
				if(!tourney.FishName.equalsIgnoreCase("ALL")) {
					item.setType(Material.SALMON);
					meta.setCustomModelData(tourney.Fish.ModelData);
				}
				meta.setDisplayName(tourney.FishName);
				
				List<String> lore = new ArrayList<String>() {{
					add("Time Left: " + tourney.GetRemainingTime());
					add("End Date: " + tourney.GetFormattedEndDate());
				}};
				meta.setLore(lore);
			}else { //If the tournament has already expiered
				item = new ItemStack(Material.COOKED_COD, 1);
				meta = item.getItemMeta();
				meta.setDisplayName(tourney.FishName + ChatColor.DARK_RED + " - Expired");
				List<String> lore = new ArrayList<String>() {{
					add("End Date: " + tourney.GetFormattedEndDate());
					add("First Place: " + tourney.Winner);
				}};
				meta.setLore(lore);
			}

			item.setItemMeta(meta);
			inv.addItem(item);
		}
		
		
		player.openInventory(inv);
	}
	
	public void CreateTourny(CommandSender sender, String fishName, int duration, int cashPrize, String itemName, int itemCount) {
		
		boolean fishFound = false;
		if(fishName.equalsIgnoreCase("ALL"))
			fishFound = true;
		else {
			for(BaseFishObject fish : Variables.BaseFishList) {
				if(fishName.equalsIgnoreCase(fish.Name)) {
					fishName = fish.Name;
					fishFound = true;
					break;
				}
			}
		}
		
		if(!fishFound) {
			sender.sendMessage("That fish could not be found");
			return;
		}
			
		
		List<ItemStack> items = new ArrayList<>();
		try{
			items.add(new ItemStack(Material.getMaterial(itemName.toUpperCase()), itemCount));
		}
		catch(Exception e){
			sender.sendMessage("That is not a valid item or amount");
			return;
		}
			
		TournamentObject tourney = new TournamentObject(duration, fishName, items, cashPrize); 
		Variables.AddTournament(tourney);
    }

	public void StartTimer(long duration, TournamentObject tourney) {
		new BukkitRunnable() {	        
            @Override
            public void run() {
                new TournamentFinish(tourney);
            }
            
        }.runTaskLater(Setup.getPlugin(), duration);
	}	
	
	public void DelayedWinnings(TournamentObject tourney) {
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable() {
        	@Override
        	  public void run() {
        		new TournamentFinish(tourney);
        	    }
        }, 600);
	}
	
	

}