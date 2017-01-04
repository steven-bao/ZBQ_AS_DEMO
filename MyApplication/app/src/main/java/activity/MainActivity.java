package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.main.R;

/**
 * Created by Administrator on 2017/1/4.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button1 = (Button)findViewById(R.id.btn_1);
        button2 = (Button)findViewById(R.id.btn_2);
        button3 = (Button)findViewById(R.id.btn_3);
        button4 = (Button)findViewById(R.id.btn_4);
        button5 = (Button)findViewById(R.id.btn_5);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
                Intent intent = new Intent(MainActivity.this,ListViewActivityDemo.class);
                startActivity(intent);
                break;
            case R.id.btn_2:
//                Intent intent2 = new Intent(MainActivity.this,ListViewActivityDemo.class);
//                startActivity(intent2);
                break;
            case R.id.btn_3:
                Intent intent3 = new Intent(MainActivity.this,PullRefreshExpandActivityDemo.class);
                startActivity(intent3);
                break;
            case R.id.btn_4:
                Intent intent4 = new Intent(MainActivity.this,RecycleViewActivityDemo.class);
                startActivity(intent4);
                break;
            case R.id.btn_5:
//                Intent intent5 = new Intent(MainActivity.this,ListViewActivityDemo.class);
//                startActivity(intent5);
                break;
        }
    }
}
