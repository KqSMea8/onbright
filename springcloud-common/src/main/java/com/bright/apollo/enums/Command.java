package com.bright.apollo.enums;

public enum Command {
    VERSION("200c"),

    HEARTBEAT("a013"),

    CONTROLPW("a007"),

    OBOXINFO("a001"),

    setStatus("8100"),

    NODESTATUS("a100"),

    SCENE("a00e"),

    CONTROLPWRESET("a00b"),

    SEARCH("0003"),

    SEARCH_RESULT("2003"),

    SENSOR("2500"),

    DRONEHEARTBEAT("2501"),

    NODESET("8004"),

    NODECHANGE("a004"),

    GROUPCHANGE("a006"),

    SETRELEASE("8008"),

    RELEASE("a008"),

    NODERELEASE("a00a"),

    SETCHANNEL("8015"),

    CHANNEL("a015"),

    REMOTERBUTTON("a016"),

    REMOTERCHANNEL("a014"),

    REMOVEOBOX("8012"),

    REMOVEOBOXRESP("a012"),

    ERROR("200f"),
    //get_real_status
    GETSTATUS("2100"),

    UPGRADE("a1"),

    CAMERAHEART("c1"),

    IRUP("99"),//IR上传学习码

    FILTER("000000");

    private String value;

    public String getValue() {
        return value;
    }

    private Command(String v) {
        this.value = v;
    }
}
