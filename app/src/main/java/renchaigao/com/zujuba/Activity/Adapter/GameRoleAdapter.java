package renchaigao.com.zujuba.Activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.renchaigao.zujuba.mongoDB.info.game.MXTWorld.Equip.RoleEquipmentsInfo;

/**
 * Created by Administrator on 2018/8/27/027.
 */

public class GameRoleAdapter extends RecyclerView.Adapter<GameRoleAdapter.ItemHolder> {

    private RoleEquipmentsInfo roleEquipmentsInfo;
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }
}
