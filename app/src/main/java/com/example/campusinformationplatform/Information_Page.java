package com.example.campusinformationplatform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Information_Page extends Fragment {

    private Global_Value gv;

    private String Cache_Head_Path;

    private Context context;

    //下拉刷新view
    private PullToRefreshListView PullToRefreshListView;

    //网络端口
    private String HOST;
    private int PORT;

    MainlistAdapt adapt;
    ListView mListView;


    private int ImgViewSize = 500;

    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;

    Handler h1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information_page, container, false);

        gv = (Global_Value) getActivity().getApplication();
        Cache_Head_Path = gv.getCache_Head_Path();

        HOST = gv.getHost();
        PORT = gv.getPort();

        context = getContext();
        //获取下拉刷新信息
        PullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.InformationPage_Pull_Refresh_List);

        System.out.println("进入inf page。");

        listItem = new ArrayList<HashMap<String, Object>>();
        adapt = new MainlistAdapt(context, listItem);
        mListView = PullToRefreshListView.getRefreshableView();
        mListView.setAdapter(adapt);

        RefreshView(0);//刷新页面

//        //获取信息
//        Thread infThread = new Thread(new Runnable() {
//            public void run() {
//
//                try {
//                    String state = Status.GetInformtion_State;
//                    Socket socket = new Socket(HOST, PORT);
//                    JSONObject Sending = new JSONObject();
//
//                    Sending.put("Status", state);
//                    Sending.put("infEndPosition","0");
//                    //写入String
//                    String msg = Sending.toString();
//                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//                    outputStream.writeUTF(msg);
//
//                    outputStream.flush();
//
//                    outputStream.close();
//                    socket.close();
//
//
//                    //接收状态
//                    socket = new Socket(HOST, PORT);
//                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//                    int GetRowNumber = 0;
//                    String s = "";
//                    try {
//                        System.out.println("接收服务器的数据");
//                        //GetRowNumber=Integer.parseInt(inputStream.readUTF());
//                        s = inputStream.readUTF();
//
//                    } catch (Exception e) {
//                        System.out.println("接收服务器数据异常");
//                        e.printStackTrace();
//                    }
//
//                    Log.d("服务器发送的数据为 ", s);
//
//                    inputStream.close();
//                    socket.close();
//
//                    JSONArray Server_JsonArray = new JSONArray(s);
//
//
//                    //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
//                    for (int i = 0; i < Server_JsonArray.length(); i++) {
//                        HashMap<String, Object> item = new HashMap<String, Object>();
//
//                        JSONObject jo = Server_JsonArray.getJSONObject(i);
//                        // System.out.println("jo: "+Server_JsonArray.length());
//                        Bitmap headimg = openImage(Cache_Head_Path + jo.getString("username") + ".jpg");
//
//                        item.put("itemid", i);
//                        item.put("releaseid", jo.getString("releaseid"));
//                        item.put("Type", jo.getString("type"));
//                        item.put("UserHeadImg", headimg);
//                        item.put("UserName", jo.getString("username"));
//                        item.put("ReleaseDate", jo.getString("release_date"));
//                        item.put("Describe", jo.getString("ddescribe"));
//                        item.put("Picrelease", null);
//
//                        listItem.add(item);
//
//                    }
//
//                    //MainlistAdapt adaptt;
//                    new Handler(context.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
//
//                            //MainlistAdapt adapt=new MainlistAdapt(context,listItem);
//                            //ListView mListView = PullToRefreshListView.getRefreshableView();
//
//                            //mListView.setAdapter(adapt);
//                            adapt.notifyDataSetChanged();
//                        }
//                    });
//
////
////                    Handler mainHandler = new Handler(Looper.getMainLooper());
////                    mainHandler.post(new Runnable() {
////
////                        @Override
////
////                        public void run() {
////
////                            Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
////                            listItem.get(1).remove("Picrelease");
////                            listItem.get(1).put("Picrelease",headimg);
////
////
////                            HashMap<String, Object> item = new HashMap<String, Object>();
////
////                            //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
////
////                            item.put("Type", "Type");
////                            item.put("UserHeadImg", headimg);
////                            item.put("UserName", "UserName");
////                            item.put("ReleaseDate", "2021-02-09 18:09:53");
////                            item.put("Describe", "● 此商品来源于Yahoo! JAPAN拍卖网站\n" +
////                                    "● 侦测到“故障品”字样，请与客服确认\n" +
////                                    "● 此卖家为店铺，可能收10%消费税\n" +
////                                    "● 此商品为海外发货，可能收取高额运费。\n" +
////                                    "   海外发货进日本海关可能产生关税，需中标者承担\n" +
////                                    "● 此商品注意尺寸重量限制,以免无法运输\n" +
////                                    "● 特殊卖家，不允许取消！请谨慎出价！;该卖家需加收[10%]消费税!");
////                            item.put("Picrelease", headimg);
////
////                            listItem.add(item);
////                            Log.d("", "nnnnn");
////
////                            ArrayList<HashMap<String, Object>> li=(ArrayList<HashMap<String, Object>>)listItem.clone();
////
////                            listItem.clear();
////                            listItem.addAll(li);
////
////
////                            adapt.notifyDataSetChanged();
////
////                        }
////                    });
//
//
////
////                    h1=new Handler() {
////                        @RequiresApi(api = Build.VERSION_CODES.N)
////                        public void run() {
////                            // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
////
////                            Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
////                            listItem.get(1).remove("Picrelease");
////                            listItem.get(1).put("Picrelease",headimg);
////
////
////                            HashMap<String, Object> item = new HashMap<String, Object>();
////
////                            //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
////
////                            item.put("Type", "Type");
////                            item.put("UserHeadImg", headimg);
////                            item.put("UserName", "UserName");
////                            item.put("ReleaseDate", "2021-02-09 18:09:53");
////                            item.put("Describe", "● 此商品来源于Yahoo! JAPAN拍卖网站\n" +
////                    "● 侦测到“故障品”字样，请与客服确认\n" +
////                    "● 此卖家为店铺，可能收10%消费税\n" +
////                    "● 此商品为海外发货，可能收取高额运费。\n" +
////                    "   海外发货进日本海关可能产生关税，需中标者承担\n" +
////                    "● 此商品注意尺寸重量限制,以免无法运输\n" +
////                    "● 特殊卖家，不允许取消！请谨慎出价！;该卖家需加收[10%]消费税!");
////                            item.put("Picrelease", headimg);
////
////                            listItem.add(item);
////
////
////                            Log.d("", "nnnnn");
////
////                            ArrayList<HashMap<String, Object>> li=(ArrayList<HashMap<String, Object>>)listItem.clone();
////                            //li.clone(listItem);
////                           // li=listItem.clone();
////
////                            listItem.clear();
////                            listItem.addAll(li);
////
////                            adapt.notifyDataSetChanged();
////                            //listItem.notify();
////
////
//////                            MainlistAdapt adapt=new MainlistAdapt(context,listItem);
//////                            ListView mListView = PullToRefreshListView.getRefreshableView();
//////                            mListView.setAdapter(adapt);
////                        }
////                    };
//
//
////                     String img=jo.get("picture_released1").toString();
////                     img=new MyTB
//
//
////                    //获取每行数据
////                    for(int i=0;i<GetRowNumber;i++){
////                        socket = new Socket(HOST, PORT);
////                        inputStream=new DataInputStream(socket.getInputStream());
////                        String server_msg="";
////                        try{
////                            System.out.println("接收服务器的数据");
////                            server_msg=inputStream.readUTF();
////
////                            JSONArray Server_msg = new JSONArray(server_msg);
////
////                            HashMap<String, Object> item = new HashMap<String, Object>();
////                            item.put("Type", s.get);
////                            //item.put("UserHeadImg", headimg);
////                            item.put("UserName", "UserName");
////                            item.put("ReleaseDate", "2021-02-09 18:09:53");
////
////
////
////
////
////                        }catch(Exception e){
////                            System.out.println("接收服务器数据异常");
////                            e.printStackTrace();
////                        }
////
////
////
////
////
////                    }
//
//                } catch (Exception e) {
//                    System.out.println("服务器连接异常");
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//        Thread picThread = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Log.d("listitem size: ", String.valueOf(listItem.size()));
//                    for (int i = 0; i < listItem.size(); i++) {
//                        String state = Status.GetInformtionPic_State;
//                        Socket socket = new Socket(HOST, PORT);
//                        JSONObject Sending = new JSONObject();
//
//                        Sending.put("Status", state);
//
//                        if (i == 0) {
//                            Sending.put("Picnum", listItem.size());
//                        }
//
//                        Sending.put("releaseid", listItem.get(i).get("releaseid"));
//
//                        String smsg = Sending.toString();
//
//                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//                        outputStream.writeUTF(smsg);
//
//                        outputStream.flush();
//
//                        outputStream.close();
//                        socket.close();
//
//
//                        socket = new Socket(HOST, PORT);
//                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//
//                        try {
//                            System.out.println("接收服务器的数据");
//                            long size = inputStream.readLong();
//                            byte[] data = new byte[(int) size];
//                            int len = 0;
//                            while (len < size) {
//                                len += inputStream.read(data, len, (int) size - len);
//                            }
//
//
//                            //ByteArrayOutputStream outPut = new ByteArrayOutputStream();
//                            if (data == null)
//                                Log.d("data", "null");
//
//                            //inputStream.close();
//                            socket.close();
//
//                            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//                            final Bitmap showBitmap = getScaleBitmap(bitmap);
//
//                            final int finalI = i;
//                            Handler mainHandler = new Handler(Looper.getMainLooper());
//                            mainHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//                                    listItem.get(finalI).remove("Picrelease");
//                                    listItem.get(finalI).put("Picrelease", showBitmap);
//
//                                    adapt.notifyDataSetChanged();
////                                    MainlistAdapt adapt=new MainlistAdapt(context,listItem);
////                                    ListView mListView = PullToRefreshListView.getRefreshableView();
////
////                                    mListView.setAdapter(adapt);
//
//                                }
//                            });
//
//                        } catch (
//                                Exception e) {
//                            System.out.println("接收服务器数据异常");
//                            e.printStackTrace();
//                        }
//
//                        Log.d("服务器发送图片 ", "接收成功");
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//
//        infThread.start();
//        try {
//            infThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        picThread.start();
//

        //设置pull-to-refresh模式为Mode.Both
        PullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        //设置上拉下拉事件
        PullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                if (refreshView.isHeaderShown()) {
                    Toast.makeText(context, "下拉刷新", Toast.LENGTH_SHORT).show();
                    //下拉刷新 业务代码

                    RefreshView(1);

                    PullToRefreshListView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            PullToRefreshListView.onRefreshComplete();
                        }
                    }, 1000);


                } else {
                    Toast.makeText(context, "上拉加载更多", Toast.LENGTH_SHORT).show();
                    //上拉加载更多 业务代码

                    Thread infThread=new Thread(new Runnable() {
                        public void run() {
                            try {
                                String state = Status.GetInformtion_State;
                                Socket socket = new Socket(HOST, PORT);
                                JSONObject Sending = new JSONObject();

                                Sending.put("Status", state);

                                Sending.put("infEndPosition",listItem.get(listItem.size()-1).get("releaseid").toString());

                                //写入String
                                String msg = Sending.toString();
                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                outputStream.writeUTF(msg);

                                outputStream.flush();

                                outputStream.close();
                                socket.close();


                                //

                                //接收状态
                                socket = new Socket(HOST, PORT);
                                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                                //int GetRowNumber = 0;
                                String s = "";
                                try {
                                    System.out.println("接收服务器的数据");
                                    //GetRowNumber=Integer.parseInt(inputStream.readUTF());
                                    s = inputStream.readUTF();

                                } catch (Exception e) {
                                    System.out.println("接收服务器数据异常");
                                    e.printStackTrace();
                                }

                                Log.d("服务器发送的数据为 ", s);

                                inputStream.close();
                                socket.close();

                                JSONArray Server_JsonArray = new JSONArray(s);
                                //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                                for (int i = 0; i < Server_JsonArray.length(); i++) {
                                    HashMap<String, Object> item = new HashMap<String, Object>();

                                    JSONObject jo = Server_JsonArray.getJSONObject(i);
                                    Bitmap headimg = openImage(Cache_Head_Path + jo.getString("username") + ".jpg");

                                    item.put("itemid", i);
                                    item.put("releaseid", jo.getString("releaseid"));
                                    item.put("Type", jo.getString("type"));
                                    item.put("UserHeadImg", headimg);
                                    item.put("UserName", jo.getString("username"));
                                    item.put("ReleaseDate", jo.getString("release_date"));
                                    item.put("Describe", jo.getString("ddescribe"));
                                    item.put("Picrelease", null);

                                    listItem.add(item);

                                }

                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapt.notifyDataSetChanged();
                                    }
                                });



                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });


                    Thread picThread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                Log.d("listitem size: ", String.valueOf(listItem.size()));

                                for (int i = 0; i < listItem.size(); i++) {
                                    if (listItem.get(i).get("Picrelease") == null) {
                                        System.out.println("添加图片："+listItem.get(i).get("releaseid"));
                                        String state = Status.GetInformtionPic_State;
                                        Socket socket = new Socket(HOST, PORT);
                                        JSONObject Sending = new JSONObject();

                                        Sending.put("Status", state);
                                        Sending.put("releaseid", listItem.get(i).get("releaseid"));

                                        String smsg = Sending.toString();

                                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                        outputStream.writeUTF(smsg);

                                        outputStream.flush();

                                        outputStream.close();
                                        socket.close();


                                        socket = new Socket(HOST, PORT);
                                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                                        try {
                                            System.out.println("接收服务器的数据");
                                            long size = inputStream.readLong();
                                            byte[] data = new byte[(int) size];
                                            int len = 0;
                                            while (len < size) {
                                                len += inputStream.read(data, len, (int) size - len);
                                            }


                                            //ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                                            if (data == null)
                                                Log.d("data", "null");

                                            //inputStream.close();
                                            socket.close();

                                            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                            final Bitmap showBitmap = getScaleBitmap(bitmap);

                                            final int finalI = i;
                                            Handler mainHandler = new Handler(Looper.getMainLooper());
                                            mainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
                                                    listItem.get(finalI).remove("Picrelease");
                                                    listItem.get(finalI).put("Picrelease", showBitmap);

                                                    adapt.notifyDataSetChanged();


                                                }
                                            });
                                            System.out.println("接收服务器数据成功");


