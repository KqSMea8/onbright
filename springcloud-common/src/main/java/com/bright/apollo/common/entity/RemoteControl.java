package com.bright.apollo.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RemoteControl extends RcDevice {
    @Expose(serialize = false)
    @SerializedName("rc_command")
    @JsonProperty(value="rc_command")
    private String  rcCommand;
    /**
     * 遥控器命令
     */
    @Expose
    @SerializedName("command")
    @JsonProperty(value="command")
    private Map<String, KeyCode> command;

    public String getRcCommand() {
        return rcCommand;
    }

    public void setRcCommand(String rcCommand) {
        this.rcCommand = rcCommand;
    }

    public Map<String, KeyCode> getCommand() {
        return command;
    }

    public void setCommand(Map<String, KeyCode> command) {
        this.command = command;
    }
}
