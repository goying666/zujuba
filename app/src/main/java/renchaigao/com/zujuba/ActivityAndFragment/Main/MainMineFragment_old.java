//package renchaigao.com.zujuba.ActivityAndFragment.Main;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
//import renchaigao.com.zujuba.ActivityAndFragment.User.MyTeamListActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.User.MyJoinTeamActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.User.UserOwnGameListPageActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.User.MyPlaceActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.User.UserPlayGameListPageActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.User.UserPlayPlaceListPageActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.User.UserWantGameListPageActivity;
//import renchaigao.com.zujuba.R;
//import renchaigao.com.zujuba.util.DataPart.DataUtil;
//
//import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.GENDER_BOY;
//
//public class MainMineFragment_old extends BaseFragment {
//
//    private ImageView f_mine_usersetting_image;
//    private TextView activity_user_textview_zuju_title, activity_user_textview_title_dianzan,
//            activity_user_textview_youxi_title, activity_user_textview_youxi_title_wanguo,
//            activity_user_changdi_title, activity_user_changdi_title_playTimes,
//            activity_user_textview_zuju_create, activity_user_textview_zuju_join,
//            activity_user_textview_youxi_own, activity_user_textview_youxi_play, activity_user_textview_youxi_want,
//            activity_user_changdi_title_own, activity_user_changdi_title_play;
//    private TextView
//            activity_user_textview_zuju_create_1, activity_user_textview_zuju_join_1,
//            activity_user_textview_youxi_own_1, activity_user_textview_youxi_play_1, activity_user_textview_youxi_want_1,
//            activity_user_changdi_title_own_1, activity_user_changdi_title_play_1,
//            activity_user_textview_zuju_create_2, activity_user_textview_zuju_join_2,
//            activity_user_textview_youxi_own_2, activity_user_textview_youxi_play_2, activity_user_textview_youxi_want_2,
//            activity_user_changdi_title_own_2, activity_user_changdi_title_play_2;
//    private TextView userName, userAgeLevel, userCheck, meilizhi, userScore, userSignature;
//    private ImageView genderImage, createTeamImage, joinTeamImage, ownGameImage, playGameImage, wantGameImage, createPlaceImage, playPlaceImage;
//
//    private CircleImageView activity_user_image;
//
//
//    @SuppressLint("CutPasteId")
//    @Override
//    protected void InitView(View rootView) {
//
//        genderImage = (ImageView) rootView.findViewById(R.id.imageView8);
//        userName = (TextView) rootView.findViewById(R.id.userNickName);
//        userAgeLevel = (TextView) rootView.findViewById(R.id.textView40);
//        userCheck = (TextView) rootView.findViewById(R.id.textView42);
//        meilizhi = (TextView) rootView.findViewById(R.id.textView34);
//        userScore = (TextView) rootView.findViewById(R.id.textView41);
//        userSignature = (TextView) rootView.findViewById(R.id.textView35);
//        userSignature.setVisibility(View.GONE);
//        activity_user_image = (CircleImageView) rootView.findViewById(R.id.activity_user_image);
//
//        activity_user_textview_zuju_title = (TextView) rootView.findViewById(R.id.activity_user_textview_zuju_title);
//        activity_user_textview_title_dianzan = (TextView) rootView.findViewById(R.id.activity_user_textview_title_dianzan);
//        activity_user_textview_youxi_title = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_title);
//        activity_user_textview_youxi_title_wanguo = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_title_wanguo);
//        activity_user_changdi_title = (TextView) rootView.findViewById(R.id.activity_user_changdi_title);
//        activity_user_changdi_title_playTimes = (TextView) rootView.findViewById(R.id.activity_user_changdi_title_playTimes);
//
//        activity_user_textview_zuju_create_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_zuju_create).findViewById(R.id.title);
//        activity_user_textview_zuju_join_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_zuju_join).findViewById(R.id.title);
//        activity_user_textview_youxi_own_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_own).findViewById(R.id.title);
//        activity_user_textview_youxi_play_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_play).findViewById(R.id.title);
//        activity_user_textview_youxi_want_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_want).findViewById(R.id.title);
//        activity_user_changdi_title_own_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_changdi_own).findViewById(R.id.title);
//        activity_user_changdi_title_play_1 = (TextView) rootView.findViewById(R.id.activity_user_textview_changdi_play).findViewById(R.id.title);
//        activity_user_textview_zuju_create_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_zuju_create).findViewById(R.id.content1);
//        activity_user_textview_zuju_join_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_zuju_join).findViewById(R.id.content1);
//        activity_user_textview_youxi_own_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_own).findViewById(R.id.content1);
//        activity_user_textview_youxi_play_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_play).findViewById(R.id.content1);
//        activity_user_textview_youxi_want_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_youxi_want).findViewById(R.id.content1);
//        activity_user_changdi_title_own_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_changdi_own).findViewById(R.id.content1);
//        activity_user_changdi_title_play_2 = (TextView) rootView.findViewById(R.id.activity_user_textview_changdi_play).findViewById(R.id.content1);
//        createTeamImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_zuju_create).findViewById(R.id.image);
//        joinTeamImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_zuju_join).findViewById(R.id.image);
//        ownGameImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_youxi_own).findViewById(R.id.image);
//        playGameImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_youxi_play).findViewById(R.id.image);
//        wantGameImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_youxi_want).findViewById(R.id.image);
//        createPlaceImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_changdi_own).findViewById(R.id.image);
//        playPlaceImage = (ImageView) rootView.findViewById(R.id.activity_user_textview_changdi_play).findViewById(R.id.image);
//        createTeamImage.setImageResource(R.drawable.mine_team_create);
//        joinTeamImage.setImageResource(R.drawable.mine_team_play);
//        ownGameImage.setImageResource(R.drawable.mine_game_own);
//        playGameImage.setImageResource(R.drawable.mine_game_play);
//        wantGameImage.setImageResource(R.drawable.mine_game_want);
//        createPlaceImage.setImageResource(R.drawable.mine_place_create);
//        playPlaceImage.setImageResource(R.drawable.mine_place_play);
//
//        rootView.findViewById(R.id.activity_user_textview_zuju_create).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyTeamListActivity.class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_zuju_join).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyJoinTeamActivity.class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_youxi_own).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserOwnGameListPageActivity.class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_youxi_play).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserPlayGameListPageActivity.class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_youxi_want).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserWantGameListPageActivity.class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_changdi_own).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyPlaceActivity.class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_changdi_play).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserPlayPlaceListPageActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    @Override
//    protected void InitData(View rootView) {
//        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
//        activity_user_textview_zuju_title.setText("组局（1）");
//        activity_user_textview_title_dianzan.setText("");
//        activity_user_textview_youxi_title.setText("游戏（5）");
//        activity_user_textview_youxi_title_wanguo.setText("");
//        activity_user_changdi_title.setText("场地（1）");
//        activity_user_changdi_title_playTimes.setText("");
//        activity_user_textview_zuju_create_1.setText("创建的组局");
//        activity_user_textview_zuju_join_1.setText("加入的组局");
//        activity_user_textview_youxi_own_1.setText("拥有的游戏");
//        activity_user_textview_youxi_play_1.setText("玩过的游戏");
//        activity_user_textview_youxi_want_1.setText("想要的游戏");
//        activity_user_changdi_title_own_1.setText("拥有的场地");
//        activity_user_changdi_title_play_1.setText("玩过的场地");
//        activity_user_textview_zuju_create_2.setText("0次");
//        activity_user_textview_zuju_join_2.setText("1次");
//        activity_user_textview_youxi_own_2.setText("1款");
//        activity_user_textview_youxi_play_2.setText("2款");
//        activity_user_textview_youxi_want_2.setText("2款");
//        activity_user_changdi_title_own_2.setText("0个");
//        activity_user_changdi_title_play_2.setText("1个");
////        GlideUrl glideUrl = new GlideUrl(PropertiesConfig.photoUrl + "showimage" + userInfo.getPicPath()
////                , new LazyHeaders.Builder()
////                .addHeader("Content-Type", "image/jpeg")
////                .build());
////        Glide.with(mContext)
////                .load(glideUrl)
////                .dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE)
////                .skipMemoryCache(false)
////                .into(activity_user_image);
//        genderImage.setImageResource(userInfo.getGender().equals(GENDER_BOY) ? R.drawable.boy : R.drawable.girl);
//        userName.setText(userInfo.getNickName());
//        userAgeLevel.setText(userInfo.getAgeLevel());
//        userCheck.setVisibility(View.GONE);
//        meilizhi.setText("10");
//        userScore.setText("10");
////        userSignature.setText(userInfo.get);
//    }
//
//    @Override
//    protected void InitOther(View rootView) {
//
//////        GlideUrl glideUrl = new GlideUrl("http://127.0.0.1:7811/show1image/6188dd9cffc64e2f9b76698be9a51d97/0a46957811ea42c3bfa67f1cfeda663f/photo9.jpg"
////        GlideUrl glideUrl = new GlideUrl(PropertiesConfig.photoUrl + "showimage/6188dd9cffc64e2f9b76698be9a51d97/0a46957811ea42c3bfa67f1cfeda663f/photo9.jpg"
////                , new LazyHeaders.Builder()
////                .addHeader("Content-Type", "image/jpeg")
////                .build());
////        Glide.with(mContext)
////                .load(glideUrl)
//////                .load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg")
////                .dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE)
////                .skipMemoryCache(false)
////                .into(activity_user_image);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_mine;
//    }
//
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//
//        }
//    };
////    private void sendAddressToService() {
////        addSubscribe(
////                RetrofitServiceManager.getInstance().creat(UserApiService.class)
////                        .UpdateUserInfo(USER_UPDATE_INFO_CLASS_ADDRESS
////                                , userId
////                                , token
////                                , JSONObject.parseObject(JSONObject.toJSONString(userInfo.getAddressInfo())))
////                        .subscribeOn(Schedulers.io())
////                        .observeOn(AndroidSchedulers.mainThread())
////                        .subscribeWith(new BaseObserver<ResponseEntity>(this) {
////
////                            @Override
////                            protected void onSuccess(ResponseEntity responseEntity) {
////
////                            }
////
////                            @Override
////                            public void onNext(ResponseEntity value) {
////                                try {
////                                    JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
////                                    int code = Integer.valueOf(responseJson.get("code").toString());
////                                    JSONObject responseJsonData;
////                                    switch (code) {
////                                        case RespCodeNumber.SUCCESS://
////                                            responseJsonData = responseJson.getJSONObject("data");
////                                            String userInfoString = JSONObject.toJSONString(responseJsonData);
////                                            DataUtil.SaveUserInfoData(MainActivity.this, userInfoString);
////                                            Message msg = new Message();
////                                            msg.obj = "地址更新成功";
////                                            // 把消息发送到主线程，在主线程里现实Toast
////                                            handler.sendMessage(msg);
////                                            break;
////                                    }
////                                } catch (Exception e) {
////                                    Log.e(TAG, e.toString());
////                                }
////                            }
////
////                            @Override
////                            public void onError(Throwable e) {
////                            }
////
////                            @Override
////                            public void onComplete() {
////                                Log.e(TAG, "onComplete:");
////                            }
////                        }));
////    }
//}
