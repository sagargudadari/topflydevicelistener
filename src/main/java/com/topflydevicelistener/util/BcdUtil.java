package com.topflydevicelistener.util;

import org.jboss.netty.buffer.ChannelBuffer;

public final class BcdUtil {

    private BcdUtil() {
    }

    public static int readInteger(ChannelBuffer buf, int digits) {
        int result = 0;

        for (int i = 0; i < digits / 2; i++) {
            int b = buf.readUnsignedByte();
            result *= 10;
            result += b >>> 4;
            result *= 10;
            result += b & 0x0f;
        }

        if (digits % 2 != 0) {
            int b = buf.getUnsignedByte(buf.readerIndex());
            result *= 10;
            result += b >>> 4;
        }

        return result;
    }

    public static double readCoordinate(ChannelBuffer buf) {
        int b1 = buf.readUnsignedByte();
        int b2 = buf.readUnsignedByte();
        int b3 = buf.readUnsignedByte();
        int b4 = buf.readUnsignedByte();

        double value = (b2 & 0xf) * 10 + (b3 >> 4);
        value += (((b3 & 0xf) * 10 + (b4 >> 4)) * 10 + (b4 & 0xf)) / 1000.0;
        value /= 60;
        value += ((b1 >> 4 & 0x7) * 10 + (b1 & 0xf)) * 10 + (b2 >> 4);

        if ((b1 & 0x80) != 0) {
            value = -value;
        }

        return value;
    }

}
