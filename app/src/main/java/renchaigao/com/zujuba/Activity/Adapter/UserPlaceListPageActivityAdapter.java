//package renchaigao.com.zujuba.Activity.Adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//
//import renchaigao.com.zujuba.Activity.Place.UserPlaceManagerActivity;
//import renchaigao.com.zujuba.Activity.PlaceListActivity;
//import renchaigao.com.zujuba.R;
//import renchaigao.com.zujuba.util.PropertiesConfig;
//
///**
// * Created by Administrator on 2018/8/7/007.
// */
//
//public class UserPlaceListPageActivityAdapter extends RecyclerView.Adapter<UserPlaceListPageActivityAdapter.ViewHolder> {
//    final static String TAG = "UserPlaceListPageActivityAdapter";
//    private ArrayList<JSONObject> mJsonObject;
//    private Context mContext;
//
//    public UserPlaceListPageActivityAdapter(Context context) {
//        this.mContext = context;
//    }
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_place_list_page, parent, false);
//        return new ViewHolder(view);
//    }
//
//    public void updateResults(ArrayList<JSONObject> jsonObjectArrayList) {
//        this.mJsonObject = jsonObjectArrayList;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        JSONObject placeInfo = mJsonObject.get(position);
//
//        Glide.with(mContext)
//                .load(PropertiesConfig.photoUrl + placeInfo.get("imageurl"))
//                .dontAnimate()
//                .skipMemoryCache(false)
//                .into(holder.place_image);
//        holder.place_name.setText(placeInfo.getString("name"));
//        holder.place_state.setText(placeInfo.getString("state"));
////        holder.place_note.setText(placeInfo.getString("sysnote"));
//
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, UserPlaceManagerActivity.class);
//                intent.putExtra("storeinfo", JSONObject.toJSONString(mJsonObject.get(position)));
//                mContext.startActivity(intent);
//            }
//        });
//    }
//
//    private PlaceListActivity.OnItemClickListener mOnItemClickListener;//声明接口
//
//    public void setOnItemClickListener(PlaceListActivity.OnItemClickListener onItemClickListener) {
//        mOnItemClickListener = onItemClickListener;
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mJsonObject == null) {
//            return 0;
//        } else
//            return mJsonObject.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private View thisView;
//        private CardView cardView;
//        private TextView place_name,place_state,place_note;
//        private ImageView place_image;
//        public ViewHolder(View view) {
//            super(view);
//            this.thisView = view;
//            this.cardView = (CardView) view;
//            this.place_name = view.findViewById(R.id.textView57);
//            this.place_state = view.findViewById(R.id.textView58);
//            this.place_note = view.findViewById(R.id.textView59);
//            this.place_image = view.findViewById(R.id.imageView11);
//        }
//    }
//}