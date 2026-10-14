package de.coinshop.gui;

import de.coinshop.CoinShopPlugin;
import de.coinshop.ItemUtil;
import de.coinshop.LicenseManager;
import de.coinshop.LicenseManager.License;
import de.coinshop.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Das Lizenz-Menü: 12 Lizenzen, einmal kaufen und für immer behalten. */
public final class LicenseMenu extends Menu {

    private static final int[] SLOTS = {10, 11, 12, 13, 14, 15, 19, 20, 21, 22, 23, 24};

    public LicenseMenu(CoinShopPlugin plugin, Player viewer) {
        super(plugin, 5, plugin.messages().get("gui.license.title"));
        Messages msg = plugin.messages();
        LicenseManager manager = plugin.licenses();
        UUID uuid = viewer.getUniqueId();

        List<License> all = manager.all();
        for (int i = 0; i < all.size() && i < SLOTS.length; i++) {
            License l = all.get(i);
            boolean owned = manager.has(uuid, l.id());
            List<Component> lore = new ArrayList<>(msg.getList("gui.license.desc." + l.id(),
                    "value", Math.round(l.value())));
            lore.add(Component.empty());
            if (owned) {
                lore.add(msg.get("gui.license.owned-line"));
                if (l.id().equals("freispiel")) {
                    lore.add(msg.get(manager.freePlayAvailable(uuid)
                            ? "gui.license.freeplay-ready" : "gui.license.freeplay-used"));
                }
            } else {
                lore.add(msg.get("gui.license.price-line", "price", l.price()));
                lore.add(msg.get("gui.license.click-line"));
            }
            ItemStack item = ItemUtil.icon(l.icon(), msg.get(owned ? "gui.license.name-owned" : "gui.license.name",
                    "name", l.name()), lore);
            if (owned) {
                item.editMeta(meta -> meta.setEnchantmentGlintOverride(true));
            }
            set(SLOTS[i], item, owned ? null : (player, click) -> {
                manager.buy(player, l.id());
                new LicenseMenu(plugin, player).open(player);
            });
        }

        set(38, balanceIcon(viewer), incomeClick());
        set(40, backButton(), (player, click) -> new MainMenu(plugin).open(player));
        fillEmpty();
    }
}
