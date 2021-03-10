package com.example.campusinformationplatform;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Personal_Page  extends Fragment {

    private Global_Value gv;

    private String Cache_Head_Path;

    //网络端口
    private String HOST;
    private int PORT;


    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;
    private PersonalpageFunctionAdapt adapt;

    private ListView listview;

    private ImageView PersonalPage_Headimg;
    private TextView PersonalPage_Username;

    private TextView PersonalPage_UserNumOfInf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_page, container,false);


        gv = (Global_Value) getActivity().getApplication();
        Cache_Head_Path=gv.getCache_Head_Path();

        HOST = gv.getHost();
        PORT = gv.getPort();

        System.out.println("进入person");
        //view.setClickable(true);

        listview =(ListView)view.findViewById(R.id.PersonalPage_Listview);
        listItem = new ArrayList<HashMap<String, Object>>();

        initItem();

        adapt = new PersonalpageFunctionAdapt(getContext(), listItem);
        //setListViewHeightBasedOnChildren(listview);
        listview.setAdapter(adapt);


        PersonalPage_Headimg=(ImageView)view.findViewById(R.id.PersonalPage_Headimg);
        PersonalPage_Username=(TextView) view.findViewById(R.id.PersonalPage_Username);

        PersonalPage_Username.setText(gv.getUserName());

        PersonalPage_UserNumOfInf=(TextView) view.findViewById(R.id.PersonalPage_UserNumOfInf);

        PersonalPage_Headimg.setImageBitmap(openImage(Cache_Head_Path+gv.getUserName()+".jpg"));

        Thread infThread = new Thread(new Runnable() {
            public void run() {
                try {
                    String state = Status.GetUserNumberOfInf;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);

                    Sending.put("Username", gv.getUserName());

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

                    JSONObject Json_msg = new JSONObject(s);

                    final String NumOfRelease=Json_msg.getString("NumOfRelease");
                    final String NumOfMessage=Json_msg.getString("NumOfMessage");

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            PersonalPage_UserNumOfInf.setText("发布： "+NumOfRelease+"    留言： "+NumOfMessage);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        infThread.start();



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(String.valueOf(id), "onItemClick: ");

                if(position==0){
                    //我的收藏
                    Intent i = new Intent(getActivity() , FavoriteSelf_Page.class);
                    startActivity(i);
                }
                else if(position==1){
                    //我的发布
                    Intent i = new Intent(getActivity() , ReleaseSelf_Page.class);
                    startActivity(i);
                }else if(position==2){
                    //我的留言
                    Intent i = new Intent(getActivity() , MessageSelf_Page.class);
                    startActivity(i);
                }
                else if(position==3){
                    //退出登录
                    SharedPreferences preferences = getActivity().getSharedPreferences("Name_Schoolname", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences .edit();
                    editor.putString("UserName", null);
                    editor.putString("SchoolName", null);
                    editor.commit();//写入

                    Intent i = new Intent(getActivity() , SignIn_Page.class);
                    startActivity(i);
                }



            }
        });

        return view;
    }

    private void initItem(){
        HashMap<String, Object> Releaseitem= new HashMap<String, Object>();
        Releaseitem.put("Functionicon",R.mipmap.releaseicon);
        Releaseitem.put("Functiontext","我的发布");

        HashMap<String, Object> Messageitem= new HashMap<String, Object>();
        Messageitem.put("Functionicon",R.mipmap.messageicon);
        Messageitem.put("Functiontext","我的留言");

        HashMap<String, Object> Favoriteitem= new HashMap<String, Object>();
        Favoriteitem.put("Functionicon",R.mipmap.messageicon);
        Favoriteitem.put("Functiontext","我的收藏");


        HashMap<String, Object> Logoutitem= new HashMap<String, Object>();
        Logoutitem.put("Functionicon",R.mipmap.logouticon);
        Logoutitem.put("Functiontext","退出登录");

        listItem.add(Favoriteitem);
        listItem.add(Releaseitem);
        listItem.add(Messageitem);
        listItem.add(Logoutitem);

    }


    public Bitmap openImage(final String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            if(bitmap==null)
                throw new FileNotFoundException();

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.d("tx不存在", "openHeadImage:");
            //从服务器获取头像
            final Bitmap[] bb = {null};
            Thread himg = new Thread(new Runnable() {
                public void run() {
                    try {
                        String state = Status.GetUserHeadImg;
                        Socket socket = new Socket(HOST, PORT + 2);
                        JSONObject Sending = new JSONObject();

                        String Username = path.replace(Cache_Head_Path, "");
                        Username = Username.replace(".jpg", "");

                        Sending.put("Status", state);

                        Sending.put("Username", Username);

                        //写入String
                        String msg = Sending.toString();
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        outputStream.writeUTF(msg);

                        outputStream.flush();

                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                        System.out.println("接收服务器的头像");
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

                        bb[0] = BitmapFactory.decodeByteArray(data, 0, data.length);


                        FileOutputStream fileOutputStream =
                                new FileOutputStream(Cache_Head_Path+Username+".jpg");
                        bb[0].compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();


                        outputStream.close();
                        socket.close();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            himg.start();
            try {
                himg.join();
            }catch (Exception ex) {
                ex.printStackTrace();
            }

            return bb[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
