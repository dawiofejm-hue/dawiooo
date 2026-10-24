package pl.dawio.plugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class KitGuiCommand implements CommandExecutor {

    private final KitGuiPlugin plugin;
    private static final String GUI_TITLE = ChatColor.DARK_GREEN + "Kits";

    public KitGuiCommand(KitGuiPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command."));
            return true;
        }
        openGui(player);
        return true;
    }

    private void openGui(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);
        for (Kit kit : plugin.getKits().values()) {
            ItemStack display = createDisplayItem(kit);
            gui.setItem(kit.getSlot(), display);
        }
        player.openInventory(gui);
    }

    private ItemStack createDisplayItem(Kit kit) {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(kit.getName()));
            meta.lore(java.util.List.of(Component.text("Click to receive this kit")));
            item.setItemMeta(meta);
        }
        return item;
    }
}
