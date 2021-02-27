package com.example.campusinformationplatform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;



public class SelfReleaselistAdapt extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    ArrayList<HashMap<String, Object>> listItem;

    private Context context;



    public SelfReleaselistAdapt(Context context, ArrayList<HashMap<String, Object>> listItem) {
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
        public TextView SelfType;
        public TextView SelfTitle;
        public TextView SelfReleasedate;
        public TextView SelfDescribe;


        public ImageView SelfIcon;
    }//声明item结构


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        SelfReleaselistAdapt.itemStruct zj ;
        if(convertView == null)
        {
            zj = new SelfReleaselistAdapt.itemStruct();
            convertView = mInflater.inflate(R.layout.release_self_list_item, null);

            zj.SelfType=(TextView)convertView.findViewById(R.id.Release_Self_Type);
            zj.SelfTitle=(TextView)convertView.findViewById(R.id.Release_Self_Title);
            zj.SelfReleasedate=(TextView)convertView.findViewById(R.id.Release_Self_Date);
            zj.SelfDescribe=(TextView)convertView.findViewById(R.id.Release_Self_Describe);


            zj.SelfIcon=(ImageView) convertView.findViewById(R.id.Release_Self_Icon);
            convertView.setTag(zj);
        }
        else {
            zj = (SelfReleaselistAdapt.itemStruct)convertView.getTag();

        }

        zj.SelfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.onClick(v,position);
            }
        });

        if(((String) listItem.get(position).get("isEnd")).equals("false")) {
            zj.SelfType.setText((String) listItem.get(position).get("SelfType"));
            zj.SelfTitle.setText((String) listItem.get(position).get("SelfTitle"));
            zj.SelfReleasedate.setText("发布于 " + (String) listItem.get(position).get("SelfReleasedate"));
            zj.SelfDescribe.setText((String) listItem.get(position).get("SelfDescribe"));


        }
        else{
            zj.SelfType.setText("暂无更多数据");
            zj.SelfTitle.setText(null);
            zj.SelfReleasedate.setText(null);
            zj.SelfDescribe.setText(null);


            zj.SelfReleasedate.setHeight(0);
            zj.SelfTitle.setHeight(0);
            zj.SelfDescribe.setHeight(0);


            zj.SelfIcon.setVisibility(View.INVISIBLE);

        }


        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图

    /**
     * 按钮的监听接口
     */
    public interface onItemListener {
        void onClick(View v,int i);
    }

    private onItemListener mOnItemListener;

    public void setOnItemClickListener(onItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }




}