//                                            socket = new Socket(HOST, PORT);
//                                            outputStream = new DataOutputStream(socket.getOutputStream());
//                                            outputStream.writeUTF(smsg);
//                                            System.out.println("发送图片信息");
//
//                                            outputStream.flush();
//
//                                            outputStream.close();
//                                            socket.close();


                                        } catch (Exception e) {
                                            System.out.println("接收服务器数据异常");
                                            e.printStackTrace();
                                        }


                                    }
                                }

                            } catch (Exception e) {
                                System.out.println("接收服务器数据异常");
                                e.printStackTrace();
                            }


                        }
                    });



                    infThread.start();
                    try {
                        infThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    picThread.start();

//                    Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//
//                    HashMap<String, Object> item = new HashMap<String, Object>();
//
//                    //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//
//                    item.put("Type", "Type");
//                    item.put("UserHeadImg", headimg);
//                    item.put("UserName", "UserName");
//                    item.put("ReleaseDate", "2021-02-09 18:09:53");
//                    item.put("Describe", "● 此商品来源于Yahoo! JAPAN拍卖网站\n" +
//                            "● 侦测到“故障品”字样，请与客服确认\n" +
//                            "● 此卖家为店铺，可能收10%消费税\n" +
//                            "● 此商品为海外发货，可能收取高额运费。\n" +
//                            "   海外发货进日本海关可能产生关税，需中标者承担\n" +
//                            "● 此商品注意尺寸重量限制,以免无法运输\n" +
//                            "● 特殊卖家，不允许取消！请谨慎出价！;该卖家需加收[10%]消费税!");
//                    item.put("Picrelease", headimg);
//
//                    listItem.add(item);
//
//                    adapt.notifyDataSetChanged();

                    PullToRefreshListView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            PullToRefreshListView.onRefreshComplete();
                        }
                    }, 1000);


                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(listItem.get(position - 1).get("releaseid").toString(), "onItemClick: ");

                gv.setDetails_Releaseid(listItem.get(position - 1).get("releaseid").toString());
                Thread infThread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            String state = Status.GetInformtionDetails_State;
                            Socket socket = new Socket(HOST, PORT);
                            JSONObject Sending = new JSONObject();

                            Sending.put("Status", state);

                            Sending.put("releaseid", listItem.get(position - 1).get("releaseid").toString());

                            //写入String
                            String msg = Sending.toString();
                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                            outputStream.writeUTF(msg);

                            outputStream.flush();

                            outputStream.close();
                            socket.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

                infThread.start();

                Intent i = new Intent(getActivity() , Details_Page.class);
                startActivity(i);

            }
        });






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

    private Bitmap getScaleBitmap(Bitmap bm) {
        int bitmapWidth = bm.getWidth();
        int bitmapHeight = bm.getHeight();
        float scaleWidth;
        float scaleHeight;

        int newWidth;
        int newHeight;

        float scale = (float) bitmapWidth / (float) bitmapHeight;
        if (scale >= 1) {//比较宽,正方形
            newHeight = ImgViewSize;
            newWidth = (int) (newHeight * scale);
        } else {//比较长
            newWidth = ImgViewSize;
            newHeight = (int) (newWidth / scale);
        }

        // 计算缩放比例
        scaleWidth = ((float) newWidth) / bitmapWidth;
        scaleHeight = ((float) newHeight) / bitmapHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, bitmapWidth, bitmapHeight, matrix,
                true);

        return newbm;
    }

    private Boolean RefreshView(int state){
//        if(state==1)
//            listItem.clear();

        //获取信息
        Thread infThread = new Thread(new Runnable() {
            public void run() {

                try {
                    String state = Status.GetInformtion_State;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);
                    Sending.put("infEndPosition","0");
                    //写入String
                    String msg = Sending.toString();
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    outputStream.close();
                    socket.close();


                    //接收状态
                    socket = new Socket(HOST, PORT);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    int GetRowNumber = 0;
                    String s = "";
                    try {
                        System.out.println("刷新接收服务器的数据");
                        //GetRowNumber=Integer.parseInt(inputStream.readUTF());
                        s = inputStream.readUTF();

                    } catch (Exception e) {
                        System.out.println("接收服务器数据异常");
                        e.printStackTrace();
                    }

                    Log.d("服务器发送的数据为 ", s);

                    inputStream.close();
                    socket.close();

                    JSONArray Server_JsonArray = new JSONArray(s);


                        ArrayList<HashMap<String, Object>> templistItem = (ArrayList<HashMap<String, Object>>) listItem.clone();
                        listItem.clear();
                        for (int i = 0; i < Server_JsonArray.length(); i++) {
                            JSONObject jo = Server_JsonArray.getJSONObject(i);
                            boolean gea=false;
                            for(int n=0;n<templistItem.size();n++){
                                if(templistItem.get(n).get("releaseid").equals(jo.getString("releaseid"))){
                                    listItem.add(templistItem.get(n));
                                    gea=true;
                                    Log.d("lll", "czcz");
                                    break;
                                }

                            }
                            if (gea == false) {
                                HashMap<String, Object> item = new HashMap<String, Object>();
                                // System.out.println("jo: "+Server_JsonArray.length());
                                Bitmap headimg = openImage(Cache_Head_Path + jo.getString("username") + ".jpg");

                                item.put("itemid", i);
                                item.put("releaseid", jo.getString("releaseid"));
                                item.put("Type", jo.getString("type"));
                                item.put("UserHeadImg", headimg);
                                item.put("UserName", jo.getString("username"));
                                item.put("ReleaseDate", jo.getString("release_date"));
                                item.put("Describe", jo.getString("ddescribe"));
                                item.put("Picrelease", null);

                                listItem.add(item);
                            }

                        }




                    //MainlistAdapt adaptt;
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui

                            //MainlistAdapt adapt=new MainlistAdapt(context,listItem);
                            //ListView mListView = PullToRefreshListView.getRefreshableView();

                            //mListView.setAdapter(adapt);
                            adapt.notifyDataSetChanged();
                        }
                    });

//
//                    Handler mainHandler = new Handler(Looper.getMainLooper());
//                    mainHandler.post(new Runnable() {
//
//                        @Override
//
//                        public void run() {
//
//                            Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//                            listItem.get(1).remove("Picrelease");
//                            listItem.get(1).put("Picrelease",headimg);
//
//
//                            HashMap<String, Object> item = new HashMap<String, Object>();
//
//                            //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//
//                            item.put("Type", "Type");
//                            item.put("UserHeadImg", headimg);
//                            item.put("UserName", "UserName");
//                            item.put("ReleaseDate", "2021-02-09 18:09:53");
//                            item.put("Describe", "● 此商品来源于Yahoo! JAPAN拍卖网站\n" +
//                                    "● 侦测到“故障品”字样，请与客服确认\n" +
//                                    "● 此卖家为店铺，可能收10%消费税\n" +
//                                    "● 此商品为海外发货，可能收取高额运费。\n" +
//                                    "   海外发货进日本海关可能产生关税，需中标者承担\n" +
//                                    "● 此商品注意尺寸重量限制,以免无法运输\n" +
//                                    "● 特殊卖家，不允许取消！请谨慎出价！;该卖家需加收[10%]消费税!");
//                            item.put("Picrelease", headimg);
//
//                            listItem.add(item);
//                            Log.d("", "nnnnn");
//
//                            ArrayList<HashMap<String, Object>> li=(ArrayList<HashMap<String, Object>>)listItem.clone();
//
//                            listItem.clear();
//                            listItem.addAll(li);
//
//
//                            adapt.notifyDataSetChanged();
//
//                        }
//                    });


//
//                    h1=new Handler() {
//                        @RequiresApi(api = Build.VERSION_CODES.N)
//                        public void run() {
//                            // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
//
//                            Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//                            listItem.get(1).remove("Picrelease");
//                            listItem.get(1).put("Picrelease",headimg);
//
//
//                            HashMap<String, Object> item = new HashMap<String, Object>();
//
//                            //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//
//                            item.put("Type", "Type");
//                            item.put("UserHeadImg", headimg);
//                            item.put("UserName", "UserName");
//                            item.put("ReleaseDate", "2021-02-09 18:09:53");
//                            item.put("Describe", "● 此商品来源于Yahoo! JAPAN拍卖网站\n" +
//                    "● 侦测到“故障品”字样，请与客服确认\n" +
//                    "● 此卖家为店铺，可能收10%消费税\n" +
//                    "● 此商品为海外发货，可能收取高额运费。\n" +
//                    "   海外发货进日本海关可能产生关税，需中标者承担\n" +
//                    "● 此商品注意尺寸重量限制,以免无法运输\n" +
//                    "● 特殊卖家，不允许取消！请谨慎出价！;该卖家需加收[10%]消费税!");
//                            item.put("Picrelease", headimg);
//
//                            listItem.add(item);
//
//
//                            Log.d("", "nnnnn");
//
//                            ArrayList<HashMap<String, Object>> li=(ArrayList<HashMap<String, Object>>)listItem.clone();
//                            //li.clone(listItem);
//                           // li=listItem.clone();
//
//                            listItem.clear();
//                            listItem.addAll(li);
//
//                            adapt.notifyDataSetChanged();
//                            //listItem.notify();
//
//
////                            MainlistAdapt adapt=new MainlistAdapt(context,listItem);
////                            ListView mListView = PullToRefreshListView.getRefreshableView();
////                            mListView.setAdapter(adapt);
//                        }
//                    };


//                     String img=jo.get("picture_released1").toString();
//                     img=new MyTB


//                    //获取每行数据
//                    for(int i=0;i<GetRowNumber;i++){
//                        socket = new Socket(HOST, PORT);
//                        inputStream=new DataInputStream(socket.getInputStream());
//                        String server_msg="";
//                        try{
//                            System.out.println("接收服务器的数据");
//                            server_msg=inputStream.readUTF();
//
//                            JSONArray Server_msg = new JSONArray(server_msg);
//
//                            HashMap<String, Object> item = new HashMap<String, Object>();
//                            item.put("Type", s.get);
//                            //item.put("UserHeadImg", headimg);
//                            item.put("UserName", "UserName");
//                            item.put("ReleaseDate", "2021-02-09 18:09:53");
//
//
//
//
//
//                        }catch(Exception e){
//                            System.out.println("接收服务器数据异常");
//                            e.printStackTrace();
//                        }
//
//
//
//
//
//                    }

                } catch (Exception e) {
                    System.out.println("服务器连接异常");
                    e.printStackTrace();
                }


            }
        });
