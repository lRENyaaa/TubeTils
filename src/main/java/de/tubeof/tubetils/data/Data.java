package de.tubeof.tubetils.data;

public class Data {

    public Data() {}

    private String PREFIX = "[TubeTils] ";
    private Boolean DEBUG = false;

    public void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    public String getPrefix() {
        return PREFIX;
    }

    public void setDebugging(Boolean paramBoolean) {
        DEBUG = paramBoolean;
    }

    public Boolean isDebuggingEnabled() {
        return DEBUG;
    }
}
