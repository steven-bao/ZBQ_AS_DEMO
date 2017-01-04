package activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.main.R;

public class ListViewActivityDemo extends AppCompatActivity {

    private LinearLayout mLayout;
    private ScrollView scView;
    private Button mBtn;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollview_1);
        mLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        scView= (ScrollView) this.findViewById(R.id.ScrollView);
        mBtn = (Button) this.findViewById(R.id.Button);
        mBtn.setOnClickListener(mClickListener);// 添加点击事件监听
    }

    //监听返回事件  可以不要
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Button bt = (Button) this.getCurrentFocus();
        int count = mLayout.getChildCount();
        Button bm = (Button) mLayout.getChildAt(count-1);

        if(keyCode==KeyEvent.KEYCODE_DPAD_UP && bt .getId()==R.id.Button){
            bm.requestFocus();
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN && bt .getId()==bm.getId()){
            this.findViewById(R.id.Button).requestFocus();   //取消焦点
            return true;
        }
        return false;
    }
    // Button事件监听,当点击第一个按钮时增加一个button和一个textview

    //这里只是做个增加按钮和数据的
    private Button.OnClickListener mClickListener = new Button.OnClickListener() {

        private int index = 1;

        @Override
        public void onClick(View v) {
            TextView tv= new TextView(ListViewActivityDemo.this);//定义一个TextView
            tv.setText("TextView" + index);//设置TextView的文本信息
            //设置线性布局的属性
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLayout.addView(tv, params);//添加一个TextView控件
            Button button = new Button(ListViewActivityDemo.this);//定义一个Button
            button.setText("Button" + index);//设置Button的文本信息
            button.setId(index++);//id
            mLayout.addView(button, params);//添加一个Button控件
            mHandler.post(mScrollToButton);//传递一个消息进行滚动
        }

    };




    //传递一个消息进行滚动
    private Runnable mScrollToButton = new Runnable() {

        @Override
        public void run() {
            int off = mLayout.getMeasuredHeight() - scView.getHeight();
            if (off > 0) {
                scView.scrollTo(0, off);//改变滚动条的位置
            }
        }
    };

}
