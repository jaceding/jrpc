package per.jaceding.jrpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

/**
 * Netty Client
 *
 * @author jaceding
 * @date 2020/6/14
 */
@Slf4j
public class NettyClient {

    /**
     * ip地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 自定义线程池
     */
    private static ExecutorService executors = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2,
            0L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * NettyClientHandler
     */
    private static NettyClientHandler nettyClientHandler;

    /**
     * 使用代理模式，获取一个代理对象
     *
     * @param serviceClass 目标对象
     * @return 代理对象
     */
    public Object getBean(final Class<?> serviceClass) {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},
                (proxy, method, args) -> {
                    if (nettyClientHandler == null) {
                        initClient();
                    }
                    // 设置要发给服务端的信息
                    nettyClientHandler.setParams(String.valueOf(args[0]));
                    return executors.submit(nettyClientHandler).get();
                });
    }

    /**
     * 初始化客户端
     */
    private void initClient() {
        nettyClientHandler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder(), new StringEncoder());
                            pipeline.addLast(nettyClientHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            if (future.isSuccess()) {
                log.info("启动成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("连接失败", e);
        }
//        finally {
//            log.info("释放资源");
//            group.shutdownGracefully();
//        }
    }
}
