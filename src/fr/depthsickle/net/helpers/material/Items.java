package fr.depthsickle.net.helpers.material;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Items {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public Items(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public Items(Material material, int number) {
        this.itemStack = new ItemStack(material, number);
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public Items(Material material, short id) {
        this.itemStack = new ItemStack(material, 1, id);
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public Items(Material material, int number, short id) {
        this.itemStack = new ItemStack(material, number, id);
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public Items name(String displayname) {
        this.getItemMeta().setDisplayName(displayname);
        return this;
    }

    public Items enchantment(Enchantment enchantment, int power) {
        this.getItemMeta().addEnchant(enchantment, power, true);
        return this;
    }

    public Items lore(String line) {
        ArrayList<String> lines;

        if (this.getItemMeta().getLore() == null) {
            lines = new ArrayList<>();
        } else {
            lines = (ArrayList<String>)this.getItemMeta().getLore();
        }

        lines.add(line);
        this.getItemMeta().setLore(lines);
        return this;
    }

    public Items flag(ItemFlag itemFlag) {
        this.getItemMeta().addItemFlags(new ItemFlag[] { itemFlag });
        return this;
    }

    public Items unbreakable() {
        this.getItemMeta().spigot().setUnbreakable(true);
        return this;
    }

    public ItemStack create() {
        this.getItemStack().setItemMeta(this.getItemMeta());
        return this.getItemStack();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

}
