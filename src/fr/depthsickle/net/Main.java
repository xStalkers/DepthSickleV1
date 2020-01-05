package fr.depthsickle.net;

import com.google.common.base.Charsets;
import fr.depthsickle.net.commands.SickleCommand;
import fr.depthsickle.net.helpers.metrics.Metrics;
import fr.depthsickle.net.listeners.PlayersAccountListener;
import fr.depthsickle.net.listeners.PlayersHarvestListener;
import fr.depthsickle.net.managers.AccountManager;
import fr.maxlego08.shop.zshop.factories.Items;
import fr.maxlego08.shop.zshop.factories.Shop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private static Economy economy;
    private static Shop shop;
    private static Items items;

    private AccountManager accountManager = new AccountManager();

    private boolean worldGuard = false;
    private boolean aSkyblock = false;
    private boolean uSkyblock = false;
    private boolean mcMMO = false;
    private boolean shopguiplus = false;
    private boolean zshop = false;
    private boolean vault = false;

    private boolean drop;
    private boolean content;
    private String message;
    private Action action;
    private String factions;

    private List<String> crop = new ArrayList<>();
    private List<String> world = new ArrayList<>();
    private List<String> plugin = new ArrayList<>();

    private File lang;
    private FileConfiguration langConfiguration;

    private boolean useOldMethods = false;
    private String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.saveDefaultLang();

        this.registersClass();
        this.registersCommands();
        this.registersMetrics();
        this.registersEconomy();
        this.registersOptions();
        this.registersPlugins();
        this.registersZShop();
        this.registersZItems();
        this.registersModules();
        this.registersVersion();

        this.sendReport();
    }

    @Override
    public void onDisable() {
        instance = null;

        this.unregistersModules();
    }

    private void registersClass() {
        Bukkit.getPluginManager().registerEvents(new PlayersAccountListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayersHarvestListener(), this);
    }

    private void registersCommands() {
        this.getCommand("depthsickle").setExecutor(new SickleCommand());
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

    public void registersOptions() {
        this.drop = this.getConfig().getBoolean("Configuration.Drop");
        this.content = this.getConfig().getBoolean("Configuration.Content");

        this.factions = this.getConfig().getString("Hook.Faction.Plugin");
        this.message = this.getConfig().getString("Configuration.Message");

        this.action = Action.valueOf(this.getConfig().getString("Configuration.Action"));

        this.crop = this.getConfig().getStringList("Crop");
        this.world = this.getConfig().getStringList("World");
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

        if (this.getServer().getPluginManager().getPlugin("zShop") != null) {
            this.zshop = true;
            this.getPlugin().add("zShop");
        }


        if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
            this.vault = true;
            this.getPlugin().add("Vault");
        }
    }

    private boolean registersZShop(){
        if (this.isZshop()) {
            try {
                RegisteredServiceProvider<Shop> shopProvider = getServer().getServicesManager().getRegistration(Shop.class);
                if (shopProvider != null) {
                    shop = shopProvider.getProvider();
                }
            } catch (NoClassDefFoundError e) {
                this.log("&cAn error was detected when registered zShop's plugins. [Shop] ("+ e.getMessage() +")");
            }
            return (shop != null);
        }
        return false;
    }

    private boolean registersZItems(){
        if (this.isZshop()) {
            try {
                RegisteredServiceProvider<Items> shopProvider = getServer().getServicesManager().getRegistration(Items.class);
                if (shopProvider != null) {
                    items = shopProvider.getProvider();
                }
            } catch (NoClassDefFoundError e) {
                this.log("&cAn error was detected when registered zShop's plugins. [Items] ("+ e.getMessage() +")");
            }
            return (items != null);
        }
        return false;
    }

    private void registersModules() {
        this.getAccountManager().registersAccounts();
    }

    private void unregistersModules() {
        this.getAccountManager().unregistersAccounts();
    }

    private void saveDefaultLang() {
        this.lang = new File(this.getDataFolder().getPath(), "lang.yml");
        this.langConfiguration = YamlConfiguration.loadConfiguration(this.getLang());

        if (!this.getLang().exists()) {
            try {
                this.getLang().createNewFile();

                this.getLangConfiguration().set("noPermission", "&6[DepthSickle] &fError, you do not have permission.");
                this.getLangConfiguration().set("noGamemode", "&6[DepthSickle] &fError, you must be in survival to use the sickle.");
                this.getLangConfiguration().set("noWorld", "&6[DepthSickle] &fError, you can not use the sickle in this world.");
                this.getLangConfiguration().set("noRegion", "&6[DepthSickle] &fError, this crop does not belong to you, so you can not break it.");
                this.getLangConfiguration().set("noCrop", "&6[DepthSickle] &fError, this block is not a crop allowed to harvest.");
                this.getLangConfiguration().set("noVault", "&6[DepthSickle] &fError, the server does not have an economy plugin compatible with this mode.");
                this.getLangConfiguration().set("pluginReload", "&6[DepthSickle] &fThe configuration files have been successfully reloaded.");
                this.getLangConfiguration().set("noPlayer", "&6[DepthSickle] &fError, the requested player is not online.");
                this.getLangConfiguration().set("disableToggle", "&6[DepthSickle] &fYou have &cdisabled &fthe messages of your sickle.");
                this.getLangConfiguration().set("enableToggle", "&6[DepthSickle] &fYou have &aenabled &fthe messages of your sickle.");
                this.getLangConfiguration().set("giveSender", "&6[DepthSickle] &fYou have just given a sickle to the player &a%player%&f.");
                this.getLangConfiguration().set("giveTarget", "&6[DepthSickle] &fYou have just received a sickle from the player &a%player%&f.");
                this.getLangConfiguration().set("errorMode", "&6[DepthSickle] &fError, the requested mode is not valid. &7(harvest - autosell)");
                this.getLangConfiguration().set("alreadyMode", "&6[DepthSickle] &fYou are already in the mode &a%mode%&f.");
                this.getLangConfiguration().set("selectMode", "&6[DepthSickle] &fYou have just joined the mode &a%mode%&f.");
                this.getLangConfiguration().set("showMode", "&6[DepthSickle] &fYou are in &a%mode% &fmode.");
                this.getLangConfiguration().set("noPrice", "&6[DepthSickle] &fError, this crop is not available for sale.");
                this.getLangConfiguration().set("cropData", "&6[DepthSickle] &fError, this crop has not reached its maximum age.");
                this.getLangConfiguration().set("cropSale", "&6[DepthSickle] &fYou just sell &7%item% &ffor &a$%price%.");
                this.getLangConfiguration().set("errorContent", "&6[DepthSickle] &fError, you do not have enough seed to replant this crop.");
                this.getLangConfiguration().set("errorInventory", "&6[DepthSickle] &fError, your inventory is full.");
                this.getLangConfiguration().set("cropTake", "&6[DepthSickle] &fYou automaticaly collected &ax%numbre% %crop%&f.");
                this.getLangConfiguration().set("cropError", "&6[DepthSickle] &fError, you do not have the crop &a%crop% &fin your inventory.");
                this.getLangConfiguration().set("noInventory", "&6[DepthSickle] &fError, you inventory is full.");
                this.saveLang();
            } catch (IOException error) {
                this.log("&cAn error was detected when creating a document. (lang.yml)");
            }
        }
    }

    private void saveLang() {
        try {
            this.getLangConfiguration().save(this.getLang());
        } catch (IOException error) {
            this.log("&cAn error was detected while saving a document. (lang.yml)");
        }
    }

    public void reloadLang() {
        this.langConfiguration = YamlConfiguration.loadConfiguration(this.getLang());
        InputStream inputStream = this.getResource("lang.yml");
        if (inputStream != null) {
            this.getLangConfiguration().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, Charsets.UTF_8)));
        }
    }

    private void registersVersion() {
        if (this.getVersion().equalsIgnoreCase("v1_8_R1") || this.getVersion().startsWith("v1_7_")) {
            this.useOldMethods = true;
        }
    }

    public void sendReport() {
        this.log("&6*****************************************************************");

        this.log("&ePlugin(s) hook :");
        for (String plugin : this.getPlugin()) {
            this.log(" &8* &f"+ plugin);
        }

        this.log("&ePlant(s) allowed :");
        for (String plant : this.getCrop()) {
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
        this.log(" &8* &7Download www.mc-market.org : &ahttps://www.mc-market.org/resources/13283/");
        this.log(" &8* &7Download www.spigotmc.org : &ahttps://www.spigotmc.org/resources/depthsickle-automate-your-farms.70511/");
        this.registersUpdate();

        this.log("&6*****************************************************************");
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[DepthSickle] §f"+ message.replace("&", "§"));
    }

    public static Main getInstance() { return instance; }

    public static Economy getEconomy() { return economy; }

    public static Shop getShop() { return shop; }

    public static Items getItems() { return items; }

    public AccountManager getAccountManager() { return accountManager; }

    public boolean isWorldGuard() { return worldGuard; }

    public boolean isaSkyblock() { return aSkyblock; }

    public boolean isuSkyblock() { return uSkyblock; }

    public boolean isMcMMO() { return mcMMO; }

    public boolean isShopguiplus() { return shopguiplus; }

    public boolean isZshop() { return zshop; }

    public boolean isVault() { return vault; }

    public boolean isDrop() { return drop; }

    public boolean isContent() { return content; }

    public String getMessage() { return message; }

    public Action getAction() { return action; }

    public String getFactions() { return factions; }

    public List<String> getCrop() { return crop; }

    public List<String> getWorld() { return world; }

    public List<String> getPlugin() { return plugin; }

    public File getLang() { return lang; }

    public FileConfiguration getLangConfiguration() { return langConfiguration; }

    public boolean isUseOldMethods() { return useOldMethods; }

    public String getVersion() { return version; }

}
