package com.example.campusinformationplatform;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ReleaseSelf_Page extends AppCompatActivity {

    private Context context;

    //网络端口
    private String HOST;
    private int PORT;

    private Global_Value gv;

    //下拉刷新view
    private PullToRefreshListView PullToRefreshListView;

    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;
    private SelfReleaselistAdapt adapt;

    private ListView listview;

    private boolean isEnd=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_self__page);

        context=this;

        gv = (Global_Value) getApplication();

        HOST = gv.getHost();
        PORT = gv.getPort();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("我的发布");

        PullToRefreshListView = (PullToRefreshListView) findViewById(R.id.Self_Release_Pull_Refresh_List);
        listview = PullToRefreshListView.getRefreshableView();
        listItem = new ArrayList<HashMap<String, Object>>();
        adapt = new SelfReleaselistAdapt(this, listItem);

        listview.setAdapter(adapt);

        RefreshView();

        //listview点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //Toast.makeText(ReleaseSelf_Page.this, "Click item" + position, Toast.LENGTH_SHORT).show();
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

                Intent i = new Intent(context , Details_Page.class);
                startActivity(i);

            }
        });


        //item点击事件
        adapt.setOnItemClickListener(new SelfReleaselistAdapt.onItemListener() {
            @Override
            public void onClick(View v,int pos) {

                gv.setUpdate_Releaseid(listItem.get(pos).get("releaseid").toString());
                Log.d("Pos", String.valueOf(pos)+"   " +listItem.get(pos).get("releaseid"));
                showPopupMenu(v,pos);

            }

        });


        PullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        //设置上拉下拉事件
        PullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    Toast.makeText(getApplicationContext(), "下拉刷新", Toast.LENGTH_SHORT).show();
                    //下拉刷新 业务代码
                    RefreshView();

                    PullToRefreshListView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            PullToRefreshListView.onRefreshComplete();
                        }
                    }, 1000);


                } else {
                    Toast.makeText(getApplicationContext(), "上拉加载更多", Toast.LENGTH_SHORT).show();
                    //上拉加载更多 业务代码
                    if(isEnd==false) {
                        Thread infThread = new Thread(new Runnable() {
                            public void run() {
                                try {
                                    String state = Status.GetUserselfRelease;
                                    Socket socket = new Socket(HOST, PORT);
                                    JSONObject Sending = new JSONObject();

                                    Sending.put("Status", state);

                                    Sending.put("Username", gv.getUserName());
                                    Sending.put("EndPosition", listItem.get(listItem.size() - 1).get("releaseid").toString());

                                    //写入String
                                    String msg = Sending.toString();
                                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                    outputStream.writeUTF(msg);

                                    outputStream.flush();

                                    //outputStream.close();
                                    //socket.close();



                                    //接收状态
                                    //socket = new Socket(HOST, PORT);
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
                                    if(Server_JsonArray.length()==0){
                                        isEnd=true;

                                        Log.d("", "trtr");
                                    }

                                    //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                                    for (int i = 0; i < Server_JsonArray.length(); i++) {

                                        HashMap<String, Object> item = new HashMap<String, Object>();

                                        JSONObject jo = Server_JsonArray.getJSONObject(i);
                                        if(jo.getString("Re_state").equals(Status.Re_InfisEnd))
                                        {
                                            item.put("isEnd", "true");
                                            isEnd=true;
                                        }

                                        else {
                                            item.put("isEnd", "false");
                                            item.put("releaseid", jo.getString("releaseid"));
                                            item.put("SelfType", jo.getString("type"));
                                            item.put("SelfTitle", jo.getString("title"));
                                            item.put("SelfReleasedate", jo.getString("release_date"));
                                            item.put("SelfDescribe", jo.getString("ddescribe"));
                                        }
                                        listItem.add(item);

                                    }


                                    new Handler(context.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {

                                            adapt.notifyDataSetChanged();
                                        }
                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        infThread.start();

                    }

                    PullToRefreshListView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            PullToRefreshListView.onRefreshComplete();
                        }
                    }, 1000);


                }

            }
        });


    }

    private void RefreshView(){

        isEnd=false;
        Thread infThread=new Thread(new Runnable() {
            public void run() {
                try {
                    String state = Status.GetUserselfRelease;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);

                    Sending.put("Username",gv.getUserName());
                    Sending.put("EndPosition",0);

                    //写入String
                    String msg = Sending.toString();
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    //outputStream.close();
                    //socket.close();


                    //

                    //接收状态
                    //socket = new Socket(HOST, PORT);
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

                    //Log.d("", s);
                    final JSONArray Server_JsonArray = new JSONArray(s);


                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                           //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                            for (int i = 0; i < Server_JsonArray.length(); i++) {

                                HashMap<String, Object> item = new HashMap<String, Object>();

                                JSONObject jo = null;
                                try {
                                    jo = Server_JsonArray.getJSONObject(i);

                                    item.put("isEnd", "false");
                                    item.put("releaseid", jo.getString("releaseid"));
                                    item.put("SelfType", jo.getString("type"));
                                    item.put("SelfTitle", jo.getString("title"));
                                    item.put("SelfReleasedate", jo.getString("release_date"));
                                    item.put("SelfDescribe", jo.getString("ddescribe"));
                                }catch (JSONException e) {
                                e.printStackTrace();
                            }

                                listItem.add(item);

                            }


                            adapt.notifyDataSetChanged();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        infThread.start();

    }

    private void showPopupMenu(final View view, final int position) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(context, view);

        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.self_release_menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                if(item.getTitle().equals("删除")){
                    //item.g
                    final String Id=(String)listItem.get(position).get("releaseid");
                    Log.d("item ",Id);
                    Log.d("item ",HOST);

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String state = Status.DeleteUserRelease;
                                Socket socket = new Socket(HOST, PORT);
                                JSONObject Sending = new JSONObject();

                                Sending.put("Status", state);

                                Sending.put("releaseid",Id);

                                //写入String
                                String msg = Sending.toString();
                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                outputStream.writeUTF(msg);

                                outputStream.flush();

                                outputStream.close();
                                socket.close();



                            }catch (Exception e){

                            }
                        }
                    }).start();


                    listItem.remove(position);

                    adapt.notifyDataSetChanged();

                }

                else if (item.getTitle().equals("编辑")){

                    //gv.setUpdate_Releaseid();
                    Intent i = new Intent(ReleaseSelf_Page.this , UpdateRelease.class);
                    startActivity(i);

                }

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
