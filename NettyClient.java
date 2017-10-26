package chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;

public class NettyClient {

    static EventLoopGroup workerGroup = new NioEventLoopGroup();
    static Promise<Object> promise = workerGroup.next().newPromise();

    public static void callBack () throws Exception{

        String host = "localhost";
        int port = 8080;

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
            //f.channel().closeFuture().sync();
        }
        finally {
            //workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

       callBack();
        while (true) {
            Object msg = promise.get();
            System.out.println("The number if the connected clients is not two");
            int ret = Integer.parseInt(msg.toString());
            if (ret == 2){
                break;
            }
        }
       System.out.println("The number if the connected clients is two");
    }
}