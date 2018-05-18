package com.bright.apollo.session;

import com.bright.apollo.bean.Message;

public interface Session {

    public static final int STATUS_CLOSED = -1;
    public static final int STATUS_CONNECTED = 1;
    public static final int STATUS_RANDOM = 3;
    public static final int STATUS_AUTHENTICATED = 4;

    public static enum CloseEnum {
        exception,

        conflict,

        inactive,

        reader_writer_idle
    }

    public void close(CloseEnum closeEnum);

    public boolean isClosed();

    public int getStatus();

    public boolean process(Message<String> msg);

    public boolean process(byte[] bs);
}
