package com.solarrabbit.fastarmorstands.event;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class MockPlayerArmorStandManipulateEvent extends PlayerArmorStandManipulateEvent {

    public MockPlayerArmorStandManipulateEvent(Player who, ArmorStand clickedEntity, ItemStack playerItem,
            ItemStack armorStandItem, EquipmentSlot slot) {
        super(who, clickedEntity, playerItem, armorStandItem, slot);
    }

}
