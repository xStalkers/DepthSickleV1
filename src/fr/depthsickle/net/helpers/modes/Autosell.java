package fr.depthsickle.net.helpers.modes;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.actions.ActionBar;
import fr.depthsickle.net.helpers.material.Items;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Autosell {

    public void economySeed(Player player, double price, int number, Block block, Material material, Material seed, int data) {
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

            if (priceShop == -1.0) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                return;
            }

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + priceShop));
            }

            block.setType(seed);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        double sold = (price * number);

        if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
            this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + sold));
        }

        Main.getEconomy().depositPlayer(player, sold);
        block.setType(seed);
    }

    public void economyID(Player player, double price, int number, Block block, Material material, Material seed, int id, int data) {
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

            if (priceShop == -1.0) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                return;
            }

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + priceShop));
            }

            block.setTypeId(id);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        double sold = (price * number);

        if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
            this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + sold));
        }

        Main.getEconomy().depositPlayer(player, sold);
        block.setTypeId(id);
    }

    public void economyBlock(Player player, double price, int number, Block block, Material material) {
        if (Main.getInstance().isMcMMO()) {
            if (ExperienceAPI.isValidSkillType(SkillType.HERBALISM.toString())) {
                ExperienceAPI.addRawXP(player, SkillType.HERBALISM.toString(), Main.getInstance().getConfig().getInt("Hook.McMMO."+ block.getType()));
            }
        }

        if (Main.getInstance().isShopguiplus() && Main.getInstance().getConfig().getBoolean("Hook.Economy.ShopGUIPlus")) {
            double priceShop = ShopGuiPlusApi.getItemStackPriceSell(player, new Items(material).create()) * number;

            if (priceShop == -1.0) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noPrice").replace("&", "§"));
                return;
            }

            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + priceShop));
            }

            block.setType(Material.AIR);
            Main.getEconomy().depositPlayer(player, priceShop);
            return;
        }

        double sold = (price * number);

        if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
            this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropSale").replace("&", "§").replace("%item%", "" + material).replace("%price%", "" + sold));
        }

        Main.getEconomy().depositPlayer(player, sold);
        block.setType(Material.AIR);
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
