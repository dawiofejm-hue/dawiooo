package pl.dawio.plugin;

import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class Kit {
    private final String name;
    private final int slot;
    private final List<ItemStack> items = new ArrayList<>();

    public Kit(String name, int slot) {
        this.name = name;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }
}
