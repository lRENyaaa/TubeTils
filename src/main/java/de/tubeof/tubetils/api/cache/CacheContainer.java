package de.tubeof.tubetils.api.cache;

import de.tubeof.tubetils.api.cache.events.CacheContainerAddEvent;
import de.tubeof.tubetils.api.cache.events.CacheContainerGetEvent;
import de.tubeof.tubetils.api.cache.events.CacheContainerRegisterTypeEvent;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.HashMap;

@SuppressWarnings({"rawtypes", "unused"})
public class CacheContainer {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    private final String cacheContainerName;

    /**
     * Creates a new instance
     * @param cacheContainerName Name of instance
     */
    public CacheContainer(String cacheContainerName) {
        if(data.isDebuggingEnabled()) ccs.sendMessage(TubeTils.getData().getPrefix() + "Created new CacheContainer with name: " + cacheContainerName);
        this.cacheContainerName = cacheContainerName;
    }

    private final HashMap<Class, HashMap<String, Object>> objectHashMap = new HashMap<>();

    /**
     * Register a new DataType
     * @param paramClass DataType Class. For example: String.class
     */
    public void registerCacheType(Class paramClass) {
        HashMap<String, Object> map = new HashMap<>();
        objectHashMap.put(paramClass, map);

        Bukkit.getScheduler().runTask(TubeTils.getInstance(), () -> {
            CacheContainerRegisterTypeEvent cacheContainerRegisterTypeEvent = new CacheContainerRegisterTypeEvent(paramClass);
            Bukkit.getPluginManager().callEvent(cacheContainerRegisterTypeEvent);
        });

        if(data.isDebuggingEnabled()) ccs.sendMessage(TubeTils.getData().getPrefix() + "Created new Cache-Type: " + paramClass.getSimpleName() + " [" + cacheContainerName + "]");
    }

    /**
     * Adds a new value to the cache instance
     * @param paramClass The DataType Class
     * @param valueName The name under which the value can be retrieved later
     * @param content The value, which should be cached
     */
    public void add(Class paramClass, String valueName, Object content) {
        objectHashMap.get(paramClass).put(valueName, content);

        Bukkit.getScheduler().runTask(TubeTils.getInstance(), () -> {
            CacheContainerAddEvent cacheContainerAddEvent = new CacheContainerAddEvent();
            Bukkit.getPluginManager().callEvent(cacheContainerAddEvent);
        });
    }

    /**
     * Get a cache value
     * @param paramClass The DataType Class
     * @param valueName The name under which the value was cached
     * @return The cached value
     */
    public Object get(Class paramClass, String valueName) {
        Bukkit.getScheduler().runTask(TubeTils.getInstance(), () -> {
            CacheContainerGetEvent cacheContainerGetEvent = new CacheContainerGetEvent();
            Bukkit.getPluginManager().callEvent(cacheContainerGetEvent);
        });

        return objectHashMap.get(paramClass).get(valueName);
    }

}
