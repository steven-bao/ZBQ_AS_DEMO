package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.main.R;

import java.util.ArrayList;
import java.util.List;

import Utils.MyDecoration;

/**
 * Created by Administrator on 2016/11/28.
 */

public class RecycleViewActivityDemo extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager; // 线性布局
    private GridLayoutManager gridLayoutManager; // 网格布局
    private MyAdapter adapter;
    private Button btn_add;
    private Button btn_del;
    private Button btn_create;

    private TextView tv_title;
    private List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recyclerview);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_create = (Button) findViewById(R.id.btn_create);
        tv_title = (TextView) findViewById(R.id.tv_title);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
//        gridLayoutManager = new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 默认是Vertical,也可以设置HORIZONTAL
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        // 为RecyclerView添加下划线(两种方法，第一种就是自己绘制，第二种就是在Item里加入View充当下划线)
        mRecyclerView.addItemDecoration(new MyDecoration(RecycleViewActivityDemo.this, MyDecoration.HORIZONTAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建数据源并设置Adapter
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item  " + i);
        }
        if (adapter == null) {
            adapter = new MyAdapter(list);
        }
        mRecyclerView.setAdapter(adapter);
        btn_add.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_create.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("isSlideToBottom:", "" + isSlideToBottom(recyclerView));

                // 方法二（判断recyclerView是否滑到底部）
//                if (isSlideToBottom(recyclerView)) {
//                    Toast.makeText(RecycleViewActivity.this,"到底了",Toast.LENGTH_SHORT).show();
//                }
                // 方法三（判断recyclerView是否滑到底部、顶部）
                if (!mRecyclerView.canScrollVertically(1)) {
                    // 表示是否能向上滚动，false表示已经滚动到底部
                    Toast.makeText(RecycleViewActivityDemo.this, "到底了", Toast.LENGTH_SHORT).show();
                }
                if (!mRecyclerView.canScrollVertically(-1)) {
                    //值表示是否能向下滚动，false表示已经滚动到顶部
                    Toast.makeText(RecycleViewActivityDemo.this, "到顶了", Toast.LENGTH_SHORT).show();
                }

                int item_height = getItemHeight(mRecyclerView);
                Log.i("item_height:", "" + item_height);
                int scroll_Y = getScollYDistance(mRecyclerView);
                Log.i("item_height_scroll_Y:", "" + scroll_Y);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 获取屏幕中第一个可见子项
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition <=2) {// 滑动超过前3个Item时，隐藏标题
                    int first_top = recyclerView.getChildAt(0).getTop();
                    Log.i("first_top:", "" + first_top);
                    first_top = Math.abs(first_top);
                    Log.i("item_height_abs:", "" + first_top);
                        tv_title.setTextColor(getResources().getColor(R.color.white));
                }else {
                    tv_title.setTextColor(getResources().getColor(R.color.transparent));
                }
            }
        });
    }

    /**
     * 方法二
     * 判断当前recyclerView是否滑到底部（效果最好）
     * computeVerticalScrollExtent()是当前屏幕显示的区域高度，
     * computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，
     * computeVerticalScrollRange()是整个View控件的高度。
     *
     * @param recyclerView
     * @return
     */
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;

        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    /**
     * 计算出子项的高度
     *
     * @param recyclerView
     * @return
     */
    public static int getItemHeight(RecyclerView recyclerView) {
        int itemHeight = 0;
        View child = null;
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstPos = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastPos = layoutManager.findLastCompletelyVisibleItemPosition();
        child = layoutManager.findViewByPosition(lastPos);
        if (child != null) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            itemHeight = child.getHeight() + params.topMargin + params.bottomMargin;
        }
        return itemHeight;
    }

    /**
     * 获取滑动的距离
     *
     * @param recyclerView
     * @return
     */
    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
//    public static int getLinearScrollY(RecyclerView recyclerView) {
//        int scrollY = 0;
//        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        int headerCildHeight = getHeaderHeight(recyclerView);
//        int firstPos = layoutManager.findFirstVisibleItemPosition();
//        View child = layoutManager.findViewByPosition(firstPos);
//        int itemHeight = getItemHeight(recyclerView);
//        if (child != null) {
//            int firstItemBottom = layoutManager.getDecoratedBottom(child);
//            scrollY = headerCildHeight + itemHeight * firstPos - firstItemBottom;
//            if (scrollY < 0) {
//                scrollY = 0;
//            }
//        }
//        return scrollY;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                adapter.addData(1);
                adapter.upData(list);
                break;
            case R.id.btn_del:
                adapter.removeData(2);
                adapter.upData(list);
                break;
            case R.id.btn_create:
                Intent intent = new Intent(this,StudentListTestActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public  class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> dataList;

        public MyAdapter(List<String> list) {
            this.dataList = list;
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return dataList.size();
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview_item, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.mTextView.setText(dataList.get(position));

            // Item里面的控件点击事件
            viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RecycleViewActivityDemo.this, "拉拉" + position, Toast.LENGTH_SHORT).show();
                }
            });
//            // 相当于Item的点击事件
//            viewHolder.rl_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(RecycleViewActivityDemo.this, "哈哈" + position, Toast.LENGTH_SHORT).show();
//                    CardDialog dialog = new CardDialog(RecycleViewActivityDemo.this);
//                    dialog.setData(position+"","哈哈点击","拉拉关闭");
//                    dialog.show();
//                }
//            });
        }

        /**
         * 刷新RecycleView的方法
         *
         * @param listData
         */
        public void upData(List<String> listData) {
            this.dataList = listData;
            notifyDataSetChanged();
        }

        public void addData(int position) {
            dataList.add(position, "Insert One");
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public RelativeLayout rl_item;

            public ViewHolder(View view) {
                super(view);
                rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
                mTextView = (TextView) view.findViewById(R.id.text);
            }
        }
    }
}
