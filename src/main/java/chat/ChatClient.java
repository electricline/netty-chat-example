package chat;

import client.TimeClientHandler;
import client.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        new ChatClient("localhost", 8080).run();
    }

    private final String host;
    private final int port;

    public ChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException, IOException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChatClientInitializer());

            // Start client;
            Channel channel = b.connect(host, port).sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while(true) {
                channel.write(in.readLine() + "\r\n");
            }

        } finally {
            workerGroup.shutdownGracefully();
        }

    }


}
