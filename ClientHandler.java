package chat;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

import java.util.function.IntConsumer;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public final IntConsumer update;
    ChannelHandlerContext ctx;

    public ClientHandler(IntConsumer update) {
        this.update = update;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RequestData msg = new RequestData();
        msg.setStringValue("client message: client connected");
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("The message sent from the server " + msg);
        final ResponseData responseData = (ResponseData) msg;
        System.out.println("The message sent from the server " + responseData);
        update.accept(responseData.getIntValue());
        //update.accept(Integer.parseInt(msg.toString()));
    }
}