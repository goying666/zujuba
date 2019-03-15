package renchaigao.com.zujuba.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;
import renchaigao.com.zujuba.Activity.User.UserCreateTeamListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserJoinTeamListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserOwnGameListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserPlaceListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserPlayGameListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserPlayPlaceListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserSettingActivity;
import renchaigao.com.zujuba.Activity.User.UserWantGameListPageActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;

import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.GENDER_BOY;

public class MineFragment extends BaseFragment {

    private ImageView f_mine_usersetting_image;
    private TextView activity_user_textview_zuju_title, activity_user_textview_title_dianzan,
            activity_user_textview_youxi_title, activity_user_textview_youxi_title_wanguo,
            activity_user_changdi_title, activity_user_changdi_title_playTimes,
            activity_user_textview_zuju_create, activity_user_textview_zuju_join,
            activity_user_textview_youxi_own, activity_user_textview_youxi_play, activity_user_textview_youxi_want,
            activity_user_changdi_title_own, activity_user_changdi_title_play;
    private TextView
            activity_user_textview_zuju_create_1, activity_user_textview_zuju_join_1,
            activity_user_textview_youxi_own_1, activity_user_textview_youxi_play_1, activity_user_textview_youxi_want_1,
            activity_user_changdi_title_own_1, activity_user_changdi_title_play_1,
            activity_user_textview_zuju_create_2, activity_user_textview_zuju_join_2,
            activity_user_textview_youxi_own_2, activity_user_textview_youxi_play_2, activity_user_textview_youxi_want_2,
            activity_user_changdi_title_own_2, activity_user_changdi_title_play_2;
    private TextView userName, userAgeLevel, userCheck, meilizhi, userScore, userSignature;
    private ImageView genderImage, createTeamImage, joinTeamImage, ownGameImage, playGameImage, wantGameImage, createPlaceImage, playPlaceImage;

    private CircleImageView activity_user_image;


    @SuppressLint("CutPasteId")
    @Override

