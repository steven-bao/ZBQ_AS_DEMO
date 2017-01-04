package widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.main.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/14.
 */

public class CardDialog extends Dialog implements View.OnClickListener{

    private View contentView;
    private Button btn_click;
    private TextView tv_title;
    private Button btn_close;
    private TextView tv_flag;
    private Context context;
    private boolean is_status;
    private int num_zan = 0;
    int num = 1;
    Timer time = new Timer();
    public CardDialog(final Context context, int defStyle) {
        super(context, defStyle);
        this.context = context;
        contentView = getLayoutInflater().inflate(
                R.layout.dialog, null);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_flag = (TextView) contentView.findViewById(R.id.tv_flag);
        btn_click = (Button) contentView.findViewById(R.id.btn_click);
        btn_close = (Button) contentView.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_status){
                    btn_click.setText("发送");
                    is_status = false;
                }else {
                    btn_click.setText("接收");
                    is_status = true;
                }
//                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                tv_flag.setVisibility(View.VISIBLE);
                tv_flag.setText("+"+num);
                textAnimation();
                num_zan++;
                btn_close.setText( num_zan+"");

            }
        });

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(contentView);
    }

    private void textAnimation(){
        AlphaAnimation alp = new AlphaAnimation(1.0f, 0.0f);
        alp.setDuration(1000);
        tv_flag.setAnimation(alp);
        alp.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                num++;
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                tv_flag.setVisibility(View.INVISIBLE);
                time.schedule(new TimerTask() {                  //要做的事情，在规则里面进行声明
                    @Override
                    public void run() {
                        num = 1;
                    }
                }, 1000);
                Toast.makeText(context,"哈哈",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public CardDialog(Context context) {
        this(context, R.style.student_class_dialog);
    }

    public void setData(String exper,String t1,String t2) {
        tv_title.setText("距下一次升级还需要" + exper + "个经验值，加油!");
        btn_click.setText(t1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
