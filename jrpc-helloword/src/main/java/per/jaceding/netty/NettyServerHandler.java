package per.jaceding.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import per.jaceding.common.HelloService;
import per.jaceding.provider.HelloServiceImpl;

/**
 * 服务端业务处理器
 *
 * @author jaceding
 * @date 2020/6/14
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端[{}]连接成功", getClientInfo(ctx));
    }

    /**
     * 获取客户端发送的消息，并调用服务
     *
     * @param ctx io.netty.channel.ChannelHandlerContext
     * @param msg java.lang.Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        String result = helloService.say(msg.toString());
        ctx.writeAndFlush(result);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端[{}]断开连接", getClientInfo(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端[{}]发生异常:{}", getClientInfo(ctx), cause);
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 获取客户端信息
     *
     * @param ctx io.netty.channel.ChannelHandlerContext
     * @return 客户端信息
     */
    public String getClientInfo(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }
}
