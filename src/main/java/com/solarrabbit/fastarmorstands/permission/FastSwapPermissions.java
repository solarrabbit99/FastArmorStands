package com.solarrabbit.fastarmorstands.permission;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import com.solarrabbit.fastarmorstands.event.MockPlayerArmorStandManipulateEvent;

public class FastSwapPermissions {
    private static final String PERMISSION_NAME = "fastarmorstands.swap";
    private final PluginManager pluginManager;
    private final Permission permission;

    public FastSwapPermissions(PluginManager manager) {
        this.pluginManager = manager;
        this.permission = manager.getPermission(PERMISSION_NAME);
    }

    public boolean hasPermission(Player player, ArmorStand armorStand) {
        return hasLocalPermission(player) && hasExternalPermission(player, armorStand);
    }

    private boolean hasLocalPermission(Player player) {
        return player.hasPermission(permission);
    }

    private boolean hasExternalPermission(Player player, ArmorStand armorStand) {
        for (EquipmentSlot slot : EquipmentSlot.values())
            if (!testArmorStandPerms(player, armorStand, slot))
                return false;
        return true;
    }

    private boolean testArmorStandPerms(Player player, ArmorStand armorStand, EquipmentSlot slot) {
        ItemStack playerItem = player.getEquipment().getItem(slot);
        ItemStack armorStandItem = armorStand.getEquipment().getItem(slot);
        PlayerArmorStandManipulateEvent evt = new MockPlayerArmorStandManipulateEvent(player, armorStand, playerItem,
                armorStandItem, slot);
        pluginManager.callEvent(evt);
        return !evt.isCancelled();
    }
}
