package com.example.campusinformationplatform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Information_Page  extends Fragment {

    private Global_Value gv;

    private String Cache_Head_Path;

    private Context context;

    //下拉刷新view
    private PullToRefreshListView PullToRefreshListView;

    //网络端口
    private String HOST;
    private int PORT;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information_page, container,false);

        gv = (Global_Value) getActivity().getApplication();
        Cache_Head_Path=gv.getCache_Head_Path();

        HOST=gv.getHost();
        PORT=gv.getPort();

        context=getContext();
        //获取下拉刷新信息
        PullToRefreshListView= (PullToRefreshListView)view.findViewById(R.id.InformationPage_Pull_Refresh_List);

        System.out.println("进入inf page。");


        //获取信息
        new Thread(new Runnable() {
            public void run() {

                try{
                    String state = Status.GetInformtion_State;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);

                    //写入String
                    String msg=Sending.toString();
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    outputStream.close();
                    socket.close();


                    //接收状态
                    socket = new Socket(HOST, PORT);
                    DataInputStream inputStream=new DataInputStream(socket.getInputStream());
                    int GetRowNumber=0;
                    try{
                        System.out.println("接收服务器的数据");
                        GetRowNumber=Integer.parseInt(inputStream.readUTF());

                    }catch(Exception e){
                        System.out.println("接收服务器数据异常");
                        e.printStackTrace();
                    }

                    Log.d("服务器发送的数据为 ", String.valueOf(GetRowNumber));

                    inputStream.close();
                    socket.close();



                }catch(Exception e){
                    System.out.println("服务器异常");
                    e.printStackTrace();
                }





            }
        }).start();











        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        for(int i=0;i<5;i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();

            Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");

            item.put("Type", "Type");
            item.put("UserHeadImg", headimg);
            item.put("UserName", "UserName");
            item.put("ReleaseDate", "2021-02-09 18:09:53");
            item.put("Describe", "● 此商品来源于Yahoo! JAPAN拍卖网站\n" +
                    "● 侦测到“故障品”字样，请与客服确认\n" +
                    "● 此卖家为店铺，可能收10%消费税\n" +
                    "● 此商品为海外发货，可能收取高额运费。\n" +
                    "   海外发货进日本海关可能产生关税，需中标者承担\n" +
                    "● 此商品注意尺寸重量限制,以免无法运输\n" +
                    "● 特殊卖家，不允许取消！请谨慎出价！;该卖家需加收[10%]消费税!");
            item.put("Picrelease", headimg);

            listItem.add(item);
        }
        MainlistAdapt adapt=new MainlistAdapt(context,listItem);


        ListView mListView = PullToRefreshListView.getRefreshableView();
        mListView.setAdapter(adapt);





        return view;
    }

    public static Bitmap openImage(String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
