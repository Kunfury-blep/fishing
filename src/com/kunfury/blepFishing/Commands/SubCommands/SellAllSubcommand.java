package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SellAllSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "SellAll";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        FishEconomy.SellFish((Player)sender, true, 1);
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.sellall";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
