package fr.depthsickle.net.managers;

import fr.depthsickle.net.Main;
import fr.depthsickle.net.objects.Players;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ManagerPlayers {

    private File file;
    private FileConfiguration fileConfiguration;

    private Map<String, Players> account = new HashMap<>();

    public void enabled() {
        this.registersFiles();

        if (!this.getFileConfiguration().contains("Players")) return;
        for (String name : this.getFileConfiguration().getConfigurationSection("Players").getKeys(false)) {
            this.added(name);
        }
    }

    public void disabled() {
        if (this.getAccount().isEmpty()) return;
        for (Entry<String, Players> account : this.getAccount().entrySet()) {
            this.removed(account.getKey());
        }
    }

    public void added(String name) {
        if (this.getAccount().containsKey(name)) return;
        this.getAccount().put(name, new Players(name,
                this.getFileConfiguration().getBoolean("Players."+ name +".Toggle"),
                this.getFileConfiguration().getInt("Players."+ name +".Used"),
                this.getFileConfiguration().getString("Players."+ name +".Mod")));
    }

    public void removed(String name) {
        if (!this.getAccount().containsKey(name)) return;
        this.getFileConfiguration().set("Players."+ name +".Toggle", this.getAccount().get(name).isToggle());
        this.getFileConfiguration().set("Players."+ name +".Used", this.getAccount().get(name).getUsed());
        this.getFileConfiguration().set("Players."+ name +".Mod", this.getAccount().get(name).getMod());
        this.savedFiles();
    }

    private void registersFiles() {
        this.file = new File(Main.getInstance().getDataFolder().getPath(), "storage/players.yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.getFile());

        if (!this.getFile().exists()) {
            try {
                this.getFile().getParentFile().mkdirs();
                this.getFile().createNewFile();
            } catch (IOException error) {
                Main.getInstance().log("&cAn error was detected when creating a document. (players.yml)");
            }
        }
    }

    private void savedFiles() {
        try {
            this.getFileConfiguration().save(this.getFile());
        } catch (IOException error) {
            Main.getInstance().log("&cAn error was detected while saving a document. (players.yml)");
        }
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public Map<String, Players> getAccount() {
        return account;
    }
}
