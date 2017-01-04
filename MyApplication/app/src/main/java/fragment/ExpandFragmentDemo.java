package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.main.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class ExpandFragmentDemo extends Fragment implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener{

    private String[] armTypes = new String[]{
            "WORD", "EXCEL", "EMAIL", "PPT"
    };
    private String[][] arms = new String[][]{
            {"文档编辑", "文档排版", "文档处理", "文档打印"},
            {"表格编辑", "表格排版", "表格处理", "表格打印"},
            {"收发邮件", "管理邮箱", "登录登出", "注册绑定"},
            {"演示编辑", "演示排版", "演示处理", "演示打印"},
    };
    private PullToRefreshExpandableListView pull_expandListView;
    private ExpandableListAdapter adapter;
    private boolean isRefresh = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pullrefreshview,null);
        pull_expandListView = (PullToRefreshExpandableListView)view.findViewById(R.id.pull_expandListView);

        // 设置列表下拉刷新时的加载提示
        pull_expandListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
        pull_expandListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        pull_expandListView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");
        // 设置列表上拉加载时的加载提示
        pull_expandListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
        pull_expandListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        pull_expandListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");
        pull_expandListView.getRefreshableView().setGroupIndicator(null);
        pull_expandListView.getRefreshableView().setDivider(null);
        pull_expandListView.getRefreshableView().setSelector(android.R.color.transparent);
        pull_expandListView.getRefreshableView().setOnChildClickListener(this);          //可以设置二级击监听
        pull_expandListView.getRefreshableView().setOnGroupClickListener(this);
        pull_expandListView.setMode(PullToRefreshBase.Mode.BOTH);     //设置模式，此模式是可以上拉，BOTH是上拉+下拉
        adapter = new ExpandFragmentDemo.ExpandableListAdapter();
        pull_expandListView.getRefreshableView().setAdapter(adapter);// 填充adapter
        onRefresh();
        // 设置展开
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            pull_expandListView.getRefreshableView().expandGroup(i);
        }
        return view;
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(getActivity(), arms[groupPosition][childPosition] + "", Toast.LENGTH_SHORT).show();
        return false;
    }
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        // 设置成true关闭一级标题点击事件
        return true;
    }
    private void onRefresh() {
        pull_expandListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            // 下拉刷新的方法
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                isRefresh = true;
                try {
                    Thread.sleep(100);
                    pull_expandListView.onRefreshComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "下拉刷新", Toast.LENGTH_SHORT).show();
            }

            @Override
            // 上拉加载刷新的方法
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                isRefresh = false;
                try {
                    Thread.sleep(100);
                    pull_expandListView.onRefreshComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "上拉加载", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 自定义可扩展ExpandableList适配器
     */
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        public ExpandableListAdapter() {
        }

        /**
         * 获取一级标签总数
         */
        @Override
        public int getGroupCount() {
            return armTypes.length;
        }

        /**
         * 获取一级标签内容
         */
        @Override
        public Object getGroup(int groupPosition) {
            return armTypes[groupPosition];
        }

        /**
         * 获取一级标签里的Id
         *
         * @param groupPosition
         * @return
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取一级标签下二级标签的总数
         *
         * @param groupPosition
         * @return
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return arms[groupPosition].length;
        }

        /**
         * 获取一级标签下二级标签的内容
         *
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return arms[groupPosition][childPosition];
        }

        /**
         * 获取二级标签的ID
         *
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         * 指定位置相应的组视图
         *
         * @return
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * 对一级标签进行设置
         *
         * @param groupPosition
         * @param isExpanded
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // 为视图对象指定布局
            convertView = View.inflate(getActivity(),
                    R.layout.expandlist_group, null);

            TextView title_group = (TextView) convertView
                    .findViewById(R.id.title_group);
            title_group.setText(getGroup(groupPosition).toString());

            // 返回一个布局对象
            return convertView;
        }

        /**
         * 对一级标签下的二级标签进行设置
         *
         * @param groupPosition
         * @param childPosition
         * @param isLastChild
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // 为视图对象指定布局
            convertView = View.inflate(getActivity(),
                    R.layout.expandlist_child, null);
            TextView tv_child = (TextView) convertView
                    .findViewById(R.id.tv_child);

            tv_child.setText(getChild(groupPosition, childPosition).toString());
            // 设置子选项点击事件
            pull_expandListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }

        /**
         * 当选择子节点的时候，调用该方法
         *
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // 设置成true表示可以点击
            return true;
        }

    }
}