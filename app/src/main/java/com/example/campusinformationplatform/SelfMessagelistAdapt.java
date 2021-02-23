package com.example.campusinformationplatform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SelfMessagelistAdapt extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    ArrayList<HashMap<String, Object>> listItem;

    private Context context;



    public SelfMessagelistAdapt(Context context, ArrayList<HashMap<String, Object>> listItem) {
        this.mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
        this.context=context;


    }//声明构造函数


    @Override
    public int getCount() {
        return listItem.size();
    }//这个方法返回了在适配器中所代表的数据集合的条目数

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }//这个方法返回了数据集合中与指定索引position对应的数据项

    @Override
    public long getItemId(int position) {
        return position;
    }//这个方法返回了在列表中与指定索引对应的行id


    static class itemStruct
    {

        public TextView SelfTitle;
        public TextView SelfReleasedate;
        public TextView SelfMessage;

        public ImageView SelfIcon;
    }//声明item结构


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        SelfMessagelistAdapt.itemStruct zj ;
        if(convertView == null)
        {
            zj = new SelfMessagelistAdapt.itemStruct();
            convertView = mInflater.inflate(R.layout.message_self_list_item, null);


            zj.SelfTitle=(TextView)convertView.findViewById(R.id.Message_Self_Title);
            zj.SelfReleasedate=(TextView)convertView.findViewById(R.id.Message_Self_Date);
            zj.SelfMessage=(TextView)convertView.findViewById(R.id.Message_Self_Message);


            zj.SelfIcon=(ImageView) convertView.findViewById(R.id.Message_Self_Icon);
            convertView.setTag(zj);
        }
        else {
            zj = (SelfMessagelistAdapt.itemStruct)convertView.getTag();

        }

        zj.SelfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.onClick(v,position);
            }
        });


        if(((String) listItem.get(position).get("isEnd")).equals("false")) {

            zj.SelfTitle.setText((String) listItem.get(position).get("SelfTitle"));
            zj.SelfReleasedate.setText("发布于 " + (String) listItem.get(position).get("SelfReleasedate"));
            zj.SelfMessage.setText((String) listItem.get(position).get("SelfMessage"));

        }
        else{
            zj.SelfTitle.setText("暂无更多数据");

            zj.SelfReleasedate.setHeight(0);
            zj.SelfMessage.setHeight(0);

            //zj.SelfTitle.setText(null);
            zj.SelfReleasedate.setText(null);
            zj.SelfMessage.setText(null);


            zj.SelfIcon.setVisibility(View.INVISIBLE);

        }


        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图

    /**
     * 删除按钮的监听接口
     */
    public interface onItemListener {
        void onClick(View v,int i);
    }

    private SelfMessagelistAdapt.onItemListener mOnItemListener;

    public void setOnItemClickListener(SelfMessagelistAdapt.onItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }




}




