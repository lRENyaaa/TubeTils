package de.tubeof.tubetils.main;

import de.tubeof.tubetils.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TubeTils extends JavaPlugin {

    private static final Data data = new Data();
    private static TubeTils instance;

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {
        ccs.sendMessage(TubeTils.getData().getPrefix() + "The Plugin will be activated ...");
        ccs.sendMessage(TubeTils.getData().getPrefix() + "Build-ID: " + TubeTils.getData().getCiBuild());

        setInstances();

        ccs.sendMessage(TubeTils.getData().getPrefix() + "The plugin was successfully activated!");
    }

    @Override
    public void onDisable() {
        ccs.sendMessage(TubeTils.getData().getPrefix() + "The Plugin will be deactivated ...");

        ccs.sendMessage(TubeTils.getData().getPrefix() + "The plugin was successfully deactivated!");
    }

    private void setInstances() {
        ccs.sendMessage(TubeTils.getData().getPrefix() + "Set Instances ...");

        instance = this;

        ccs.sendMessage(TubeTils.getData().getPrefix() + "Setting Instances done!");
    }

    public static TubeTils getInstance() {
        return instance;
    }

    public static Data getData() {
        return data;
    }



    /**
     * TubeTils API Properties for Developer
     */
    @SuppressWarnings("unused")
    public static class Properties {

        private static final Data data = TubeTils.getData();
        private static final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

        public static void setPrefix(String prefix) {
            if(data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Prefix will be changed to: " + prefix);
            data.setPrefix(prefix);
        }

        public static void setDebuggingStatus(Boolean paramBoolean) {
            if(data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Debugging-Status will be changed to: " + paramBoolean);
            data.setDebugging(paramBoolean);
        }

    }
}
