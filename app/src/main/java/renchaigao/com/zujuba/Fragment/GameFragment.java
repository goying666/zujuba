package renchaigao.com.zujuba.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import renchaigao.com.zujuba.Activity.GamePart.GameBeginActivity;
import renchaigao.com.zujuba.GamePart.DeepForest.DeepForestGmaeSetUpActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.support.ViewInject;

public class GameFragment extends Fragment {

    private Button fragment_game_button2;
    public Activity mContext;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.mContext = activity;
    }
    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_game, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView( View view){
        fragment_game_button2 = view.findViewById(R.id.fragment_game_button2);
        fragment_game_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), GameBeginActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
