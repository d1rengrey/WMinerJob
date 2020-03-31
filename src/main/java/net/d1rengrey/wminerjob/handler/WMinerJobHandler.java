package net.d1rengrey.wminerjob.handler;

import net.d1rengrey.wminerjob.MinerBlock;
import net.d1rengrey.wminerjob.utils.AreaUtils;
import net.milkbowl.vault.economy.Economy;
import net.d1rengrey.wminerjob.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WMinerJobHandler implements Listener {

    private Core core;
    private Economy economy;
    private List<MinerBlock> blocks;

    private Location pos1;
    private Location pos2;

    public WMinerJobHandler(Core core, Economy econ){
        Set<String> keys = core.getConfig().getConfigurationSection("blocks").getKeys(false);

        this.core = core;
        this.economy = econ;
        this.blocks = new ArrayList<MinerBlock>();

        String[] pos1 = core.getConfig().getString("settings.pos1")
                .replace("\"", "")
                .replace("[", "")
                .replace("]", "")
                .split(",");
        String[] pos2 = core.getConfig().getString("settings.pos2")
                .replace("\"", "")
                .replace("[", "")
                .replace("]", "")
                .split(",");

        this.pos1 = new Location(Bukkit.getWorld("world"),
                Double.parseDouble(pos1[0]),
                Double.parseDouble(pos1[1]),
                Double.parseDouble(pos1[2]));

        this.pos2 = new Location(Bukkit.getWorld("world"),
                Double.parseDouble(pos2[0]),
                Double.parseDouble(pos2[1]),
                Double.parseDouble(pos2[2]));

        for (String key : keys){
            Material material = Material.valueOf(key);
            double cost = core.getConfig().getConfigurationSection("blocks." + key).getDouble("cost");
            int delay = core.getConfig().getConfigurationSection("blocks." + key).getInt("delay");
            blocks.add(new MinerBlock.Builder()
                    .setMaterial(material)
                    .setCost(cost)
                    .setDelay(delay)
                    .build()
            );
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (AreaUtils.isInArea(block.getLocation(), pos1, pos2)){
            for (MinerBlock minerBlock : blocks){
                if (minerBlock.getMaterial().equals(block.getType())){
                    economy.depositPlayer(player, minerBlock.getCost());

                    player.sendMessage(core.getMessage("messages.reward").replace("{cost}", Double.toString(minerBlock.getCost())));

                    changeBlockWithDelay(block, minerBlock.getMaterial(), minerBlock.getDelay());
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (AreaUtils.isInArea(block.getLocation(), pos1, pos2))
            event.setCancelled(true);
    }

    private void changeBlockWithDelay(final Block block, final Material material, long delay) {
        block.setType(Material.COBBLESTONE, true);

        new BukkitRunnable() {
            public void run() {
                block.setType(material, true);
            }
        }.runTaskLater(core, 20L * delay);
    }

}
