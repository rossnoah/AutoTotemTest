package net.vanillaplus.autototemtest;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("totemtest").setExecutor(new AutoTotemTestCommand());

    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }





}
