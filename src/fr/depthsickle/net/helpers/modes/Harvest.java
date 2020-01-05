package fr.depthsickle.net.helpers.modes;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.actions.ActionBar;
import fr.depthsickle.net.helpers.material.Items;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Harvest {

    public void harvestItem(Player player, int number, Block block, Material material, Material seed, int data) {
        if (!player.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Harvest"))) {
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

        if (Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                int currentMaterial = this.countMaterial(player, material);

                if (currentMaterial == 0) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropError").replace("&", "§").replace("%crop%", ""+ material));
                    return;
                }

                if (currentMaterial == 1) {
                    player.getInventory().remove(material);
                    block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));
                    block.setType(seed);

                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                    }
                    return;
                }

                this.removedMaterial(player.getInventory(), material, currentMaterial);
                player.getInventory().addItem(new Items(material, (currentMaterial - 1)).create());

                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));

                block.setType(seed);

                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }

            } else if (!Main.getInstance().isContent()) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }

                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));
                block.setType(seed);
            }
        } else if (!Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                if (this.countSlot(player)) {
                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noInventory").replace("&", "§"));
                    }
                    return;
                }

                int currentMaterial = this.countMaterial(player, material);

                if (currentMaterial == 0) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropError").replace("&", "§").replace("%crop%", ""+ material));
                    return;
                }

                if (currentMaterial == 1) {
                    player.getInventory().remove(material);
                    player.getInventory().addItem(new Items(material, number).create());
                    block.setType(seed);

                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                    }
                    return;
                }

                this.removedMaterial(player.getInventory(), material, currentMaterial);
                player.getInventory().addItem(new Items(material, (currentMaterial - 1) + number).create());

                block.setType(seed);

                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }

            } else if (!Main.getInstance().isContent()) {
                if (this.countSlot(player)) {
                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noInventory").replace("&", "§"));
                    }
                    return;
                }

                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }

                int currentMaterial = this.countMaterial(player, material);

                this.removedMaterial(player.getInventory(), material, currentMaterial);
                player.getInventory().addItem(new Items(material, (currentMaterial + number)).create());

                block.setType(seed);
            }
        }
    }

    public void harvestSeed(Player player, int number, Block block, Material material, Material seed, int data, int id) {
        if (!player.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Harvest"))) {
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

        if (Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                int currentSeedMaterial = this.countMaterial(player, seed);

                if (currentSeedMaterial == 0) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropError").replace("&", "§").replace("%crop%", ""+ seed));
                    return;
                }

                if (currentSeedMaterial == 1) {
                    player.getInventory().remove(seed);
                    block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));
                    block.setTypeId(id);

                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                    }
                    return;
                }

                this.removedMaterial(player.getInventory(), seed, currentSeedMaterial);
                player.getInventory().addItem(new Items(seed, (currentSeedMaterial - 1)).create());

                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));

                block.setTypeId(id);

                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }

            } else if (!Main.getInstance().isContent()) {
                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }

                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));
                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(seed, ThreadLocalRandom.current().nextInt(1, 4)));
                block.setTypeId(id);
            }
        } else if (!Main.getInstance().isDrop()) {
            if (Main.getInstance().isContent()) {
                if (this.countSlot(player)) {
                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noInventory").replace("&", "§"));
                    }
                    return;
                }

                int currentMaterial = this.countMaterial(player, material);
                int currentSeedMaterial = this.countMaterial(player, seed);

                if (currentSeedMaterial == 0) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropError").replace("&", "§").replace("%crop%", ""+ seed));
                    return;
                }

                if (currentSeedMaterial == 1) {
                    player.getInventory().remove(seed);
                    player.getInventory().addItem(new Items(seed, ThreadLocalRandom.current().nextInt(1, 4)).create());
                    block.setTypeId(id);

                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                    }
                    return;
                }

                this.removedMaterial(player.getInventory(), material, currentMaterial);
                player.getInventory().addItem(new Items(material, (currentMaterial + number)).create());

                this.removedMaterial(player.getInventory(), seed, currentSeedMaterial);
                player.getInventory().addItem(new Items(seed, (currentSeedMaterial - 1)).create());

                block.setTypeId(id);

            } else if (!Main.getInstance().isContent()) {
                if (this.countSlot(player)) {
                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noInventory").replace("&", "§"));
                    }
                    return;
                }

                int currentMaterial = this.countMaterial(player, material);
                int currentSeedMaterial = this.countMaterial(player, seed);

                if (currentSeedMaterial == 0) {
                    this.removedMaterial(player.getInventory(), material, currentMaterial);
                    player.getInventory().addItem(new Items(material, (currentMaterial + number)).create());

                    player.getInventory().addItem(new Items(seed, 1).create());
                    block.setTypeId(id);

                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                    }
                    return;
                }

                if (currentSeedMaterial == 1) {
                    this.removedMaterial(player.getInventory(), material, currentMaterial);
                    player.getInventory().addItem(new Items(material, (currentMaterial + number)).create());

                    player.getInventory().remove(seed);
                    player.getInventory().addItem(new Items(seed, ThreadLocalRandom.current().nextInt(1, 4)).create());
                    block.setTypeId(id);

                    if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                        this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                    }
                    return;
                }

                this.removedMaterial(player.getInventory(), material, currentMaterial);
                player.getInventory().addItem(new Items(material, (currentMaterial + number)).create());

                this.removedMaterial(player.getInventory(), seed, currentSeedMaterial);
                player.getInventory().addItem(new Items(seed, (currentSeedMaterial + ThreadLocalRandom.current().nextInt(1, 4))).create());
                block.setTypeId(id);

                if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                    this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
                }
            }
        }
    }

    public void harvestBlock(Player player, int number, Block block, Material material) {
        if (!player.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Harvest"))) {
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

        if (Main.getInstance().isDrop()) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
            }

            switch (block.getType()) {
                case MELON_BLOCK:
                case PUMPKIN:
                    block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(material, number));
                    block.setType(Material.AIR);
                    break;
            }


        } else if (!Main.getInstance().isDrop()) {
            if (Main.getInstance().getAccountManager().getAccount().get(player.getName()).isToggle()) {
                this.send(player, Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("cropTake").replace("&", "§").replace("%numbre%", ""+ number).replace("%crop%", ""+ material));
            }

            switch (block.getType()) {
                case MELON_BLOCK:
                case PUMPKIN:
                    player.getInventory().addItem(new Items(material, number).create());
                    block.setType(Material.AIR);
                    break;
            }

        }
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

    private int countMaterial(Player player, Material material) {
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

    private boolean countSlot(Player player) {
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

    private int removedMaterial(Inventory inventory, Material material, int amount) {
        if(material == null || inventory == null)
            return -1;
        if (amount <= 0)
            return -1;

        if (amount == Integer.MAX_VALUE) {
            inventory.remove(material);
            return 0;
        }

        HashMap<Integer,ItemStack> retVal = inventory.removeItem(new ItemStack(material,amount));

        int notRemoved = 0;
        for(ItemStack item: retVal.values()) {
            notRemoved += item.getAmount();
        }

        return notRemoved;
    }

}
