package net.mxtch.wminerjob;

import net.mxtch.wminerjob.handler.WMinerJobHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "WMinerJob", version = "1.0")
public class Core extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new WMinerJobHandler(this), this);
        saveDefaultConfig();
    }
}
