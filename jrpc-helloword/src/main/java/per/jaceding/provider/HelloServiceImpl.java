package per.jaceding.provider;

import lombok.extern.slf4j.Slf4j;
import per.jaceding.common.HelloService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 打招呼实现类
 *
 * @author jaceding
 * @date 2020/6/14
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String say(String msg) {
        log.info("收到消息：{}", msg);
        String content = "hello, i am server, time:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("回复消息：{}", content);
        return content;
    }
}
