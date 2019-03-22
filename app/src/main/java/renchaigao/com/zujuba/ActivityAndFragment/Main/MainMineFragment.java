package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;
import renchaigao.com.zujuba.ActivityAndFragment.Function.CallUsActivity;
import renchaigao.com.zujuba.ActivityAndFragment.User.Place.MyPlaceActivity;
import renchaigao.com.zujuba.ActivityAndFragment.User.Team.MyTeamActivity;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.ActivityAndFragment.User.UserSettingActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;

public class MainMineFragment extends BaseFragment {

    private ImageView userGender;
    private TextView userNickName, userAgeLivel, userScore, userCheck, userSignature;
    private CircleImageView userPhoto;
    private ConstraintLayout teamPart;
    private ConstraintLayout gamePart;
    private ConstraintLayout placePart;
    private ConstraintLayout fightPart;
    private ConstraintLayout toolsPart;
    private ConstraintLayout setting;
    private ConstraintLayout callus;

    @SuppressLint("CutPasteId")
    @Override
    protected void InitView(View rootView) {

        userNickName = (TextView) rootView.findViewById(R.id.userNickName);
        userAgeLivel = (TextView) rootView.findViewById(R.id.textView40);
        userScore = (TextView) rootView.findViewById(R.id.textView41);
        userSignature = (TextView) rootView.findViewById(R.id.textView35);
        userCheck = (TextView) rootView.findViewById(R.id.textView42);
        userGender = (ImageView) rootView.findViewById(R.id.imageView8);
        userPhoto = (CircleImageView) rootView.findViewById(R.id.activity_user_image);
        /*  team part*/
        teamPart = (ConstraintLayout) rootView.findViewById(R.id.part1);
        ((ImageView) teamPart.findViewById(R.id.image)).setImageResource(R.drawable.mine_team_create);
        ((ImageView) teamPart.findViewById(R.id.icon1)).setImageResource(R.drawable.club_huodong);
        ((ImageView) teamPart.findViewById(R.id.icon2)).setImageResource(R.drawable.club_huodong);
        ((TextView) teamPart.findViewById(R.id.title)).setText("我的组局");
        ((TextView) teamPart.findViewById(R.id.content1)).setText("");
        ((TextView) teamPart.findViewById(R.id.content2)).setText("");
        /*  game part*/
        gamePart = (ConstraintLayout) rootView.findViewById(R.id.part2);
        ((ImageView) gamePart.findViewById(R.id.image)).setImageResource(R.drawable.mine_game_own);
        ((ImageView) gamePart.findViewById(R.id.icon1)).setImageResource(R.drawable.club_huodong);
        ((ImageView) gamePart.findViewById(R.id.icon2)).setImageResource(R.drawable.club_huodong);
        ((TextView) gamePart.findViewById(R.id.title)).setText("我的游戏");
        ((TextView) gamePart.findViewById(R.id.content1)).setText("");
        ((TextView) gamePart.findViewById(R.id.content2)).setText("");
        /*  place part*/
        placePart = (ConstraintLayout) rootView.findViewById(R.id.part3);
        ((ImageView) placePart.findViewById(R.id.image)).setImageResource(R.drawable.mine_place_play);
        ((ImageView) placePart.findViewById(R.id.icon1)).setImageResource(R.drawable.club_huodong);
        ((ImageView) placePart.findViewById(R.id.icon2)).setImageResource(R.drawable.club_huodong);
        ((TextView) placePart.findViewById(R.id.title)).setText("我的场地");
        ((TextView) placePart.findViewById(R.id.content1)).setText("");
        ((TextView) placePart.findViewById(R.id.content2)).setText("");
        /*  fight part*/
        fightPart = (ConstraintLayout) rootView.findViewById(R.id.part4);
        ((ImageView) fightPart.findViewById(R.id.image)).setImageResource(R.drawable.mine_game_play);
        ((ImageView) fightPart.findViewById(R.id.icon1)).setImageResource(R.drawable.club_huodong);
        ((ImageView) fightPart.findViewById(R.id.icon2)).setImageResource(R.drawable.club_huodong);
        ((TextView) fightPart.findViewById(R.id.title)).setText("我的战斗");
        ((TextView) fightPart.findViewById(R.id.content1)).setText("");
        ((TextView) fightPart.findViewById(R.id.content2)).setText("");
        /*  tools part*/
        toolsPart = (ConstraintLayout) rootView.findViewById(R.id.part5);
        ((ImageView) toolsPart.findViewById(R.id.image)).setImageResource(R.drawable.tools);
        ((ImageView) toolsPart.findViewById(R.id.icon1)).setImageResource(R.drawable.club_huodong);
        ((ImageView) toolsPart.findViewById(R.id.icon2)).setImageResource(R.drawable.club_huodong);
        ((TextView) toolsPart.findViewById(R.id.title)).setText("我的道具");
        ((TextView) toolsPart.findViewById(R.id.content1)).setText("");
        ((TextView) toolsPart.findViewById(R.id.content2)).setText("");

        setting = (ConstraintLayout) rootView.findViewById(R.id.fragment_mine_setting);
        callus =  (ConstraintLayout) rootView.findViewById(R.id.fragment_mine_callus);

    }

    @Override
    protected void InitData(View rootView) {
        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
    }

    @Override
    protected void InitOther(View rootView) {
        teamPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyTeamActivity.class);
                startActivity(intent);
            }
        });
        gamePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyTeamActivity.class);
                startActivity(intent);
            }
        });
        placePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPlaceActivity.class);
                startActivity(intent);
            }
        });
        fightPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyTeamActivity.class);
                startActivity(intent);
            }
        });
        toolsPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyTeamActivity.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingActivity.class);
                startActivity(intent);
            }
        });
        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallUsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };
}
