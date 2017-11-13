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
        msg.setIntValue(123);
        msg.setStringValue("client message: client connected");
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("the servers message is the following: " +msg);
        update.accept(Integer.parseInt(msg.toString()));
    }

    public void messageReceived(Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    public void sendMessage(String msgToSend) {
        if (ctx != null) {
            ChannelFuture cf = ctx.writeAndFlush(Unpooled.copiedBuffer(msgToSend, CharsetUtil.UTF_8));
            if (!cf.isSuccess()) {
                System.out.println("Send failed to be sent: " + cf.cause());
            }
        } else {
            //ctx not initialized yet. you were too fast. do something here
        }
    }
}