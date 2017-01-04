package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.main.R;
import activity.StudentListTestActivity;

import java.util.ArrayList;
import java.util.List;

import bean.Student;
import widget.ViewPagerIndicator;

/**
 * Created by Administrator on 2016/12/16.
 */

public class AllClassifyFragmentDemo extends Fragment implements View.OnClickListener{

    private Button btn_back;
    private FrameLayout frameLayout;
    private ViewPager vp_pager;
    private ViewPagerIndicator pager_indicator;
    List<Fragment> list = new ArrayList<>();
    private TestFragment test1;
    private TestFragment2 test2;
    private StudentListTestActivity activityDemo;
    private String[] title = {"在线", "下线",};
    FragmentPagerAdapter adapter_pager;
    private List<Student> list1 = new ArrayList<>();
    private List<Student> list2 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_classify_activity,null);
        vp_pager = (ViewPager)view.findViewById(R.id.vp_pager);
        pager_indicator = (ViewPagerIndicator)view.findViewById(R.id.pager_indicator);
        btn_back = (Button)view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
//        frameLayout = activityDemo.fl_contain;
        initData();
        return view;
    }

    private void initData(){
        dealData();
        test1 = new TestFragment();
        test2 = new TestFragment2();
        list.add(test1);
        list.add(test2);

        adapter_pager = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };

        vp_pager.setAdapter(adapter_pager);

        // 将Fragment作为数据源绑定到ViewPager上
        vp_pager.addOnPageChangeListener(new ViewPagerListener());
        vp_pager.setOffscreenPageLimit(1);
        // 为ViewPagerIndicator设置文字显示、点击事件和滚动事件
        pager_indicator.setTitle(title);
        pager_indicator.setTextSize(14);
        pager_indicator.setBackgroundResource(R.color.white);
        pager_indicator.setVisibility(View.VISIBLE);
        pager_indicator.setRound(2);
        pager_indicator.setIndicatorWidth(84);
        pager_indicator.setTextOnClickListener(new ViewPagerIndicator.OnTextClickListener() {
            @Override
            public void textOnClickListener(TextView textView, int position) {
                vp_pager.setCurrentItem(position);
            }
        });
        vp_pager.addOnPageChangeListener(pager_indicator);
        ((TestFragment)list.get(0)).setData(list1);
        ((TestFragment2)list.get(1)).setData(list2);
    }

    private void dealData(){
        Student student1,student2,student3,student4,student5;
        student1 = new Student("拉拉","男","乒乓球");
        student2 = new Student("哈哈","女","足球");
        student3 = new Student("嘿嘿","男","篮球");
        student4 = new Student("露露","女","羽毛球");
        student5 = new Student("妮妮","女","水球");
        list1.add(student1);
        list1.add(student3);
        list1.add(student5);
        list2.add(student2);
        list2.add(student4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
               activityDemo.hideFragmentContain(frameLayout);
                break;
        }
    }

    public class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pager_indicator.scroll(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    ((TestFragment)list.get(position)).setData(list1);
                    break;
                case 1:
                    ((TestFragment2)list.get(position)).setData(list2);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
