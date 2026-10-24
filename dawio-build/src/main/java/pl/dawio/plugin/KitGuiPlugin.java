package pl.dawio.plugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public final class KitGuiPlugin extends JavaPlugin {

    private final Map<String, Kit> kits = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadKits();
        getCommand("kitgui").setExecutor(new KitGuiCommand(this));
        getServer().getPluginManager().registerEvents(new KitGuiListener(this), this);
    }

    private void loadKits() {
        ConfigurationSection kitsSection = getConfig().getConfigurationSection("kits");
        if (kitsSection == null) {
            getLogger().warning("No kits defined in config.yml");
            return;
        }
        for (String kitName : kitsSection.getKeys(false)) {
            ConfigurationSection kitSec = kitsSection.getConfigurationSection(kitName);
            if (kitSec == null) continue;
            int slot = kitSec.getInt("slot", 0);
            Kit kit = new Kit(kitName, slot);
            for (String itemStr : kitSec.getStringList("items")) {
                String[] parts = itemStr.split(":");
                if (parts.length != 2) continue;
                String materialName = parts[0].toUpperCase();
                int amount;
                try {
                    amount = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                kit.addItem(ItemUtil.createItem(materialName, amount));
            }
            kits.put(kitName.toLowerCase(), kit);
        }
    }

    public Map<String, Kit> getKits() {
        return kits;
    }
}
