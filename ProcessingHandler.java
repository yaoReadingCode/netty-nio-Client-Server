package chat;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    final ChannelGroup channels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        channels.add(ctx.channel());
        for (Channel ch : channels) {
            System.out.println(ch);
        }
        System.out.println("the size of the channels are: "+ channels.size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();

        if(channels.size() == 1) {
            responseData.setIntValue(1);
            ChannelFuture future = ctx.writeAndFlush(responseData);
            future.addListener(ChannelFutureListener.CLOSE);
            System.out.println(requestData);
        }
        else {
            responseData.setIntValue(0);
            ChannelFuture future = ctx.writeAndFlush(responseData);
            future.addListener(ChannelFutureListener.CLOSE);
            System.out.println(requestData);
        }
    }
}