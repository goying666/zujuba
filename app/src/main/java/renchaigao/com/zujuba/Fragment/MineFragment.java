package renchaigao.com.zujuba.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import de.hdodenhof.circleimageview.CircleImageView;
import renchaigao.com.zujuba.Activity.Place.CreateStoreActivity;
import renchaigao.com.zujuba.Activity.Place.UserPlaceListPageActivity;
import renchaigao.com.zujuba.Activity.User.UserSettingActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

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

    private CircleImageView activity_user_image;


    @SuppressLint("CutPasteId")
    @Override
    protected void InitView(View rootView) {
        f_mine_usersetting_image = rootView.findViewById(R.id.f_mine_usersetting_image);
        f_mine_usersetting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingActivity.class);
                getActivity().startActivity(intent);
            }
        });
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

        activity_user_image = rootView.findViewById(R.id.activity_user_image);
//        rootView.findViewById(R.id.activity_user_textview_zuju_create).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),. class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_zuju_join).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),. class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_youxi_own).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),. class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_youxi_play).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),. class);
//                startActivity(intent);
//            }
//        });
//        rootView.findViewById(R.id.activity_user_textview_youxi_want).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),. class);
//                startActivity(intent);
//            }
//        });
        rootView.findViewById(R.id.activity_user_textview_changdi_own).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UserPlaceListPageActivity.class);
                startActivity(intent);
            }
        });
//        rootView.findViewById(R.id.activity_user_textview_changdi_play).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),. class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void InitData(View rootView) {

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
    }

    @Override
    protected void InitOther(View rootView) {

//        GlideUrl glideUrl = new GlideUrl("http://127.0.0.1:7811/show1image/6188dd9cffc64e2f9b76698be9a51d97/0a46957811ea42c3bfa67f1cfeda663f/photo9.jpg"
        GlideUrl glideUrl = new GlideUrl(PropertiesConfig.photoUrl + "showimage/6188dd9cffc64e2f9b76698be9a51d97/0a46957811ea42c3bfa67f1cfeda663f/photo9.jpg"
                , new LazyHeaders.Builder()
                .addHeader("Content-Type","image/jpeg")
                .build());
        Glide.with(mContext)
                .load(glideUrl)
//                .load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg")
                .dontAnimate() .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(activity_user_image);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }


}
