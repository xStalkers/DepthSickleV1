package fr.depthsickle.net.helpers.modes;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.actions.ActionBar;
import fr.depthsickle.net.helpers.material.Items;
import fr.maxlego08.shop.zshop.items.ShopItem;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class Autosell {

    public void economySeed(Player player, double price, int number, Block block, Material material, Material seed, int data) {
        if (!player.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Autosell"))) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
            }
            return;
        }

        if (block.getData() != data) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropData").replace("&", "§"));
            }
            return;
        }

        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Hook.McMMO."+ block.getType()));
            }
        }

        if (Main.getInstance().isShopguiplus() && Main.getInstance().getConfig().getBoolean("Hook.Economy.ShopGUIPlus")) {
            double priceShop = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).create()) * number;

            if (priceShop <= -1.0) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                }
                return;
            }

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(priceShop)));
            }

            block.setType(seed);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        if (Main.getInstance().isZshop() && Main.getInstance().getConfig().getBoolean("Hook.Economy.zShop")) {
            List<ShopItem> shopItem = Main.getItems().getItems(new Items(material).create());
            if (shopItem.size() <= 0) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                }
                return;
            }

            double priceShop = shopItem.get(0).getSellPrice() * number;

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(priceShop)));
            }

            block.setType(seed);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        double sold = (price * number);

        if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
            this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(sold)));
        }

        Main.getEconomy().depositPlayer(player, sold);
        block.setType(seed);
    }

    public void economyID(Player player, double price, int number, Block block, Material material, int id, int data) {
        if (!player.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Autosell"))) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
            }
            return;
        }

        if (block.getData() != data) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropData").replace("&", "§"));
            }
            return;
        }

        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Hook.McMMO."+ block.getType()));
            }
        }

        if (Main.getInstance().isShopguiplus() && Main.getInstance().getConfig().getBoolean("Hook.Economy.ShopGUIPlus")) {
            double priceShop = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).create()) * number;

            if (priceShop <= -1.0) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                }
                return;
            }

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(priceShop)));
            }

            block.setTypeId(id);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        if (Main.getInstance().isZshop() && Main.getInstance().getConfig().getBoolean("Hook.Economy.zShop")) {
            List<ShopItem> shopItem = Main.getItems().getItems(new Items(material).create());
            if (shopItem.size() <= 0) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                }
                return;
            }

            double priceShop = shopItem.get(0).getSellPrice() * number;

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(priceShop)));
            }

            block.setTypeId(id);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        double sold = (price * number);

        if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
            this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(sold)));
        }

        Main.getEconomy().depositPlayer(player, sold);
        block.setTypeId(id);
    }

    public void economyBlock(Player player, double price, int number, Block block, Material material) {
        if (!player.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Autosell"))) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
            }
            return;
        }

        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Hook.McMMO."+ block.getType()));
            }
        }

        if (Main.getInstance().isShopguiplus() && Main.getInstance().getConfig().getBoolean("Hook.Economy.ShopGUIPlus")) {
            double priceShop = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).create()) * number;

            if (priceShop <= -1.0) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                return;
            }


            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", ""+ this.decimal(priceShop)));
            }

            block.setType(Material.AIR);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        if (Main.getInstance().isZshop() && Main.getInstance().getConfig().getBoolean("Hook.Economy.zShop")) {
            List<ShopItem> shopItem = Main.getItems().getItems(new Items(material).create());
            if (shopItem.size() <= 0) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                }
                return;
            }

            double priceShop = shopItem.get(0).getSellPrice() * number;

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(priceShop)));
            }

            block.setType(Material.AIR);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        double sold = (price * number);

        if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
            this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + this.decimal(sold)));
        }

        Main.getEconomy().depositPlayer(player, sold);
        block.setType(Material.AIR);
    }

    private String decimal(Double price) {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return decimalFormat.format(price);
    }

    private void send(Player player, String type, String message) {
        switch (type) {
            case "COMMON":
                player.sendMessage(message);
                break;
            case "ACTIONBAR":
                new ActionBar().send(player, message);
                break;
        }
    }

}