    protected void InitView(View rootView) {

        genderImage = rootView.findViewById(R.id.imageView8);
        userName = rootView.findViewById(R.id.activity_user_name);
        userAgeLevel = rootView.findViewById(R.id.textView40);
        userCheck = rootView.findViewById(R.id.textView42);
        meilizhi = rootView.findViewById(R.id.textView34);
        userScore = rootView.findViewById(R.id.textView41);
        userSignature = rootView.findViewById(R.id.textView35);
        userSignature.setVisibility(View.GONE);
        activity_user_image = rootView.findViewById(R.id.activity_user_image);

        activity_user_textview_zuju_title = rootView.findViewById(R.id.activity_user_textview_zuju_title);
        activity_user_textview_title_dianzan = rootView.findViewById(R.id.activity_user_textview_title_dianzan);
        activity_user_textview_youxi_title = rootView.findViewById(R.id.activity_user_textview_youxi_title);
        activity_user_textview_youxi_title_wanguo = rootView.findViewById(R.id.activity_user_textview_youxi_title_wanguo);
        activity_user_changdi_title = rootView.findViewById(R.id.activity_user_changdi_title);
        activity_user_changdi_title_playTimes = rootView.findViewById(R.id.activity_user_changdi_title_playTimes);

        activity_user_textview_zuju_create_1 = rootView.findViewById(R.id.activity_user_textview_zuju_create).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_textview_zuju_join_1 = rootView.findViewById(R.id.activity_user_textview_zuju_join).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_textview_youxi_own_1 = rootView.findViewById(R.id.activity_user_textview_youxi_own).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_textview_youxi_play_1 = rootView.findViewById(R.id.activity_user_textview_youxi_play).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_textview_youxi_want_1 = rootView.findViewById(R.id.activity_user_textview_youxi_want).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_changdi_title_own_1 = rootView.findViewById(R.id.activity_user_textview_changdi_own).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_changdi_title_play_1 = rootView.findViewById(R.id.activity_user_textview_changdi_play).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_textview_zuju_create_2 = rootView.findViewById(R.id.activity_user_textview_zuju_create).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_textview_zuju_join_2 = rootView.findViewById(R.id.activity_user_textview_zuju_join).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_textview_youxi_own_2 = rootView.findViewById(R.id.activity_user_textview_youxi_own).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_textview_youxi_play_2 = rootView.findViewById(R.id.activity_user_textview_youxi_play).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_textview_youxi_want_2 = rootView.findViewById(R.id.activity_user_textview_youxi_want).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_changdi_title_own_2 = rootView.findViewById(R.id.activity_user_textview_changdi_own).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_changdi_title_play_2 = rootView.findViewById(R.id.activity_user_textview_changdi_play).findViewById(R.id.widget_user_content_TextView_info);
        createTeamImage = rootView.findViewById(R.id.activity_user_textview_zuju_create).findViewById(R.id.widget_user_content_imageview);
        joinTeamImage = rootView.findViewById(R.id.activity_user_textview_zuju_join).findViewById(R.id.widget_user_content_imageview);
        ownGameImage = rootView.findViewById(R.id.activity_user_textview_youxi_own).findViewById(R.id.widget_user_content_imageview);
        playGameImage = rootView.findViewById(R.id.activity_user_textview_youxi_play).findViewById(R.id.widget_user_content_imageview);
        wantGameImage = rootView.findViewById(R.id.activity_user_textview_youxi_want).findViewById(R.id.widget_user_content_imageview);
        createPlaceImage = rootView.findViewById(R.id.activity_user_textview_changdi_own).findViewById(R.id.widget_user_content_imageview);
        playPlaceImage = rootView.findViewById(R.id.activity_user_textview_changdi_play).findViewById(R.id.widget_user_content_imageview);
        createTeamImage.setImageResource(R.drawable.mine_team_create);
        joinTeamImage.setImageResource(R.drawable.mine_team_play);
        ownGameImage.setImageResource(R.drawable.mine_game_own);
        playGameImage.setImageResource(R.drawable.mine_game_play);
        wantGameImage.setImageResource(R.drawable.mine_game_want);
        createPlaceImage.setImageResource(R.drawable.mine_place_create);
        playPlaceImage.setImageResource(R.drawable.mine_place_play);

        rootView.findViewById(R.id.activity_user_textview_zuju_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserCreateTeamListPageActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.activity_user_textview_zuju_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserJoinTeamListPageActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.activity_user_textview_youxi_own).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserOwnGameListPageActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.activity_user_textview_youxi_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPlayGameListPageActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.activity_user_textview_youxi_want).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserWantGameListPageActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.activity_user_textview_changdi_own).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPlaceListPageActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.activity_user_textview_changdi_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPlayPlaceListPageActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void InitData(View rootView) {
        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
        activity_user_textview_zuju_title.setText("组局（1）");
        activity_user_textview_title_dianzan.setText("点赞：5");
        activity_user_textview_youxi_title.setText("游戏（5）");
        activity_user_textview_youxi_title_wanguo.setText("玩过：15盘");
        activity_user_changdi_title.setText("场地（1）");
        activity_user_changdi_title_playTimes.setText("去过：5次");
        activity_user_textview_zuju_create_1.setText("创建的组局");
        activity_user_textview_zuju_join_1.setText("加入的组局");
        activity_user_textview_youxi_own_1.setText("拥有的游戏");
        activity_user_textview_youxi_play_1.setText("玩过的游戏");
        activity_user_textview_youxi_want_1.setText("想要的游戏");
        activity_user_changdi_title_own_1.setText("拥有的场地");
        activity_user_changdi_title_play_1.setText("玩过的场地");
        activity_user_textview_zuju_create_2.setText("0次，累计0人参与");
        activity_user_textview_zuju_join_2.setText("1次，5人点赞");
        activity_user_textview_youxi_own_2.setText("1款，和12位朋友玩过");
        activity_user_textview_youxi_play_2.setText("2款，和19位朋友玩过");
        activity_user_textview_youxi_want_2.setText("2款，和11位朋友玩过");
        activity_user_changdi_title_own_2.setText("0个，0人玩过");
        activity_user_changdi_title_play_2.setText("1个，1900人玩过");
        GlideUrl glideUrl = new GlideUrl(PropertiesConfig.photoUrl + "showimage" + userInfo.getPicPath()
                , new LazyHeaders.Builder()
                .addHeader("Content-Type", "image/jpeg")
                .build());
        Glide.with(mContext)
                .load(glideUrl)
                .dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(activity_user_image);
        genderImage.setImageResource(userInfo.getGender().equals(GENDER_BOY) ? R.drawable.boy : R.drawable.girl);
        userName.setText(userInfo.getNickName());
        userAgeLevel.setText(userInfo.getAgeLevel());
        userCheck.setVisibility(View.GONE);
        meilizhi.setText("10");
        userScore.setText("10");
//        userSignature.setText(userInfo.get);
    }

    @Override
    protected void InitOther(View rootView) {

////        GlideUrl glideUrl = new GlideUrl("http://127.0.0.1:7811/show1image/6188dd9cffc64e2f9b76698be9a51d97/0a46957811ea42c3bfa67f1cfeda663f/photo9.jpg"
//        GlideUrl glideUrl = new GlideUrl(PropertiesConfig.photoUrl + "showimage/6188dd9cffc64e2f9b76698be9a51d97/0a46957811ea42c3bfa67f1cfeda663f/photo9.jpg"
//                , new LazyHeaders.Builder()
//                .addHeader("Content-Type", "image/jpeg")
//                .build());
//        Glide.with(mContext)
//                .load(glideUrl)
////                .load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg")
//                .dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(false)
//                .into(activity_user_image);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }


}