//
//        Thread picThread = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Log.d("listitem size: ", String.valueOf(listItem.size()));
//                    for (int i = 0; i < listItem.size(); i++) {
//                        String state = Status.GetInformtionPic_State;
//                        Socket socket = new Socket(HOST, PORT);
//                        JSONObject Sending = new JSONObject();
//
//                        Sending.put("Status", state);
//
//                        if (i == 0) {
//                            Sending.put("Picnum", listItem.size());
//                        }
//
//                        Sending.put("releaseid", listItem.get(i).get("releaseid"));
//
//                        String smsg = Sending.toString();
//
//                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//                        outputStream.writeUTF(smsg);
//
//                        outputStream.flush();
//
//                        outputStream.close();
//                        socket.close();
//
//
//                        socket = new Socket(HOST, PORT);
//                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//
//                        try {
//                            System.out.println("接收服务器的数据");
//                            long size = inputStream.readLong();
//                            byte[] data = new byte[(int) size];
//                            int len = 0;
//                            while (len < size) {
//                                len += inputStream.read(data, len, (int) size - len);
//                            }
//
//
//                            //ByteArrayOutputStream outPut = new ByteArrayOutputStream();
//                            if (data == null)
//                                Log.d("data", "null");
//
//                            //inputStream.close();
//                            socket.close();
//
//                            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//                            final Bitmap showBitmap = getScaleBitmap(bitmap);
//
//                            final int finalI = i;
//                            Handler mainHandler = new Handler(Looper.getMainLooper());
//                            mainHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//                                    listItem.get(finalI).remove("Picrelease");
//                                    listItem.get(finalI).put("Picrelease", showBitmap);
//
//                                    adapt.notifyDataSetChanged();
////                                    MainlistAdapt adapt=new MainlistAdapt(context,listItem);
////                                    ListView mListView = PullToRefreshListView.getRefreshableView();
////
////                                    mListView.setAdapter(adapt);
//
//                                }
//                            });
//
//                        } catch (
//                                Exception e) {
//                            System.out.println("接收服务器数据异常");
//                            e.printStackTrace();
//                        }
//
//                        Log.d("服务器发送图片 ", "接收成功");
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

        Thread picThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("listitem size: ", String.valueOf(listItem.size()));

                    for (int i = 0; i < listItem.size(); i++) {
                        if (listItem.get(i).get("Picrelease") == null) {
                            System.out.println("添加图片："+listItem.get(i).get("releaseid"));
                            String state = Status.GetInformtionPic_State;

                            JSONObject Sending = new JSONObject();

                            Sending.put("Status", state);
                            Sending.put("releaseid", listItem.get(i).get("releaseid"));

                            String smsg = Sending.toString();

                            Socket socket = new Socket(HOST, PORT);
                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                            outputStream.writeUTF(smsg);
                            System.out.println("发送图片信息");

                            outputStream.flush();

                            outputStream.close();
                            socket.close();


                            socket = new Socket(HOST, PORT);
                            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                            try {
                                System.out.println("接收服务器的数据");
                                long size = inputStream.readLong();
                                byte[] data = new byte[(int) size];
                                int len = 0;
                                while (len < size) {
                                    len += inputStream.read(data, len, (int) size - len);
                                }

                                //ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                                if (data == null)
                                    Log.d("data", "null");

                                inputStream.close();
                                socket.close();

//                                socket = new Socket(HOST, PORT);
//                                outputStream = new DataOutputStream(socket.getOutputStream());
//                                outputStream.writeUTF(smsg);
//                                System.out.println("发送图片信息");
//
//                                outputStream.flush();
//
//                                outputStream.close();
//                                socket.close();



                                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                final Bitmap showBitmap = getScaleBitmap(bitmap);

                                final int finalI = i;
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
                                        listItem.get(finalI).remove("Picrelease");
                                        listItem.get(finalI).put("Picrelease", showBitmap);

                                        adapt.notifyDataSetChanged();

                                    }
                                });
                                System.out.println("接收服务器数据成功");
                            } catch (Exception e) {
                                System.out.println("接收服务器数据异常");
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("接收服务器数据异常");
                    e.printStackTrace();
                }


            }
        });


        infThread.start();
        try {
            infThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        picThread.start();

        return true;
    }

}
