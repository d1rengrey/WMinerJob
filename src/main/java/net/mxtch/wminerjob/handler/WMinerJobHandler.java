package net.mxtch.wminerjob.handler;

import net.mxtch.wminerjob.Core;
import net.mxtch.wminerjob.MinerBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WMinerJobHandler implements Listener {

    private Core core;
    private List<MinerBlock> blocks;

    public WMinerJobHandler(Core core){
        Set<String> keys = core.getConfig().getConfigurationSection("blocks").getKeys(false);

        this.core = core;
        this.blocks = new ArrayList<MinerBlock>();

        for (String key : keys){
            Material material = Material.valueOf(key);
            int cost = core.getConfig().getConfigurationSection("blocks." + key).getInt("cost");
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

        for (MinerBlock minerBlock : blocks){
            if (minerBlock.getMaterial().equals(block.getType())){
                player.sendMessage("Your reward: " + minerBlock.getCost());
                changeBlockWithDelay(block, minerBlock.getMaterial(), minerBlock.getDelay());
                event.setCancelled(true);
            }
        }
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
