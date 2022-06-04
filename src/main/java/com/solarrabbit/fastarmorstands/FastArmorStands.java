package com.solarrabbit.fastarmorstands;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.solarrabbit.fastarmorstands.listener.UseArmorStandListener;
import com.solarrabbit.fastarmorstands.permission.FastSwapPermissions;

public class FastArmorStands extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        FastSwapPermissions permissions = new FastSwapPermissions(pluginManager);
        pluginManager.registerEvents(new UseArmorStandListener(permissions), this);
    }
}
