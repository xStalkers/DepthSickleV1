package fr.depthsickle.net.managers;

import fr.depthsickle.net.Main;
import fr.depthsickle.net.objects.Account;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.*;

public class AccountManager {

    private File storage;
    private FileConfiguration storageConfiguration;

    private Map<String, Account> account = new HashMap<>();

    public void registersAccounts() {
        this.saveDefaultStorage();

        if (!this.getStorageConfiguration().contains("Accounts")) return;
        for (String player : this.getStorageConfiguration().getConfigurationSection("Accounts").getKeys(false)) {
            this.add(player);
        }
    }

    public void unregistersAccounts() {
        if (this.getAccount().isEmpty()) return;
        for (Entry<String, Account> account : this.getAccount().entrySet()) {
            this.remove(account.getKey());
        }
    }

    public void add(String player) {
        if (this.getAccount().containsKey(player)) return;
        this.getAccount().put(player, new Account(player, this.getStorageConfiguration().getString("Accounts."+ player +".Mode"), this.getStorageConfiguration().getBoolean("Accounts."+ player +".Toggle")));
    }

    public Account create(String player) {
        return this.getAccount().put(player, new Account(player, "harvest", true));
    }

    public void remove(String player) {
        if (!this.getAccount().containsKey(player)) return;
        this.getStorageConfiguration().set("Accounts."+ player +".Mode", this.getAccount().get(player).getMode());
        this.getStorageConfiguration().set("Accounts."+ player +".Toggle", this.getAccount().get(player).isToggle());
        this.saveStorage();
    }

    public void saveDefaultStorage() {
        this.storage = new File(Main.getInstance().getDataFolder().getPath(), "storage/accounts.yml");
        this.storageConfiguration = YamlConfiguration.loadConfiguration(this.getStorage());

        if (!this.getStorage().exists()) {
            try {
                this.getStorage().getParentFile().mkdirs();
                this.getStorage().createNewFile();
            } catch (IOException error) {
                Main.getInstance().log("&cAn error was detected when creating a document. (accounts.yml)");
            }
        }
    }

    public void saveStorage() {
        try {
            this.getStorageConfiguration().save(this.getStorage());
        } catch (IOException e) {
            Main.getInstance().log("&cAn error was detected while saving a document. (accounts.yml)");
        }
    }

    public File getStorage() {
        return storage;
    }

    public FileConfiguration getStorageConfiguration() {
        return storageConfiguration;
    }

    public Map<String, Account> getAccount() {
        return account;
    }

}
