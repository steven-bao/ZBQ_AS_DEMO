package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.administrator.main.R;

import java.util.ArrayList;
import java.util.List;

import adapter.StudentAdapter;
import bean.Student;

/**
 * Created by Administrator on 2016/12/26.
 */

public class TestFragment extends Fragment{

    private List<Student> list1 = new ArrayList<>();
    private GridView gridView;
    StudentAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment,null);
        gridView = (GridView)view.findViewById(R.id.g1);
        if(adapter == null){
            adapter = new StudentAdapter(getActivity(),list1);
            gridView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    public void setData(List<Student> list1){
        this.list1 = list1;
        try{
            if(adapter == null || gridView == null){
                adapter = new StudentAdapter(getActivity(),list1);
                if(gridView != null){
                    gridView.setAdapter(adapter);
                }
            }else {
                adapter = new StudentAdapter(getActivity(),list1);
                gridView.setAdapter(adapter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
