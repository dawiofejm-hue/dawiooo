package pl.dawio.plugin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class for creating ItemStack objects from configuration strings.
 */
public final class ItemUtil {

    private ItemUtil() {
        // Utility class – prevent instantiation.
    }

    /**
     * Creates an ItemStack based on a material name and amount.
     *
     * @param materialName name of the material (case‑insensitive, e.g. "DIAMOND_SWORD")
     * @param amount       amount of items, must be at least 1
     * @return ItemStack of the requested material and amount, or AIR if material is invalid
     */
    public static ItemStack createItem(String materialName, int amount) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            return new ItemStack(Material.AIR);
        }
        if (amount < 1) {
            amount = 1;
        }
        ItemStack stack = new ItemStack(material, amount);
        return stack;
    }
}
