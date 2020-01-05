package fr.depthsickle.net;

import com.google.common.base.Charsets;
import fr.depthsickle.net.commands.CommandSickle;
import fr.depthsickle.net.helpers.PluginsHelper;
import fr.depthsickle.net.helpers.actions.ActionBar;
import fr.depthsickle.net.helpers.librairy.statistic.Metrics;
import fr.depthsickle.net.listeners.PlayersHarvestListener;
import fr.depthsickle.net.listeners.PlayersManagerListener;
import fr.depthsickle.net.managers.ManagerPlayers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Economy economy = null;

    private ManagerPlayers playersManager = new ManagerPlayers();
    private PluginsHelper pluginsHelper = new PluginsHelper();
    private ActionBar actionBar = new ActionBar();

    private String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

    private boolean useOldMethods = false;

    private File fileMessage;
    private FileConfiguration fileMessageConfiguration;

    private boolean drop;
    private boolean content;
    private String factions;
    private String message;
    private Action action;

    private boolean worldGuard = false;
    private boolean aSkyblock = false;
    private boolean uSkyblock = false;
    private boolean mcMMO = false;
    private boolean shopguiplus = false;

    private List<String> plant = new ArrayList<>();
    private List<String> world = new ArrayList<>();
    private List<String> plugin = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.registersClass();
        this.registersCommands();
        this.registersPlugins();
        this.registersOptions();
        this.registersNMS();
        this.registersMetrics();
        this.registersEconomy();
        this.registersUpdate();
        this.registersFiles();

        this.getPlayersManager().enabled();
    }

    @Override
    public void onDisable() {
        instance = null;

        this.getPlayersManager().disabled();
    }

    private void registersClass() {
        Bukkit.getPluginManager().registerEvents(new PlayersManagerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayersHarvestListener(), this);
    }

    private void registersCommands() {
        getCommand("depthsickle").setExecutor(new CommandSickle());
    }

    private void registersPlugins() {
        if (this.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            this.worldGuard = true;
            this.getPlugin().add("WorldGuard");
        }

        if (this.getServer().getPluginManager().getPlugin("ASkyBlock") != null) {
            this.aSkyblock = true;
            this.getPlugin().add("ASkyBlock");
        }

        if (this.getServer().getPluginManager().getPlugin("USkyBlock") != null) {
            this.uSkyblock = true;
            this.getPlugin().add("USkyBlock");
        }

        if (this.getServer().getPluginManager().getPlugin("mcMMO") != null) {
            this.mcMMO = true;
            this.getPlugin().add("McMMO");
        }

        if (this.getServer().getPluginManager().getPlugin("ShopGUIPlus") != null) {
            this.shopguiplus = true;
            this.getPlugin().add("ShopGUIPlus");
        }
    }

    public void registersOptions() {
        this.drop = this.getConfig().getBoolean("Configuration.Drop");
        this.content = this.getConfig().getBoolean("Configuration.Content");

        this.factions = this.getConfig().getString("Plugin.Factions.Plugin");
        this.message = this.getConfig().getString("Configuration.Message");

        this.action = Action.valueOf(this.getConfig().getString("Configuration.Break"));

        this.plant = this.getConfig().getStringList("Plants");
        this.world = this.getConfig().getStringList("Worlds");

        this.log("&6*****************************************************************");

        this.log("&ePlugin(s) hook :");
        for (String plugin : this.getPlugin()) {
            this.log(" &8* &f"+ plugin);
        }

        this.log("&ePlant(s) allowed :");
        for (String plant : this.getPlant()) {
            this.log(" &8* &f"+ plant);
        }

        this.log("&eWorld(s) allowed :");
        for (String world : this.getWorld()) {
            this.log(" &8* &f"+ world);
        }

        this.log("&eBasic configuration :");
        this.log(" &8* &7Drop boolean : &f"+ this.isDrop());
        this.log(" &8* &7Content boolean : &f"+ this.isContent());
        this.log(" &8* &7Message system : &f"+ this.getMessage());
        this.log(" &8* &7Action system : &f"+ this.getAction());

        this.log("&eDevelopment :");
        this.log(" &8* &7Author : &a"+ this.getDescription().getAuthors());
        this.log(" &8* &7Version : &a"+ this.getDescription().getVersion());
        this.log(" &8* &7Download www.spigotmc.org : &ahttps://www.spigotmc.org/resources/depthsickle-automate-your-farms.70511/");

        this.log("&6*****************************************************************");
    }

    private void registersNMS() {
        if (this.getVersion().equalsIgnoreCase("v1_8_R1") || this.getVersion().startsWith("v1_7_")) {
            this.useOldMethods = true;
        }
    }

    private void registersMetrics() {
       try {
           Metrics metrics = new Metrics(this);

           metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
               Map<String, Map<String, Integer>> map = new HashMap<>();
               String javaVersion = System.getProperty("java.version");
               Map<String, Integer> entry = new HashMap<>();
               entry.put(javaVersion, 1);

               if (javaVersion.startsWith("1.7")) {
                   map.put("Java 1.7", entry);
               } else if (javaVersion.startsWith("1.8")) {
                   map.put("Java 1.8", entry);
               } else if (javaVersion.startsWith("1.9")) {
                   map.put("Java 1.9", entry);
               } else {
                   map.put("Other", entry);
               }
               return map;
           }));
       } catch (Exception error) {
           this.log("&cAn error was detected while send data metrics.");
       }

    }

    private boolean registersEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false ;
        }

        RegisteredServiceProvider<Economy> RegisteredServiceProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (RegisteredServiceProvider == null) {
            return false;
        }

        this.economy = ((Economy) RegisteredServiceProvider.getProvider());
        return this.economy != null;
    }

    private void registersUpdate() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=70511")).openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");

            String updateVersion = (new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))).readLine();

            if (!updateVersion.equals(this.getDescription().getVersion())) {
                this.log("&cAn update of the plugin is available ! (Current version : "+ this.getDescription().getVersion() +" | New version : "+ updateVersion +")");
            }

        } catch (IOException error) {
            this.log("&cAn error was detected while searching for the latest version.");
        }
    }

    public void checkUpdate(Player player) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=70511")).openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");

            String updateVersion = (new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))).readLine();

            if (!updateVersion.equals(this.getDescription().getVersion())) {
                if (player.hasPermission(this.getConfig().getString("Permission.Commands"))) {
                    player.sendMessage("§6[DepthSickle] §cAn update of the plugin is available ! §7(Current version : " + this.getDescription().getVersion() + " | New version : " + updateVersion + ")");
                }
            }

        } catch (IOException error) {
            this.log("&cAn error was detected while searching for the latest version.");
        }
    }

    private void registersFiles() {
        this.fileMessage = new File(Main.getInstance().getDataFolder().getPath(), "messages.yml");
        this.fileMessageConfiguration = YamlConfiguration.loadConfiguration(this.getFileMessage());

        if (!this.getFileMessage().exists()) {
            try {
                this.getFileMessage().createNewFile();
                this.getFileMessageConfiguration().set("noAccessPermission", "&cYou do not have permission.");
                this.getFileMessageConfiguration().set("pluginReload", "&aThe plugin has reloaded.");
                this.getFileMessageConfiguration().set("noGamemode", "&cYou must be in survival to use the sickle.");
                this.getFileMessageConfiguration().set("noWorld", "&cYou can not use the sickle in this world.");
                this.getFileMessageConfiguration().set("noRegion", "&cThis plant does not belong to you, so you can not break it.");
                this.getFileMessageConfiguration().set("errorData", "&cThis plant has not reached its maximum age.");
                this.getFileMessageConfiguration().set("errorPlant", "&cThis block is not a plant allowed to harvest.");
                this.getFileMessageConfiguration().set("errorContent", "&cYou do not have enough seed to replant this plant.");
                this.getFileMessageConfiguration().set("errorInventory", "&cYour inventory is full.");
                this.getFileMessageConfiguration().set("noPlayer", "&cThe requested player is not online.");
                this.getFileMessageConfiguration().set("giveSender", "&aYou have just given a sickle to the player %player%.");
                this.getFileMessageConfiguration().set("giveTarget", "&aYou have just received a sickle from the player %player%.");
                this.getFileMessageConfiguration().set("enableMessage", "&aYou have activated the messages of your sickle.");
                this.getFileMessageConfiguration().set("disableMessage", "&cYou have disabled the messages of your sickle.");
                this.getFileMessageConfiguration().set("disableCommand", "&cThis command is disabled on the server.");
                this.getFileMessageConfiguration().set("brokePlant", "&aYou just broke a plantation.");
                this.getFileMessageConfiguration().set("playerStatistic", "&aYou used the sickle %number% times.");
                this.getFileMessageConfiguration().set("sellHarvest", "&aYou just sell %item% for $%price%.");
                this.getFileMessageConfiguration().set("noPrice", "&cThis plantation is not available for sale.");
                this.savedFiles();
            } catch (IOException error) {
                this.log("&cAn error was detected when creating a document. (messages.yml)");
            }
        }
    }

    private void savedFiles() {
        try {
            this.getFileMessageConfiguration().save(this.getFileMessage());
        } catch (IOException error) {
            this.log("&cAn error was detected while saving a document. (messages.yml)");
        }
    }

    public void reloadMessageFiles() {
        this.fileMessageConfiguration = YamlConfiguration.loadConfiguration(this.getFileMessage());
        InputStream defConfigStream = this.getResource("messages.yml");
        if (defConfigStream != null) {
            this.getFileMessageConfiguration().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[DepthSickle] §f"+ message.replace("&", "§"));
    }

    public static Main getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public ManagerPlayers getPlayersManager() {
        return playersManager;
    }

    public PluginsHelper getPluginsHelper() {
        return pluginsHelper;
    }

    public ActionBar getActionBar() { return actionBar; }

    public boolean isUseOldMethods() { return useOldMethods; }

    public String getVersion() { return version; }

    public File getFileMessage() {
        return fileMessage;
    }

    public FileConfiguration getFileMessageConfiguration() {
        return fileMessageConfiguration;
    }

    public boolean isDrop() {
        return drop;
    }

    public boolean isContent() {
        return content;
    }

    public String getFactions() {
        return factions;
    }

    public String getMessage() { return message; }

    public Action getAction() { return action; }

    public boolean isWorldGuard() {
        return worldGuard;
    }

    public boolean isASkyblock() {
        return aSkyblock;
    }

    public boolean isUSkyblock() {
        return uSkyblock;
    }

    public boolean isMcMMO() {
        return mcMMO;
    }

    public boolean isShopguiplus() {
        return shopguiplus;
    }

    public List<String> getPlant() {
        return plant;
    }

    public List<String> getWorld() {
        return world;
    }

    private List<String> getPlugin() { return plugin; }
}
