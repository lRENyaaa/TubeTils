package de.tubeof.tubetils.data;

public class Data {

    public Data() {}

    private final String CI_BUILD = "${ci.buildId}";
    private String PREFIX = "[TubeTils] ";
    private Boolean DEBUG = false;

    public void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    public String getPrefix() {
        return PREFIX;
    }

    public String getCiBuild() {
        return CI_BUILD;
    }

    public void setDebugging(Boolean paramBoolean) {
        DEBUG = paramBoolean;
    }

    public Boolean isDebuggingEnabled() {
        return DEBUG;
    }
}
