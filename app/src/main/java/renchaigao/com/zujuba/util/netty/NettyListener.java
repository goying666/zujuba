package renchaigao.com.zujuba.util.netty;

public interface NettyListener {

    public final static byte STATUS_CONNECT_SUCCESS = 1;//连接成功

    public final static byte STATUS_CONNECT_CLOSED = 0;//关闭连接

    public final static byte STATUS_CONNECT_ERROR = 0;//连接失败


    /**
     * 当接收到系统消息
     */
    void onMessageResponse(String msg);

    /**
     * 当连接状态发生变化时调用
     */
    public void onServiceStatusConnectChanged(int statusCode);
}