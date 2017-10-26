package chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;

public class NettyClient {

    public static int callBack () throws Exception{

        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Promise<Object> promise = workerGroup.next().newPromise();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RequestDataEncoder(), new ResponseDataDecoder(), new ClientHandler(promise));
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            Object msg = promise.get();
            System.out.println("The  number if the connected clients: " + msg.toString());
            f.channel().closeFuture().sync();
            return  Integer.parseInt(msg.toString());
        }
        finally {
            //workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

       int ret = callBack();
    }
}