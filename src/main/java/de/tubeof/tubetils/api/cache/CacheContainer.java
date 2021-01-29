package de.tubeof.tubetils.api.cache;

import de.tubeof.tubetils.api.cache.events.CacheContainerAddEvent;
import de.tubeof.tubetils.api.cache.events.CacheContainerGetEvent;
import de.tubeof.tubetils.api.cache.events.CacheContainerRegisterTypeEvent;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.HashMap;

@SuppressWarnings("ALL")
public class CacheContainer {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    private String cacheContainerName;

    public CacheContainer(String cacheContainerName) {
        if(data.isDebuggingEnabled()) ccs.sendMessage(TubeTils.getData().getPrefix() + "Created new CacheContainer with name: " + cacheContainerName);
        this.cacheContainerName = cacheContainerName;
    }

    private final HashMap<Class, HashMap<String, Object>> objectHashMap = new HashMap<>();

    public void registerCacheType(Class paramClass) {
        HashMap<String, Object> map = new HashMap<>();

        Bukkit.getPluginManager().callEvent(new CacheContainerRegisterTypeEvent(paramClass));

        objectHashMap.put(paramClass, map);
        if(data.isDebuggingEnabled()) ccs.sendMessage(TubeTils.getData().getPrefix() + "Created new Cache-Type: " + paramClass.getSimpleName() + " [" + cacheContainerName + "]");
    }

    public void add(Class paramClass, String valueName, Object content) {
        Bukkit.getPluginManager().callEvent(new CacheContainerAddEvent());

        objectHashMap.get(paramClass).put(valueName, content);
    }

    public Object get(Class paramClass, String valueName) {
        Bukkit.getPluginManager().callEvent(new CacheContainerGetEvent());

        return objectHashMap.get(paramClass).get(valueName);
    }

}
