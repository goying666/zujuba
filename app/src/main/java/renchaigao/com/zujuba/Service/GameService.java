package renchaigao.com.zujuba.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import renchaigao.com.zujuba.ActivityAndFragment.Game.lrs.GameLRSMainActivity;
import renchaigao.com.zujuba.util.GameServiceSocketChangeListener;
import renchaigao.com.zujuba.util.LocalReceiver;
import renchaigao.com.zujuba.util.netty.Const;
import renchaigao.com.zujuba.util.netty.NettyClient;
import renchaigao.com.zujuba.util.netty.NettyListener;

@Getter
@Setter
public class GameService extends Service implements NettyListener  {

    private GameServiceBinder gameServiceBinder = new GameServiceBinder();
    public class GameServiceBinder extends Binder{
        public GameService getService(){
            return GameService.this;
        }
    }
    private Callback callback = null;

    public  interface Callback {
        void getData(String s);
    }
    public void setCallBack(Callback callBack){
        this.callback = callBack;
    }

    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    public GameService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSocketTcp();
        localBroadcastManager = LocalBroadcastManager.getInstance(GameService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return gameServiceBinder;
    }


    @Override
    public void onMessageResponse(String ss) {
        Intent intent = new Intent("gameSocket");
        intent.putExtra("ret",ss);
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 当连接状态发生变化时调用
     *
     * @param statusCode
     */
    @Override
    public void onServiceStatusConnectChanged(int statusCode) {

    }

    public static NettyClient nettyClient;

    private void initSocketTcp() {
        nettyClient = new NettyClient(Const.HOST, Const.TCP_PORT);
        if (!nettyClient.getConnectStatus()) {
            nettyClient.setListener(GameService.this);
            nettyClient.connect();
        } else {
            nettyClient.disconnect();
        }
    }
}
