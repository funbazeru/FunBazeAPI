package me.drkapdor.funbazeapi.addon;

import me.drkapdor.funbazeapi.FunBazeApiPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddonsCommand implements CommandExecutor {

    private static final AddonsManager addonManager = FunBazeApiPlugin.getApi().getAddonsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "reload": {
                        if (addonManager.reload())
                            sender.sendMessage("§a§lFun§e§lBaze §8○ §fСистема аддонов успешно перезагружена!");
                    }
                    case "list": {
                        StringBuilder addonsList = new StringBuilder();
                        addonManager.getAddons().forEach(addon -> addonsList.append("§f, §7").append(addon.getName()));
                        sender.sendMessage("§a§lFun§e§lBaze §8○ §fСписок установленных аддонов: §7" + addonsList.substring(4));
                        return true;
                    }
                }
            }
            sender.sendMessage("§cИспользуйте: §6/" + label + " <reload/list>");
        } else sender.sendMessage("§cУ Вас недостаточно полномочий!");
        return true;
    }
}
