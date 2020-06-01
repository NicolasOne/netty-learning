package cn.jsbintask.netty.server.discardserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author jsbintask@gmail.com
 * @date 2019/1/30 9:27
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ctx.pipeline().channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ByteBuf in = (ByteBuf) msg;
            try {
                while (in.isReadable()) {
                    System.out.print((char) in.readByte());
                    System.out.flush();
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });

        System.out.println("DiscardServerHandler.channelRead");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
