package per.jaceding.jrpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * 客户端业务处理器
 *
 * @author jaceding
 * @date 2020/6/14
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    /**
     * 上下文
     */
    private ChannelHandlerContext channelHandlerContext;

    /**
     * 调用返回结果
     */
    private String result;

    /**
     * 客户端调用参数
     */
    private String params;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("成功建立连接");
        channelHandlerContext = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 设置服务器返回结果
        result = msg.toString();
        // 唤醒等待线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常:{}", cause);
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 被代理对象调用，发送数据给服务器，发送完之后->wait()->等待(channelRead)唤醒->返回调用结果
     *
     * @return 调用结果
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        // 发送数据
        channelHandlerContext.writeAndFlush(params);
        // 等待
        wait();
        // 返回结果
        return result;
    }

    /**
     * 设置参数
     *
     * @param params 参数
     */
    public void setParams(String params) {
        this.params = params;
    }
}
