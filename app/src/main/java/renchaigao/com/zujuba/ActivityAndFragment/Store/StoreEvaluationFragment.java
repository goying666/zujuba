package renchaigao.com.zujuba.ActivityAndFragment.Store;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;


public class StoreEvaluationFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private void reloadAdapter() {
//        addSubscribe(RetrofitServiceManager.getInstance().creat(ClubApiService.class)
//                .GetMyAllClub(userInfo.getId(), userInfo.getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
//                    @Override
//                    public void onNext(ResponseEntity value) {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            Message msg = new Message();
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//                            switch (code) {
//                                case RespCodeNumber.CLUB_UPDATE_SUCCESS: //在数据库中更新用户数据出错；
//                                    msg.arg1 = RespCodeNumber.SUCCESS;
//                                    displayTipCardBeanList.clear();
//                                    for (Object m : responseJsonData) {
//                                        displayTipCardBeanList.add(JSONObject.parseObject(JSONObject.toJSONString(m), CardClubFragmentTipBean.class));
//                                    }
//                                    if (displayTipCardBeanList.size() > 0)
//                                        handler.sendMessage(msg);
//                                    break;
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//
//                    }
//
//                    @Override
//                    protected void onSuccess(ResponseEntity responseEntity) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        Log.e(TAG, "onError");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e(TAG, "onComplete");
//                    }
//                }));
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {

    }

    @Override
    protected void InitView(View rootView) {

    }

    @Override
    protected void InitData(View rootView) {

        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
        userId = userInfo.getId();
        token = userInfo.getToken();
        storeId = getActivity().getIntent().getStringExtra("placeId");
    }

    private String userId, token, storeId;
    private int lastTime = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void InitOther(View rootView) {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_evaluation;
    }
}
