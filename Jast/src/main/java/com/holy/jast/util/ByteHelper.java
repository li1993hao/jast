package com.holy.jast.util;

import io.netty.buffer.ByteBuf;

public class ByteHelper {
	public static byte[] getBytes(ByteBuf	buf){
		byte[] contentByte = new byte[buf.capacity()];
		buf.readBytes(contentByte);
		return contentByte;
	}
}
