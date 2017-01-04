package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.main.R;

import java.util.List;

import bean.Student;

/**
 * Created by Administrator on 2016/12/29.
 */

public class StudentAdapter extends BaseAdapter {

    private Context context;
    private List<Student> list;

    public StudentAdapter(Context context,List<Student> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder =null;
        if(convertView == null){
            holder = new MyHolder();
            convertView = View.inflate(context, R.layout.studentlist_item,null);
            holder.name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.sex = (TextView)convertView.findViewById(R.id.tv_sex);
            holder.hobby = (TextView)convertView.findViewById(R.id.tv_hobby);
            convertView.setTag(holder);
        }else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.sex.setText(list.get(position).getSex());
        holder.hobby.setText(list.get(position).getHobby());
        return convertView;
    }

    public class MyHolder{
        private TextView name;
        private TextView sex;
        private TextView hobby;
    }
}
