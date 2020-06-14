package per.jaceding.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty Server
 *
 * @author jaceding
 * @date 2020/6/14
 */
@Slf4j
public class NettyServer {

    /**
     * 启动Server服务
     *
     * @param port 端口
     */
    public static void startServer(int port) {
        log.info("绑定端口:{}", port);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup eventGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, eventGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 设置编码、解码器
                            pipeline.addLast(new StringDecoder(), new StringEncoder());
                            // 设置业务处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                log.info("启动成功");
                channelFuture.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("启动失败", e);
        } finally {
            // 释放资源
            log.info("释放资源");
            bossGroup.shutdownGracefully();
            eventGroup.shutdownGracefully();
        }
    }
}
