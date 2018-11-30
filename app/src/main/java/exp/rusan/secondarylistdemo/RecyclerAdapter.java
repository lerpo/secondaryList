package exp.rusan.secondarylistdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rusan on 2017/5/15.
 */

public class RecyclerAdapter extends SecondaryListAdapter<RecyclerAdapter.GroupItemViewHolder, RecyclerAdapter.SubItemViewHolder> {


    private Context context;

    private List<DataTree<String, String>> dts = new ArrayList<>();
    private Map<Integer,GroupItemViewHolder> holderMap = new HashMap<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, final int groupItemIndex) {

        GroupItemViewHolder groupHolder = ((GroupItemViewHolder) holder);
        groupHolder.tvGroup.setText(dts.get(groupItemIndex).getGroupItem());
        groupHolder.actionSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSubItem(groupItemIndex);
            }
        });
        YoYo.with(Techniques.SlideInRight).duration(700)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(groupHolder.parentLayout);
        holderMap.put(groupItemIndex,groupHolder);

    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {

        ((SubItemViewHolder) holder).tvSub.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex));

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

//        Toast.makeText(context, "group item " + String.valueOf(groupItemIndex) + " is expand " +
//                String.valueOf(isExpand), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {

        String subValue = dts.get(groupItemIndex).getSubItems().get(subItemIndex);
        dts.get(groupItemIndex).setGroupItem(subValue);
        notifyItemRangeChanged(groupItemIndex,1);
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroup;
        Button actionSub;
        LinearLayout parentLayout;
        LinearLayout parentLayout2;
        int tag;
        public GroupItemViewHolder(View itemView) {
            super(itemView);

           tvGroup = (TextView) itemView.findViewById(R.id.tv);
           actionSub = (Button) itemView.findViewById(R.id.action_sub);
           parentLayout = (LinearLayout) itemView.findViewById(R.id.layout_parent);
           parentLayout2 = (LinearLayout) itemView.findViewById(R.id.layout_parent2);

        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvSub;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            tvSub = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public Map<Integer, GroupItemViewHolder> getHolderMap() {
        return holderMap;
    }
}

