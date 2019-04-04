package renchaigao.com.zujuba.ActivityAndFragment.Game.lrs;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.SocketBean.NormalUse;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Main.MainActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.Service.GameService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.LocalReceiver;
import renchaigao.com.zujuba.util.netty.Const;
import renchaigao.com.zujuba.util.netty.NettyClient;
import renchaigao.com.zujuba.util.netty.NettyListener;
import renchaigao.com.zujuba.util.netty.one.SimpleChatClient;

public class GameLRSMainActivity extends BaseActivity {
    private final String TAG = "GameLRSMainActivity.class";
    LinearLayout LinearLayout_playerStatePart;
    LinearLayout LinearLayout_playerPart;
    private Button Button_voteCancle;
    private TextView TextView_laveTimeCenter;

    @Override
    protected void InitView() {

        TextView_laveTimeCenter = (TextView) findViewById(R.id.TextView_laveTimeCenter);
        Button_voteCancle = (Button) findViewById(R.id.Button_voteCancle);
        LayoutInflater inflater = LayoutInflater.from(GameLRSMainActivity.this);
        LinearLayout_playerStatePart = (LinearLayout) findViewById(R.id.LinearLayout_playerStatePart);
        LinearLayout_playerPart = (LinearLayout) findViewById(R.id.LinearLayout_playerPart);
        ConstraintLayout user8 = (ConstraintLayout) inflater.inflate(R.layout.widget_user_12, null)
                .findViewById(R.id.ConstraintLayout_12players);
        LinearLayout_playerPart.addView(user8);

    }

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
//        userId = userInfo.getId();
//        token = userInfo.getToken();
    }

    @Override
    protected void InitOther() {
        Button_voteCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GameService.nettyClient.getConnectStatus()) {//获取连接状态，必须连接才能点。
                    Toast.makeText(GameLRSMainActivity.this, "先连接", Toast.LENGTH_SHORT).show();
                } else {
                    NormalUse normalUse = new NormalUse();
                    normalUse.setUserId("123");
                    String ss = JSONObject.toJSONString(normalUse);
                    GameService.nettyClient.sendMsgToServer(ss, new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {                //4
                                Log.d(TAG, "Write auth successful");
                            } else {
                                Log.d(TAG, "Write auth error");
                            }
                        }
                    });
                }
            }
        });
        SetBroadcast();
//        try {
//            new SimpleChatClient("192.168.199.155", 7802).run();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void UpdateView() {
        TextView_laveTimeCenter.setText(ss);
    }

    private LocalReceiver localReceiver;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    ss = (String) msg.obj;
                    break;
            }
        }
    };

    String ss = null;

    private void SetBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(GameLRSMainActivity.this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("gameSocket");
        localReceiver = new LocalReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                Message msg = new Message();
                msg.obj = intent.getStringExtra("ret");
                ss = intent.getStringExtra("ret");
                msg.arg1 = MSG_UPDATE_VIEW;
                baseHandler.sendMessage(msg);
//                handler.sendMessage(msg);
            }
        };
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

    }

    private GameService.GameServiceBinder gameServiceBinder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_lrsmain;
    }

//    ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            gameServiceBinder = (GameService.GameServiceBinder) service;
//            GameService gameService = gameServiceBinder.getService();
//            gameService.setGameServiceSocketChangeListener(GameLRSMainActivity.this);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    }


}
