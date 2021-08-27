package com.cjrequena.sample.fooclientservice.configuration;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;
import java.util.Optional;

import static io.netty.util.internal.PlatformDependent.allocateUninitializedArray;
import static java.lang.Math.max;
import static java.nio.charset.Charset.defaultCharset;

public class CustomNettyLogger extends LoggingHandler {

  public CustomNettyLogger(Class<?> clazz) {
    super(clazz);
  }

  @Override
  protected String format(ChannelHandlerContext ctx, String event, Object arg) {
    Optional<ByteBuf> msg = Optional.empty();
    if (arg instanceof DefaultFullHttpRequest) {
      msg = Optional.of(((DefaultFullHttpRequest) arg).content());
    } else if (arg instanceof DefaultHttpContent) {
      msg = Optional.of(((DefaultHttpContent) arg).content());
    }

    if (msg.isPresent()) {
      return decode(msg.get(), msg.get().readerIndex(), msg.get().readableBytes(), defaultCharset());
    } else {
      return super.format(ctx, event, arg);
    }
  }

  private String decode(ByteBuf src, int readerIndex, int len, Charset charset) {
    if (len != 0) {
      byte[] array;
      int offset;
      if (src.hasArray()) {
        array = src.array();
        offset = src.arrayOffset() + readerIndex;
      } else {
        array = allocateUninitializedArray(max(len, 1024));
        offset = 0;
        src.getBytes(readerIndex, array, 0, len);
      }
      return new String(array, offset, len, charset);
    }
    return "";
  }

}
