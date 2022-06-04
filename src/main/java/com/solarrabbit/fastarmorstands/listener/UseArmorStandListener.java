package com.solarrabbit.fastarmorstands.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.solarrabbit.fastarmorstands.event.MockPlayerArmorStandManipulateEvent;
import com.solarrabbit.fastarmorstands.permission.FastSwapPermissions;

public class UseArmorStandListener implements Listener {
    private final FastSwapPermissions permissions;

    public UseArmorStandListener(FastSwapPermissions permissions) {
        this.permissions = permissions;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorStandUse(PlayerInteractAtEntityEvent evt) {
        if (evt.isCancelled())
            return;
        Entity entity = evt.getRightClicked();
        if (!(entity instanceof ArmorStand))
            return;
        ArmorStand armorStand = (ArmorStand) entity;
        Player player = evt.getPlayer();
        if (player.isSneaking() && permissions.hasPermission(player, armorStand))
            swapEquipments(player, armorStand);
    }

    @EventHandler
    public void onManipulateArmorStand(PlayerArmorStandManipulateEvent evt) {
        if (evt instanceof MockPlayerArmorStandManipulateEvent)
            return;
        if (evt.getPlayer().isSneaking())
            evt.setCancelled(true);
    }

    private void swapEquipments(Player player, ArmorStand armorStand) {
        EntityEquipment armorStandEquipment = armorStand.getEquipment();
        EntityEquipment playerEquipment = player.getEquipment();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            swapEquipment(playerEquipment, armorStandEquipment, slot);

            ItemStack incomingItem = playerEquipment.getItem(slot);
            if (incomingItem != null)
                player.getWorld().playSound(player.getLocation(), getSound(incomingItem.getType()), 1.0f, 1.0f);
        }
        showHandsIfHolding(armorStand);
    }

    private void showHandsIfHolding(ArmorStand armorStand) {
        EntityEquipment armorStandEquipment = armorStand.getEquipment();
        boolean hasItemInHand = !armorStandEquipment.getItemInMainHand().getType().isAir()
                || !armorStandEquipment.getItemInOffHand().getType().isAir();
        armorStand.setArms(hasItemInHand);
    }

    private void swapEquipment(EntityEquipment playerEquipment, EntityEquipment armorStandEquipment,
            EquipmentSlot slot) {
        ItemStack playerItem = playerEquipment.getItem(slot);
        ItemStack armorStandItem = armorStandEquipment.getItem(slot);
        playerEquipment.setItem(slot, armorStandItem);
        armorStandEquipment.setItem(slot, playerItem);
    }

    private Sound getSound(Material material) {
        if (material == Material.TURTLE_HELMET)
            return Sound.ITEM_ARMOR_EQUIP_TURTLE;
        else if (material == Material.ELYTRA)
            return Sound.ITEM_ARMOR_EQUIP_ELYTRA;
        else if (!isArmor(material))
            return Sound.ITEM_ARMOR_EQUIP_GENERIC;

        String name = material.name().toLowerCase();
        if (name.contains("leather"))
            return Sound.ITEM_ARMOR_EQUIP_LEATHER;
        else if (name.contains("iron"))
            return Sound.ITEM_ARMOR_EQUIP_IRON;
        else if (name.contains("chainmail"))
            return Sound.ITEM_ARMOR_EQUIP_CHAIN;
        else if (name.contains("gold"))
            return Sound.ITEM_ARMOR_EQUIP_GOLD;
        else if (name.contains("diamond"))
            return Sound.ITEM_ARMOR_EQUIP_DIAMOND;
        else if (name.contains("netherite"))
            return Sound.ITEM_ARMOR_EQUIP_NETHERITE;
        else
            return null;
    }

    private boolean isArmor(Material material) {
        String name = material.name().toLowerCase();
        String[] armorNames = new String[] { "helmet", "chestplate", "leggings", "boots" };
        for (String armorName : armorNames)
            if (name.contains(armorName))
                return true;
        return false;
    }
}
