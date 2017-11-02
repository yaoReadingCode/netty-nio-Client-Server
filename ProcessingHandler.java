package chat;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    static final ChannelGroup channels1 =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        channels1.add(ctx.channel());
        for (Channel ch : channels1) {
            System.out.println(ch);
            System.out.print(ch.hashCode());
        }
        System.out.println("the size of the channels are: "+ channels1.size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();

        if(channels1.size() == 2) {
            responseData.setIntValue(channels1.size());
            ChannelFuture future = ctx.writeAndFlush(responseData);
            //future.addListener(ChannelFutureListener.CLOSE);
            System.out.println(requestData);
        }
        else {
            responseData.setIntValue(channels1.size());
            ChannelFuture future = ctx.writeAndFlush(responseData);
            //future.addListener(ChannelFutureListener.CLOSE);
            System.out.println(requestData);
        }
    }
}