package exp.rusan.secondarylistdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<SecondaryListAdapter.DataTree<String, String>> datas = new ArrayList<>();

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeMenuRecyclerView rv = (SwipeMenuRecyclerView) findViewById(R.id.rv);

        final LinearLayoutManager layoutMgr = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutMgr);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new RvDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        final RecyclerAdapter adapter = new RecyclerAdapter(this);

        rv.setLongPressDragEnabled(true);
//        rv.setItemViewSwipeEnabled(true);
        rv.setSwipeMenuCreator(swipeMenuCreator);
        rv.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                menuBridge.closeMenu();
                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();
                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {

                    if(menuPosition == 0) {  //删除操作
                        datas.remove(position);
                        adapter.notifyItemRemoved(position);
//                        adapter.notifyItemRangeRemoved(position,datas.size()-position);
                        adapter.notifyItemRangeChanged(Math.min(position, datas.size()-position), Math.abs(datas.size()-position) +1);
                    }else {
                        Toast.makeText(MainActivity.this, "编辑", Toast.LENGTH_SHORT)
                                .show();

                        int firstPosition = layoutMgr.findFirstVisibleItemPosition();
                        View v = layoutMgr.getChildAt(position - firstPosition).findViewById(R.id.action_sub);
                        YoYo.with(Techniques.FlipInX).duration(700)
                                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(v);

                    }
                }
            }
        });

        rv.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                int fromPosition = srcHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();
                if (fromPosition ==toPosition) {
                    return false;
                }
                Collections.swap(datas, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                adapter.notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition - toPosition) +1);
                return true;
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

            }
        });
        adapter.setData(datas);
        rv.setAdapter(adapter);

    }

    {

        for (int i = 0; i < 500; i++) {
            final int finalI = i;
            datas.add(new SecondaryListAdapter.DataTree<String, String>(String.valueOf(i), new
                    ArrayList<String>() {{
                        add("item"+ finalI +" sub 0");
                        add("item"+ finalI +" sub 1");
                        add("item"+ finalI +" sub 2");
                    }}));
        }

    }
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this).setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(MainActivity.this).setBackground(R.drawable.selector_green)
                        .setText("编辑")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

}
