package com.bright.apollo.common.rokid;

import java.io.Serializable;

public class RokidExeAction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String property;

    private String name;

    private String value;
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{property=" + property + ", name=" + name + ", value=" + value + "}";
    }
}
