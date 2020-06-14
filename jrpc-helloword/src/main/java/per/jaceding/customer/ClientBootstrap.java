package per.jaceding.customer;

import lombok.extern.slf4j.Slf4j;
import per.jaceding.common.Constants;
import per.jaceding.common.HelloService;
import per.jaceding.netty.NettyClient;

import java.util.concurrent.TimeUnit;

/**
 * 服务器消费者
 *
 * @author jaceding
 * @date 2020/6/14
 */
@Slf4j
public class ClientBootstrap {

    public static void main(String[] args) throws InterruptedException {
        // 创建消费者
        NettyClient nettyClient = new NettyClient(Constants.DEFAULT_HOSTNAME, Constants.DEFAULT_PORT);
        // 创建代理对象
        HelloService helloService = (HelloService) nettyClient.getBean(HelloService.class);
        // 开始远程调用
        for (; ; ) {
            // 通过代理对象调用服务者提供的方法
            String msg = "你好~~~";
            String result = helloService.say(msg);
            log.info("调用结果：{}", result);
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
