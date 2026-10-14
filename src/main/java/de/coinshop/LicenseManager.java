package de.coinshop;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lizenzen: Einmal gekauft, gelten sie für immer und geben kleine Vorteile.
 * Es gibt 12 Lizenzen (feste Kennungen, siehe IDS). Name, Symbol, Preis und Wert stehen in der config.yml
 * unter "licenses". Die Beschreibungen stehen in der messages.yml unter "gui.license.desc".
 *
 *  steuerfrei    - keine Verkaufssteuer im Spieler-Shop
 *  gebuehrenfrei - keine Einstellgebühr im Spieler-Shop
 *  freispiel     - einmal am Tag ein Casino-Spiel gratis
 *  rabatt        - Rabatt (in %) im Server-Shop
 *  rente         - zusätzliches tägliches Einkommen (Coins)
 *  zusatzslots   - zusätzliche Verkaufs-Slots im Spieler-Shop
 *  treue         - Login-Serien-Bonus um Prozent erhöht
 *  investor      - Einkommen aus Item-Sets um Prozent erhöht
 *  boersen       - niedrigere Börsen-Gebühr (Wert = Prozent)
 *  ranglisten    - Bonus aus den Ranglisten um Prozent erhöht
 *  aufgaben      - Belohnungen der täglichen Aufgaben um Prozent erhöht
 *  sammler       - Belohnungen der Kollektionen um Prozent erhöht
 */
public final class LicenseManager {

    public static final List<String> IDS = List.of("steuerfrei", "gebuehrenfrei", "freispiel", "rabatt", "rente",
            "zusatzslots", "treue", "investor", "boersen", "ranglisten", "aufgaben", "sammler");

    public record License(String id, String name, Material icon, long price, double value) { }

    private final CoinShopPlugin plugin;
    private final java.util.Map<UUID, Set<String>> cache = new ConcurrentHashMap<>();

    public LicenseManager(CoinShopPlugin plugin) {
        this.plugin = plugin;
    }

    // ------------------------------------------------------------------
    //  Einstellungen
    // ------------------------------------------------------------------

    public List<License> all() {
        List<License> list = new ArrayList<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("licenses");
        for (String id : IDS) {
            ConfigurationSection c = section == null ? null : section.getConfigurationSection(id);
            if (c == null) {
                continue;
            }
            Material icon = Material.matchMaterial(c.getString("icon", "PAPER"));
            list.add(new License(id, c.getString("name", id), icon == null ? Material.PAPER : icon,
                    Math.max(0, c.getLong("price", 10000)), c.getDouble("value", 0)));
        }
        return list;
    }

    public License get(String id) {
        for (License l : all()) {
            if (l.id().equals(id)) {
                return l;
            }
        }
        return null;
    }

    /** Der Wert der Lizenz (z. B. Prozent), 0 wenn es sie nicht gibt. */
    public double value(String id) {
        License l = get(id);
        return l == null ? 0 : l.value();
    }

    // ------------------------------------------------------------------
    //  Besitz
    // ------------------------------------------------------------------

    public Set<String> owned(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> new HashSet<>(plugin.database().getLicenses(u)));
    }

    public boolean has(UUID uuid, String id) {
        try {
            return owned(uuid).contains(id);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void forget(UUID uuid) {
        cache.remove(uuid);
    }

    /** Kauft eine Lizenz. */
    public void buy(Player player, String id) {
        Messages msg = plugin.messages();
        License license = get(id);
        UUID uuid = player.getUniqueId();
        if (license == null) {
            return;
        }
        if (has(uuid, id)) {
            msg.send(player, "license-owned");
            return;
        }
        Database db = plugin.database();
        db.begin();
        try {
            if (!db.takeCoins(uuid, license.price())) {
                db.rollback();
                msg.send(player, "not-enough");
                return;
            }
            db.addShopAccount(license.price());
            db.addLicense(uuid, id);
            db.commit();
        } catch (RuntimeException e) {
            db.rollback();
            plugin.getLogger().severe("Fehler beim Lizenz-Kauf: " + e.getMessage());
            msg.send(player, "error");
            return;
        }
        owned(uuid).add(id);
        db.logTx(uuid, player.getName(), "lizenz", -license.price(), id);
        msg.send(player, "license-bought", "name", license.name());
        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    }

    // ------------------------------------------------------------------
    //  Wirkungen
    // ------------------------------------------------------------------

    /** Preis mit Rabatt (Server-Shop). */
    public long discounted(UUID uuid, long price) {
        if (!has(uuid, "rabatt")) {
            return price;
        }
        long off = (long) Math.floor(price * value("rabatt") / 100.0);
        return Math.max(1, price - off);
    }

    /** Betrag plus Bonus in Prozent, falls der Spieler die Lizenz hat. */
    public long boost(UUID uuid, String id, long amount) {
        if (amount <= 0 || !has(uuid, id)) {
            return amount;
        }
        return amount + (long) Math.floor(amount * value(id) / 100.0);
    }

    /** Zusätzliches tägliches Einkommen (Rente). */
    public long dailyIncome(UUID uuid) {
        return has(uuid, "rente") ? (long) value("rente") : 0;
    }

    public int extraSlots(UUID uuid) {
        return has(uuid, "zusatzslots") ? (int) value("zusatzslots") : 0;
    }

    /** Welchen Tag haben wir (Wechsel zur Shop-Wechsel-Uhrzeit)? */
    private long dayNumber() {
        ZonedDateTime now = ZonedDateTime.now(plugin.serverShop().zone());
        return now.minusHours(plugin.serverShop().resetHour()).toLocalDate().toEpochDay();
    }

    /** Ist das Freispiel heute noch frei? (ohne es zu verbrauchen) */
    public boolean freePlayAvailable(UUID uuid) {
        return has(uuid, "freispiel") && plugin.database().getMetaLong("freeplay_" + uuid, -1) != dayNumber();
    }

    /**
     * Verbraucht das heutige Freispiel, falls vorhanden. Soll innerhalb einer Datenbank-Transaktion
     * aufgerufen werden (wird bei Abbruch zurückgenommen).
     */
    public boolean consumeFreePlay(UUID uuid) {
        if (!freePlayAvailable(uuid)) {
            return false;
        }
        plugin.database().setMetaLong("freeplay_" + uuid, dayNumber());
        return true;
    }
}
