package chat;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    static ArrayList<String> remoteAddr = new ArrayList<String>();
    static final ChannelGroup channels1 =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        channels1.add(ctx.channel());
        for (Channel ch : channels1) {
            System.out.println(ch);
            System.out.print(ch.hashCode());
        }
        System.out.println();
        System.out.println("the size of the channels are: " + channels1.size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        ResponseData responseData1 = new ResponseData();
        ChannelFuture future;

        for (Channel ch : channels1) {
            responseData.setIntValue(channels1.size());
            remoteAddr.add(ch.remoteAddress().toString());
            future = ch.writeAndFlush(responseData);
            //future.addListener(ChannelFutureListener.CLOSE);
            System.out.println("the requested data from the clients are: "+requestData);
            responseData1.setIntValue(requestData.getIntValue());
            future = ch.writeAndFlush(responseData1);
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(remoteAddr);
        remoteAddr.clear();
        remoteAddr.addAll(hs);
        System.out.println(remoteAddr);
    }
}