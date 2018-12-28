package renchaigao.com.zujuba.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import renchaigao.com.zujuba.Fragment.Adapter.MessageFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.Bean.MessageNoteInfo;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;


public class MessageFragment extends Fragment {


    private String userId;
    private UserInfo userInfo;
    public Context mContext;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MessageFragmentAdapter messageFragmentAdapter;

    private ArrayList<MessageNoteInfo> messageNoteInfoArrayList = new ArrayList<>();
    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = DataUtil.getUserInfoData(mContext);
        userId= userInfo.getId();
    }

    private List<MessageNoteInfo> GetMessageNoteInfos(){
        return LitePal.findAll(MessageNoteInfo.class);
    }
    private List<MessageNoteInfo> InitMessageNoteInfos(){
        List<MessageNoteInfo> messageNoteInfos = LitePal.findAll(MessageNoteInfo.class);
        if (messageNoteInfos.size()>0){
//           本地存储的有数据，排序后显示
            if (messageNoteInfos.size()>1){
                Collections.sort(messageNoteInfos, new Comparator<MessageNoteInfo>() {
                    @Override
                    public int compare(MessageNoteInfo o1, MessageNoteInfo o2) {
                        return (int) (o2.getSendTime() - o1.getSendTime());
                    }
                });
            }
            messageFragmentAdapter.updateResults(new ArrayList<MessageNoteInfo>(messageNoteInfos));
        }
        try {

        }catch (Exception e){

        }
        return messageNoteInfos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        // Inflate the layout for this fragment
//        reloadAdapter();
        setRecyclerView(rootView);
//        获取数据数据
        InitMessageNoteInfos();

        return rootView;
    }


    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }
    /*
    * Deprecated on API 23
    * Use onAttachToContext instead
    */

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        //do something
        this.mContext = context;
    }

    private void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.message_RecyclerView_People);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        messageFragmentAdapter = new MessageFragmentAdapter(mContext);
//        teamFragmentAdapter.setOnItemClickListener(new PlaceListActivity.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                final Intent intent = new Intent(getActivity(), TeamCreateActivity.class);
//                intent.putExtra("address",JSONObject.toJSONString(mStoreInfo.get(position).getAddressInfo()));
//                intent.putExtra("storeInfo",JSONObject.toJSONString(mStoreInfo.get(position)));
//                intent.putExtra("name", mStoreInfo.get(position).getName());
//                setResult(CREATE_TEAM_ADDRESS_STORE, intent);
//            }
//        });
        recyclerView.setAdapter(messageFragmentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }


    private String reloadFlag;

//    @SuppressLint("StaticFieldLeak")
//    public void reloadAdapter() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                reloadFlag = "onPreExecute";
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
////                Log.e(TAG, "doInBackground");
//
//                String path = PropertiesConfig.messageUrl + "get/" + userId;
////                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
//                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(15, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .get()
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e("onFailure", e.toString());
//                        reloadFlag = "doInBackground";
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            String responseJsoStr = responseJson.toJSONString();
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//
//                            Log.e(TAG, "onResponse CODE OUT");
//                            Log.e(TAG, "onResponse CODE is" + code);
//
////                            ArrayList<StoreInfo> mStores = new ArrayList<>();
//                            switch (code) {
//                                case 0: //在数据库中更新用户数据出错；
//                                    ArrayList<TeamInfo> mTeam = new ArrayList();
//                                    for (Object m : responseJsonData) {
//                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
//                                    }
////                                    Log.e("responseJsonData",responseJsonData.toJSONString());
//                                    if (teamFragmentAdapter == null) {
//                                        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
//                                    }
//                                    teamFragmentAdapter.updateResults(mTeam);
//
//                                    Log.e(TAG, "onResponse");
//                                    break;
//                            }
////                            swipeRefreshLayout.setRefreshing(false);
//                        } catch (Exception e) {
//                        }
//                        reloadFlag = "doInBackground";
//                    }
//
//                });
//                while (!reloadFlag.equals("doInBackground")) ;
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Log.e(TAG, "onPostExecute");
//                if (mContext == null)
//                    return;
//                teamFragmentAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }.execute();
//    }
}
