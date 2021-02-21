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

        zj.SelfType.setText((String) listItem.get(position).get("SelfType"));
        zj.SelfTitle.setText((String) listItem.get(position).get("SelfTitle"));
        zj.SelfReleasedate.setText("发布于 "+(String) listItem.get(position).get("SelfReleasedate"));
        zj.SelfDescribe.setText((String) listItem.get(position).get("SelfDescribe"));


        zj.SelfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zjzj", "onClick: ");
                showPopupMenu(v);
            }
        });


        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(context, view);


        //popupMenu.inflate(R.style.TextAppearance_AppCompat_Light_Widget_PopupMenu_Large);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.self_release_menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(context, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }



}



