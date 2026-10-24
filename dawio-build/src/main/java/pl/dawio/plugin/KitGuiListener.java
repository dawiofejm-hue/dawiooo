package pl.dawio.plugin;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitGuiListener implements Listener {

    private final KitGuiPlugin plugin;
    private static final String GUI_TITLE = ChatColor.DARK_GREEN + "Kits";

    public KitGuiListener(KitGuiPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Sprawdzamy tytuł otwartego GUI
        if (!GUI_TITLE.equals(event.getView().getTitle())) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= event.getView().getTopInventory().getSize()) return;

        Kit clickedKit = plugin.getKits().values().stream()
                .filter(k -> k.getSlot() == slot)
                .findFirst()
                .orElse(null);
        if (clickedKit == null) return;

        for (ItemStack kitItem : clickedKit.getItems()) {
            player.getInventory().addItem(kitItem.clone());
        }
        player.sendMessage(Component.text("You have received the " + clickedKit.getName() + " kit."));
        player.closeInventory();
    }
}
