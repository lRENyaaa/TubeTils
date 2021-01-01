package de.tubeof.tubetils.api.title;

import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class TitleSender {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    public TitleSender(String titleSenderName) {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new TitleSender with name: " + titleSenderName);
    }



}
