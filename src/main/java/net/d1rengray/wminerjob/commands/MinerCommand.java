package net.d1rengray.wminerjob.commands;

import net.d1rengray.wminerjob.Core;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinerCommand implements CommandExecutor {
    private Core core;

    public MinerCommand(Core core) {
        this.core = core;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if (!isPlayer) {
            sender.sendMessage("§cNot allowed for console.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage("§c/miner help§7 - for more information");
            return true;
        }
        if (args[0].equalsIgnoreCase("help") && args.length == 1) {
            sender.sendMessage(core.getMessage("messages.help"));
            return true;
        } else if (args[0].equalsIgnoreCase("set") && args.length == 1) {
            sender.sendMessage("§7Use §c/miner set <pos1/pos2>");
            return true;
        } else if (args[0].equalsIgnoreCase("reload") && args.length == 1){
            core.getPluginLoader().disablePlugin(core);
            core.getPluginLoader().enablePlugin(core);
            sender.sendMessage("§aPlugin successfully reloaded");
            return true;
        } else if (args.length == 1) {
            sender.sendMessage("§c/miner help§7 - for more information");
            return true;
        }

        if (args[1].equalsIgnoreCase("pos1") && args.length == 2) {
            setPosition(player, "settings.pos1");
            sender.sendMessage("§aPos1 successfully set.");
            return true;
        } else if (args[1].equalsIgnoreCase("pos2") && args.length == 2) {
            setPosition(player, "settings.pos2");
            sender.sendMessage("§aPos2 successfully set.");
            return true;
        }
        sender.sendMessage("§c/miner help§7 - for more information");
        return true;
    }

    private void setPosition(Player player, String path) {
        Location location = player.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        String pos = "\"[" +
                x +
                "," +
                y +
                "," +
                z +
                "]\"";
        core.getConfig().set(path, pos);
        core.saveConfig();
    }
}
