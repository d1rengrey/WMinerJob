package net.mxtch.wminerjob;

import net.milkbowl.vault.economy.Economy;
import net.mxtch.wminerjob.commands.MinerCommand;
import net.mxtch.wminerjob.handler.WMinerJobHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.util.Collection;
import java.util.List;

@Plugin(name = "WMinerJob", version = "1.0")
@Commands(
        @Command(name = "miner", permission = "miner.use")
)
public class Core extends JavaPlugin {

    private Economy econ;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        if (!setupEconomy()){
            System.out.println("Vault not enabled, power off");
            pluginManager.disablePlugin(this);
            return;
        }

        pluginManager.registerEvents(new WMinerJobHandler(this, econ), this);

        saveDefaultConfig();

        getCommand("miner").setExecutor(new MinerCommand(this));
    }

    public String getMessage(String path) {
        Object message = getConfig().get(path);
        StringBuilder result = new StringBuilder();

        if (message instanceof Collection){
            List<String> messages = (List<String>) message;
            for (String line : messages){
                result.append(line).append("\n");
            }
            return result.toString();
        }
        return message.toString();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
