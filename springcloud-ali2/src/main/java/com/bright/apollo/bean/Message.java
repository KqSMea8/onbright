package com.bright.apollo.bean;

import java.io.Serializable;

public class Message<T> implements Serializable {

    private static final long serialVersionUID = -4083578424981139935L;

    private String header;

    private int dataLength;

    private String decodeData; //data before deocde

    private String cmd;

    private String data; // data after decode

    private byte [] bs;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getLength() {
        return dataLength;
    }

    public void setLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public String getDecodeData() {
        return decodeData;
    }

    public void setDecodeData(String decodeData) {
        this.decodeData = decodeData;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getBs() {
        return bs;
    }

    public void setBs(byte[] bs) {
        this.bs = bs;
    }
}
