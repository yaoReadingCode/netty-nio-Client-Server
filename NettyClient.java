package chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    static EventLoopGroup workerGroup = new NioEventLoopGroup();
    static Promise<Object> promise = workerGroup.next().newPromise();
    private static volatile int connectedClients = 0;
    private static final Object lock = new Object() ;

    public static ChannelFuture callBack () throws Exception{

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
                    ch.pipeline().addLast(new RequestDataEncoder(), new ResponseDataDecoder(),
                            new ClientHandler(i -> {
                                synchronized (lock) {
                                    connectedClients = i;
                                    lock.notifyAll();
                                }
                            }));
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            //f.channel().closeFuture().sync();

            return f;
        }
        finally {
            //workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        ChannelFuture ret;
        ClientHandler obj = new ClientHandler(i -> {
            synchronized (lock) {
                connectedClients = i;
                lock.notifyAll();
            }
        });

       ret = callBack();
        int connected = connectedClients;
        if (connected != 2) {
            System.out.println("The number if the connected clients is not two before locking");
            synchronized (lock) {
                while (true) {
                    connected = connectedClients;
                    if (connected == 2)
                        break;
                    System.out.println("The number if the connected clients is not two");
                    lock.wait();
                }
            }
        }
        System.out.println("The number if the connected clients is two: " + connected );
        RequestData msg = new RequestData();
        msg.setStringValue("welcome from the fake client");
        ret.channel().writeAndFlush(msg);
    }
}