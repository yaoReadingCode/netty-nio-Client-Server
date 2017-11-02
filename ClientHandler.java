package chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;

import java.util.function.IntConsumer;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public final IntConsumer update;
    public ClientHandler(IntConsumer update) {
        this.update = update;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RequestData msg = new RequestData();
        msg.setIntValue(123);
        msg.setStringValue("all work and no play makes jack a dull boy");
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        update.accept(Integer.parseInt(msg.toString()));
    }
}