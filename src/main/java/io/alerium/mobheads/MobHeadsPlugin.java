package io.alerium.mobheads;

import io.alerium.mobheads.heads.HeadsManager;
import io.alerium.mobheads.utils.Configuration;
import io.samdev.actionutil.ActionUtil;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MobHeadsPlugin extends JavaPlugin {

    @Getter private static MobHeadsPlugin instance;

    @Getter private Economy economy;
    @Getter private ActionUtil actionUtil;
    @Getter private Configuration configuration;

    private HeadsManager headsManager;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Enabling MobHeads...");
        if (!setupEconomy()) {
            getLogger().info("Vault not found.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        actionUtil = ActionUtil.init(this);
        configuration = new Configuration(this, "config");
        headsManager = new HeadsManager(this);
        headsManager.enable();

        getLogger().info("MobHeads enabled!");
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        economy = rsp.getProvider();
        return true;
    }

}
