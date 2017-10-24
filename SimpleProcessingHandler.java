package chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.EchoServerHandler;

public class SimpleProcessingHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf tmp;

    public final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Logger logger = LoggerFactory.getLogger( EchoServerHandler.class );

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);
        channels.add(ctx.channel());
        if(logger.isDebugEnabled() ) {
            logger.debug("[CHANNEL ACTIVE]");
        }
        if(channels.size() == 1) {
            sendConStatus(ctx.channel());
            ChannelFuture future = ctx.writeAndFlush("true");
            future.addListener(ChannelFutureListener.CLOSE);
        }
        ctx.channel().closeFuture().addListener(f -> logger.debug("[CLOSE]"));
    }

    public void sendConStatus(Channel channel) throws InterruptedException{
        channel.writeAndFlush( Unpooled.copiedBuffer("true", CharsetUtil.UTF_8));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added");
        tmp = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed");
        tmp.release();
        tmp = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        tmp.writeBytes(m);
        m.release();
        if (tmp.readableBytes() >= 4) {
            RequestData requestData = new RequestData();
            requestData.setIntValue(tmp.readInt());
            ResponseData responseData = new ResponseData();
            responseData.setIntValue(requestData.getIntValue() * 2);
            ChannelFuture future = ctx.writeAndFlush(responseData);
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
