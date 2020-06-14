package per.jaceding.jrpc.common;

/**
 * 公共接口
 *
 * @author jaceding
 * @date 2020/6/14
 */
public interface HelloService {

    /**
     * 说话
     *
     * @param msg 消息内容
     * @return 回复内容
     */
    String say(String msg);
}
