package com.bright.apollo.common.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class MatchRemoteControl extends RcDevice{

    private static final long serialVersionUID = -3332l;

    public HashMap<String, KeyCode> getRcCommand() {
        return rcCommand;
    }

    public void setRcCommand(HashMap<String, KeyCode> rcCommand) {
        this.rcCommand = rcCommand;
    }

    /**
     * 遥控器命令
     */
    @Expose
    @SerializedName("rc_command")

    private HashMap<String,KeyCode> rcCommand;
}
