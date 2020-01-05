package fr.depthsickle.net.helpers;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.librairy.material.Items;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import fr.depthsickle.net.managers.AccountManager;

public class PluginsHelper {

    public void harvestWithSeed(Player player, Block block, Material seed, Material drop, int data) {
        if (block.getData() != data) {
            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorData").replace("&", "§"));
            }
            return;
        }

        Main.getInstance().getPlayersManager().getAccount().get(player.getName()).setUsed(Main.getInstance().getPlayersManager().getAccount().get(player.getName()).getUsed() + 1);

        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Plugin.McMMO.Experiences."+ seed));
            }
        }

        if (Main.getInstance().isShopguiplus()) {
            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).getMod().equals("autosell")) {
                this.sellWithSeed(player, block, seed, drop);
                return;
            }
        }

        if (Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                if (!player.getInventory().contains(drop)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorContent").replace("&", "§"));
                    }
                    return;
                }

                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }

                int count = this.count(player, drop);

                if (count <= 1) {
                    player.getInventory().remove(drop);
                    player.getInventory().addItem(new ItemStack(drop, count - 1));
                    block.breakNaturally();
                    block.setType(seed);
                } else if (count > 1) {
                    ItemStack[] arrayOfItemStack;
                    int x1 = (arrayOfItemStack = player.getInventory().getContents()).length;
                    for (int i = 0; i < x1; i++) {
                        ItemStack contents = arrayOfItemStack[i];
                        if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                            ItemStack n = new ItemStack(contents.getType(), contents.getAmount() - 1);
                            player.getInventory().remove(contents);
                            player.getInventory().addItem(n);
                        }
                    }

                    block.breakNaturally();
                    block.setType(seed);
                }
            } else if (!Main.getInstance().isContent()) {
                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }
                block.breakNaturally();
                block.setType(seed);
            }
        } else if (!Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                if (this.inventory(player)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorInventory").replace("&", "§"));
                    }
                    block.breakNaturally();
                    return;
                }

                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }

                int count = this.count(player, drop);

                if (count <= 1) {
                    player.getInventory().remove(drop);
                    player.getInventory().addItem(new ItemStack(drop, count - 1 + ThreadLocalRandom.current().nextInt(2 , 4)));
                    block.breakNaturally();
                    block.setType(seed);
                } else if (count > 1) {
                    ItemStack[] arrayOfItemStack;
                    int x1 = (arrayOfItemStack = player.getInventory().getContents()).length;
                    for (int i = 0; i < x1; i++) {
                        ItemStack contents = arrayOfItemStack[i];
                        if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                            ItemStack n = new ItemStack(contents.getType(), contents.getAmount() - 1  + ThreadLocalRandom.current().nextInt(2 , 4));
                            player.getInventory().remove(contents);
                            player.getInventory().addItem(n);
                        }
                    }

                    block.setType(seed);
                }
            } else if (!Main.getInstance().isContent()) {
                if (!player.getInventory().contains(drop)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorContent").replace("&", "§"));
                    }
                    return;
                }

                if (this.inventory(player)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorInventory").replace("&", "§"));
                    }
                    block.breakNaturally();
                    return;
                }

                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }

                int count = this.count(player, drop);

                if (count <= 1) {
                    player.getInventory().remove(drop);
                    player.getInventory().addItem(new ItemStack(drop, count + ThreadLocalRandom.current().nextInt(2 , 4)));
                    block.setType(seed);
                } else if (count > 1) {
                    ItemStack[] arrayOfItemStack;
                    int x1 = (arrayOfItemStack = player.getInventory().getContents()).length;
                    for (int i = 0; i < x1; i++) {
                        ItemStack contents = arrayOfItemStack[i];
                        if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                            ItemStack n = new ItemStack(contents.getType(), contents.getAmount() + ThreadLocalRandom.current().nextInt(2 , 4));
                            player.getInventory().remove(contents);
                            player.getInventory().addItem(n);
                        }
                    }

                    block.setType(seed);
                }
            }
        }
    }

    public void harvestWithID(Player player, Block block, Material seed, Material drop, int id, int data) {
        if (block.getData() != data) {
            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorData").replace("&", "§"));
            }
            return;
        }


        Main.getInstance().getPlayersManager().getAccount().get(player.getName()).setUsed(Main.getInstance().getPlayersManager().getAccount().get(player.getName()).getUsed() + 1);

        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Plugin.McMMO.Experiences."+ drop));
            }
        }

        if (Main.getInstance().isShopguiplus()) {
            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).getMod().equals("autosell")) {
                this.sellWithID(player, block, id, seed);
                return;
            }
        }

        if (Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                if (!player.getInventory().contains(seed)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorContent").replace("&", "§"));
                    }
                    return;
                }

                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }

                int count = this.count(player, drop);

                if (count <= 1) {
                    player.getInventory().remove(drop);
                    player.getInventory().addItem(new ItemStack(drop, count - 1));
                    block.breakNaturally();
                    block.setTypeId(id);
                } else if (count > 1) {
                    ItemStack[] arrayOfItemStack;
                    int x1 = (arrayOfItemStack = player.getInventory().getContents()).length;
                    for (int i = 0; i < x1; i++) {
                        ItemStack contents = arrayOfItemStack[i];
                        if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                            ItemStack n = new ItemStack(contents.getType(), contents.getAmount() - 1);
                            player.getInventory().remove(contents);
                            player.getInventory().addItem(n);
                        }
                    }

                    block.breakNaturally();
                    block.setTypeId(id);
                }
            } else if (!Main.getInstance().isContent()) {
                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }
                block.breakNaturally();
                block.setTypeId(id);
            }
        } else if (!Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                if (this.inventory(player)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorInventory").replace("&", "§"));
                    }
                    block.breakNaturally();
                    return;
                }

                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }

                int count = this.count(player, drop);

                if (count <= 1) {
                    player.getInventory().remove(drop);
                    player.getInventory().addItem(new ItemStack(seed, count - 1 + 1));
                    block.breakNaturally();
                    block.setTypeId(id);
                } else if (count > 1) {
                    ItemStack[] arrayOfItemStack;
                    int x1 = (arrayOfItemStack = player.getInventory().getContents()).length;
                    for (int i = 0; i < x1; i++) {
                        ItemStack contents = arrayOfItemStack[i];
                        if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                            ItemStack n = new ItemStack(contents.getType(), contents.getAmount() - 1  + 1);
                            player.getInventory().remove(contents);
                            player.getInventory().addItem(n);
                        }
                    }

                    block.setTypeId(id);
                }
            } else if (!Main.getInstance().isContent()) {
                if (!player.getInventory().contains(seed)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorContent").replace("&", "§"));
                    }
                    return;
                }

                if (this.inventory(player)) {
                    if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                        this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorInventory").replace("&", "§"));
                    }
                    block.breakNaturally();
                    return;
                }

                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
                }

                int count = this.count(player, drop);
                if (count <= 1) {
                    player.getInventory().remove(drop);
                    player.getInventory().addItem(new ItemStack(seed, count + 1));
                    block.setTypeId(id);
                } else if (count > 1) {
                    ItemStack[] arrayOfItemStack;
                    int x1 = (arrayOfItemStack = player.getInventory().getContents()).length;
                    for (int i = 0; i < x1; i++) {
                        ItemStack contents = arrayOfItemStack[i];
                        if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                            ItemStack n = new ItemStack(contents.getType(), contents.getAmount() + 1);
                            player.getInventory().remove(contents);
                            player.getInventory().addItem(n);
                        }
                    }

                    block.setTypeId(id);
                }
            }
        }
    }

    public void harvestWithBlock(Player player, Block block, Material drop) {
        Main.getInstance().getPlayersManager().getAccount().get(player.getName()).setUsed(Main.getInstance().getPlayersManager().getAccount().get(player.getName()).getUsed() + 1);

        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Plugin.McMMO.Experiences."+ block.getType()));
            }
        }

        if (Main.getInstance().isShopguiplus()) {
            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).getMod().equals("autosell")) {
                this.sellWithBlock(player, block, drop);
                return;
            }
        }

        if (Main.getInstance().isDrop()) {
            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
            }

            block.breakNaturally();
        } else if (!Main.getInstance().isDrop()) {
            if (this.inventory(player)) {
                if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                    this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorInventory").replace("&", "§"));
                }
                block.breakNaturally();
                return;
            }

            if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
                this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("brokePlant").replace("&", "§"));
            }

            int count = this.count(player, drop);

            switch (block.getType()) {
                case MELON_BLOCK:
                    if (count <= 1) {
                        block.setType(Material.AIR);
                        player.getInventory().remove(drop);
                        player.getInventory().addItem(new ItemStack(drop, count + ThreadLocalRandom.current().nextInt(1, 7)));
                    } else if (count > 1) {
                        ItemStack[] arrayOfItemStack;
                        int x = (arrayOfItemStack = player.getInventory().getContents()).length;
                        for (int i = 0; i < x; i++) {
                            ItemStack contents = arrayOfItemStack[i];
                            if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                                ItemStack n = new ItemStack(contents.getType(), contents.getAmount() + ThreadLocalRandom.current().nextInt(1, 7));
                                player.getInventory().remove(contents);
                                player.getInventory().addItem(n);
                            }
                        }

                        block.setType(Material.AIR);
                    }
                    break;
                case PUMPKIN:
                    if (count <= 1) {
                        block.setType(Material.AIR);
                        player.getInventory().remove(drop);
                        player.getInventory().addItem(new ItemStack(drop, count + 1));
                    } else if (count > 1) {
                        ItemStack[] arrayOfItemStack;
                        int x = (arrayOfItemStack = player.getInventory().getContents()).length;
                        for (int i = 0; i < x; i++) {
                            ItemStack contents = arrayOfItemStack[i];
                            if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == drop)) {
                                ItemStack n = new ItemStack(contents.getType(), contents.getAmount() + 1);
                                player.getInventory().remove(contents);
                                player.getInventory().addItem(n);
                            }
                        }

                        block.setType(Material.AIR);
                    }
                    break;
            }

        }
    }

    public void sellWithSeed(Player player, Block block, Material seed, Material material) {
        double price = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).build());

        if (price == -1.0) {
            this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noPrice").replace("&", "§"));
            return;
        }

        if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
            this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("sellHarvest").replace("&", "§").replace("%item%", ""+ material).replace("%price%", ""+ ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).build())));
        }

        block.setType(seed);
        Main.getEconomy().depositPlayer(player, ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).build()));
    }

    public void sellWithID(Player player, Block block, int id, Material material) {
        double price = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).build());

        if (price == -1.0) {
            this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noPrice").replace("&", "§"));
            return;
        }

        if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
            this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("sellHarvest").replace("&", "§").replace("%item%", ""+ material).replace("%price%", ""+ ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).build())));
        }

        block.setTypeId(id);
        Main.getEconomy().depositPlayer(player, ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).build()));
    }

    public void sellWithBlock(Player player, Block block, Material seed) {
        double price = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(seed).build());

        if (price == -1.0) {
            this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noPrice").replace("&", "§"));
            return;
        }

        if (Main.getInstance().getPlayersManager().getAccount().get(player.getName()).isToggle()) {
            this.message(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("sellHarvest").replace("&", "§").replace("%item%", ""+ block.getType()).replace("%price%", ""+ ShopGuiPlusApi.getItemStackPriceSell(player, new Items(seed).build())));
        }

        block.setType(Material.AIR);
        Main.getEconomy().depositPlayer(player, ShopGuiPlusApi.getItemStackPriceSell(player, new Items(seed).build()));
    }

    public void message(Player player, String type, String message) {
        switch (type) {
            case "MESSAGE":
                player.sendMessage(message);
                break;
            case "ACTIONBAR":
                Main.getInstance().getActionBar().send(player, message);
                break;
        }
    }

    public boolean inventory(Player player) {
        int slot = 0;
        ItemStack[] arrayOfItemStack;
        int x = (arrayOfItemStack = player.getInventory().getContents()).length;
        for (int i = 0; i < x; i++) {
            ItemStack contents = arrayOfItemStack[i];
            if ((contents == null)) {
                slot++;
            }
        }
        return slot == 0;
    }

    public int count(Player player, Material material) {
        int item = 0;
        ItemStack[] arrayOfItemStack;
        int x = (arrayOfItemStack = player.getInventory().getContents()).length;
        for (int i = 0; i < x; i++) {
            ItemStack contents = arrayOfItemStack[i];
            if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == material)) {
                item = item + contents.getAmount();
            }
        }
        return item;
    }

    public void item(Player player) {
        ItemStack item = new ItemStack(Material.matchMaterial(Main.getInstance().getConfig().getString("Material.Item")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getInstance().getConfig().getString("Material.Name").replace("&", "§"));
        List<String> lore = new ArrayList<>();
        for (String line : Main.getInstance().getConfig().getStringList("Material.Lore")) {
            lore.add(line.replace("&", "§"));
        }
        if (Main.getInstance().getConfig().getBoolean("Material.Attribute")) {
            meta.addItemFlags(new ItemFlag[]{ ItemFlag.HIDE_ATTRIBUTES });
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }

}
