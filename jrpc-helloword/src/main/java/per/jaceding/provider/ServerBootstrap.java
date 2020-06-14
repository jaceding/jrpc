package per.jaceding.provider;

import per.jaceding.common.Constants;
import per.jaceding.netty.NettyServer;

/**
 * 服务提供者
 *
 * @author jaceding
 * @date 2020/6/14
 */
public class ServerBootstrap {

    /**
     * 启动服务提供者
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        NettyServer.startServer(Constants.DEFAULT_PORT);
    }
}
