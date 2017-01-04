package activity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.main.R;

import fragment.AllClassifyFragmentDemo;

/**
 * Created by Administrator on 2016/12/29.
 */

public class StudentListTestActivity extends AppCompatActivity  {

    private ImageView imageView;
    private Button btn_create;
    private Button yes;
    private Button no;
    public FrameLayout fl_contain;
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlistdemo);
        btn_create = (Button)findViewById(R.id.btn_create);
        imageView = (ImageView) findViewById(R.id.iv);
        yes = (Button)findViewById(R.id.yes);
        no = (Button)findViewById(R.id.no);
        fl_contain = (FrameLayout) findViewById(R.id.fl_contain);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageViewFilter(imageView,1);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageViewFilter(imageView,0);
            }
        });
    }
    /**
     * 使用滤镜实现头像置灰
     */
    private void setImageViewFilter(ImageView iv, float f) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(f);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        iv.setColorFilter(filter);
    }
    /**
     * 创建一个fragment,在
     */
    private void createFragment() {
        showFragmentContain();
        // 创建一个事物
        transaction = manager.beginTransaction();
        // 创建一个fragment
        AllClassifyFragmentDemo fragmentDemo = new AllClassifyFragmentDemo();
//        TestFragment fragmentDemo = new TestFragment();
        // 通过事物管理器把fragment添加到右侧的容器中
        transaction.add(R.id.fl_contain,fragmentDemo);
        // 提交事物
        transaction.commit();
    }


    /**
     * 隐藏Fragment
     */
    public void hideFragmentContain(FrameLayout fl_contain){
        fl_contain.setVisibility(View.GONE);
    }

    /**
     * 显示Fragment
     */
    public void showFragmentContain(){
        fl_contain.setVisibility(View.VISIBLE);
    }

}
