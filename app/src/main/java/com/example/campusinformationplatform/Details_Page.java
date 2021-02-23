package com.example.campusinformationplatform;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
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

public class Details_Page extends AppCompatActivity {
    private Global_Value gv;

    //网络端口
    private String HOST;
    private int PORT;

    private String Cache_Head_Path;

    private String Type;
    private String Username;
    private String Release_date;
    private String ddescribe;
    private String Picture_num;
    private String Releaseid="";

    public ImageView Emptyitems;

    public TextView DetailsType;
    public ImageView DetailsUserheadImg;
    public TextView DetailsUsername;
    public TextView DetailsReleasedate;
    public TextView DetailsDescribe;

    public ImageView DetailsPicrelease1;
    public ImageView DetailsPicrelease2;
    public ImageView DetailsPicrelease3;
    public ImageView DetailsPicrelease4;
    public ImageView DetailsPicrelease5;
    public ImageView DetailsPicrelease6;


    public EditText Message_EditText;
    public Button ReleaseMessage_Bt;
    //储存数据
    private ArrayList<HashMap<String, Object>> listItem;
    private MessagelistAdapt adapt;

    private ListView listview;

    private Context context;

    public int ImgViewSize;
    public static int px2dip(Context context, float pxValue) {
              final float scale = context.getResources().getDisplayMetrics().density;
              return (int) (pxValue / scale + 0.5f);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__page);

        //禁止输入法自动弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //隐藏标题栏
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        gv = (Global_Value) getApplication();

        HOST = gv.getHost();
        PORT = gv.getPort();

        Cache_Head_Path = gv.getCache_Head_Path();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）

//        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
//        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
//        int screenWidth = (int) (width/density);//屏幕宽度(dp)
//        int screenHeight = (int)(height/density);//屏幕高度(dp)
//
//        Log.d("widthPixels: ", String.valueOf(width));
//        Log.d("density: ", String.valueOf(density));
//        Log.d("ImgViewSize: ", String.valueOf(ImgViewSize));
        ImgViewSize=width;

        Log.d("ImgViewSize: ", String.valueOf(ImgViewSize));

        DetailsType=(TextView)findViewById(R.id.Details_Type);
        DetailsUsername=(TextView)findViewById(R.id.Details_UserName);
        DetailsReleasedate=(TextView)findViewById(R.id.Details_ReleaseDate);
        DetailsDescribe=(TextView)findViewById(R.id.Details_Describe);
        DetailsUserheadImg=(ImageView)findViewById(R.id.Details_UserHeadImg);

        DetailsPicrelease1=(ImageView)findViewById(R.id.Details_PicRelease1);
        DetailsPicrelease2=(ImageView)findViewById(R.id.Details_PicRelease2);
        DetailsPicrelease3=(ImageView)findViewById(R.id.Details_PicRelease3);
        DetailsPicrelease4=(ImageView)findViewById(R.id.Details_PicRelease4);
        DetailsPicrelease5=(ImageView)findViewById(R.id.Details_PicRelease5);
        DetailsPicrelease6=(ImageView)findViewById(R.id.Details_PicRelease6);

        Emptyitems=(ImageView)findViewById(R.id.Emptyitems);

        context=this;

        listview =(ListView)findViewById(R.id.Details_Messagelistview);
        listItem = new ArrayList<HashMap<String, Object>>();

        adapt = new MessagelistAdapt(context, listItem);
        setListViewHeightBasedOnChildren(listview);
        listview.setAdapter(adapt);

        listview.setEmptyView(Emptyitems);





        Message_EditText=(EditText)findViewById(R.id.Message_EditText);

        ReleaseMessage_Bt=(Button) findViewById(R.id.ReleaseMessage_Bt);

        ReleaseMessage_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Thread(new Runnable() {
                    public void run() {
                        try {
                            String state = Status.SendMessage_State;
                            Socket socket = new Socket(HOST, PORT);
                            JSONObject Sending = new JSONObject();

                            Sending.put("Status", state);
                            Sending.put("ReleaseId", Releaseid);
                            Sending.put("Username", gv.getUserName());
                            Sending.put("Message", Message_EditText.getText().toString());


                            //写入String
                            String msg = Sending.toString();

                            System.out.println("发送数据："+msg);
                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                            outputStream.writeUTF(msg);

                            outputStream.flush();

                            outputStream.close();
                            socket.close();

                            String Re_State="";
                            socket = new Socket(HOST, PORT);

                            DataInputStream inputStream=new DataInputStream(socket.getInputStream());

                            try{
                                System.out.println("接收服务器的数据");
                                Re_State=inputStream.readUTF();

                            }catch(Exception e){
                                System.out.println("接收服务器数据异常");
                                e.printStackTrace();
                            }

                            Log.d("服务器发送的数据为 ", Re_State);

                            inputStream.close();
                            socket.close();


                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            final String finalRe_State = Re_State;
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if(finalRe_State.equals(Status.Re_Message_Success))
                                        Show_Message_Success();
                                    else
                                        Show_Message_Err();


                                }
                            });

//                            Looper.prepare();//增加部分
//                            if(finalRe_State.equals(Status.Re_Message_Success))
//                                Show_Message_Success();
//                            else
//                                Show_Message_Err();
//                            Looper.loop();//增加




                        }catch (Exception e){
                            e.printStackTrace();
                        }




                    }
                })).start();

            }
        });

        Message_EditText.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum= s;//实时记录输入的字数
            }
            @Override
            public void afterTextChanged(Editable s) {
                //当前字数
                int number = s.length();
                if(number==0){
                    ReleaseMessage_Bt.setEnabled(false);
                }
                else{
                    ReleaseMessage_Bt.setEnabled(true);
                }

            }
        });





        Thread infThread=new Thread(new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = new Socket(HOST, PORT);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    String msg = "";
                    try {
                        System.out.println("infthread接收服务器的数据");
                        msg = inputStream.readUTF();

                    } catch (Exception e) {
                        System.out.println("infthread接收服务器数据异常");
                        e.printStackTrace();
                    }

                    Log.d("infthread服务器发送的数据为 ", msg);

                    inputStream.close();
                    socket.close();

                    JSONObject Json_msg = new JSONObject(msg);

                    Releaseid=Json_msg.getString("releaseid");
                    Type=Json_msg.getString("type");
                    Username=Json_msg.getString("username");
                    Release_date=Json_msg.getString("release_date");
                    ddescribe=Json_msg.getString("ddescribe");
                    Picture_num=Json_msg.getString("picture_num");

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap headimg = openImage(Cache_Head_Path + Username+".jpg");

                                        DetailsType.setText(Type);
                                        DetailsUsername.setText(Username);
                                        DetailsReleasedate.setText(Release_date);
                                        DetailsDescribe.setText(ddescribe);
                                        DetailsUserheadImg.setImageBitmap(headimg);

                                    }
                                });



                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }));

        Thread picThread = new Thread(new Runnable() {
            public void run() {
                try {
                    int num=Integer.valueOf(Picture_num);

                    for(int i=0;i<num;i++) {
                        String state = Status.GetInformtionDetailsPic_State;

                        JSONObject Sending = new JSONObject();
                        Sending.put("Status", state);
                        Sending.put("ReleaseId", Releaseid);
                        Sending.put("PictureId", i + 1);

                        Socket socket = new Socket(HOST, PORT+1);

                        String smsg = Sending.toString();
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        outputStream.writeUTF(smsg);

                        System.out.println("fs的数据:"+smsg);
                        outputStream.flush();

                        outputStream.close();
                        socket.close();

                        socket = new Socket(HOST, PORT+1);
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                        try {
                            System.out.println("pic接收服务器的数据");
                            long size = inputStream.readLong();
                            byte[] data = new byte[(int) size];
                            int len = 0;
                            while (len < size) {
                                len += inputStream.read(data, len, (int) size - len);
                            }

                            if (data == null)
                                Log.d("data", "null");

                            socket.close();

                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                            final Bitmap bbitmap=getScaleBitmap(bitmap);

                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            final int finalI = i+1;
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(finalI ==1)
                                        DetailsPicrelease1.setImageBitmap(bbitmap);
                                    else if(finalI ==2)
                                        DetailsPicrelease2.setImageBitmap(bbitmap);
                                    else if(finalI ==3)
                                        DetailsPicrelease3.setImageBitmap(bbitmap);
                                    else if(finalI ==4)
                                        DetailsPicrelease4.setImageBitmap(bbitmap);
                                    else if(finalI ==5)
                                        DetailsPicrelease5.setImageBitmap(bbitmap);
                                    else if(finalI ==6)
                                        DetailsPicrelease6.setImageBitmap(bbitmap);

                                }
                            });
                            System.out.println("接收cg");

                        }catch (Exception e) {
                            System.out.println("接收服务器数据异常");
                            e.printStackTrace();
                        }
                    }


//                    for (int i = 0; i < listItem.size(); i++) {
//                        if (listItem.get(i).get("Picrelease") == null) {
//                            System.out.println("添加图片："+listItem.get(i).get("releaseid"));
//                            String state = Status.GetInformtionPic_State;
//                            socket = new Socket(HOST, PORT);
//                            JSONObject Sending = new JSONObject();
//
//                            Sending.put("Status", state);
//                            Sending.put("releaseid", listItem.get(i).get("releaseid"));
//
//                            String smsg = Sending.toString();
//
//                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//                            outputStream.writeUTF(smsg);
//
//                            outputStream.flush();
//
//                            outputStream.close();
//                            socket.close();
//
//
//                            socket = new Socket(HOST, PORT);
//                            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//
//                            try {
//                                System.out.println("接收服务器的数据");
//                                long size = inputStream.readLong();
//                                byte[] data = new byte[(int) size];
//                                int len = 0;
//                                while (len < size) {
//                                    len += inputStream.read(data, len, (int) size - len);
//                                }
//
//
//                                //ByteArrayOutputStream outPut = new ByteArrayOutputStream();
//                                if (data == null)
//                                    Log.d("data", "null");
//
//                                //inputStream.close();
//                                socket.close();
//
//                                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//                                final Bitmap showBitmap = getScaleBitmap(bitmap);
//
//                                final int finalI = i;
//                                Handler mainHandler = new Handler(Looper.getMainLooper());
//                                mainHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //Bitmap headimg = openImage(Cache_Head_Path + "ice.jpg");
//                                        listItem.get(finalI).remove("Picrelease");
//                                        listItem.get(finalI).put("Picrelease", showBitmap);
//
//                                        adapt.notifyDataSetChanged();
//
//
//                                    }
//                                });
//                                System.out.println("接收服务器数据成功");
//
//
//                                socket = new Socket(HOST, PORT);
//                                outputStream = new DataOutputStream(socket.getOutputStream());
//                                outputStream.writeUTF(smsg);
//                                System.out.println("发送图片信息");
//
//                                outputStream.flush();
//
//                                outputStream.close();
//                                socket.close();
//
//
//                            } catch (Exception e) {
//                                System.out.println("接收服务器数据异常");
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }

                } catch (Exception e) {
                    System.out.println("接收服务器数据异常");
                    e.printStackTrace();
                }


            }
        });





        Thread messageThread=new Thread(new Thread(new Runnable() {
            public void run() {
                try {
                    String state = Status.GetMessage_State;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);
                    Sending.put("ReleaseId", Releaseid);

                    //写入String
                    String msg = Sending.toString();

                    System.out.println("messagethread发送数据："+msg);
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    outputStream.close();
                    socket.close();

                    //接收状态
                    socket = new Socket(HOST, PORT);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    String s = "";
                    try {
                        System.out.println("messagethread接收服务器的数据");
                        //GetRowNumber=Integer.parseInt(inputStream.readUTF());
                        s = inputStream.readUTF();

                    } catch (Exception e) {
                        System.out.println("messagethread接收服务器数据异常");
                        e.printStackTrace();
                    }
                    Log.d("messagethread服务器发送的数据为 ", s);

                    inputStream.close();
                    socket.close();

                    JSONArray Server_JsonArray = new JSONArray(s);
                    //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < Server_JsonArray.length(); i++) {
                        HashMap<String, Object> item = new HashMap<String, Object>();

                        JSONObject jo = Server_JsonArray.getJSONObject(i);
                        Bitmap headimg = openImage(Cache_Head_Path + jo.getString("username") + ".jpg");

                        item.put("MessageUsername", jo.getString("username"));
                        item.put("MessageReleasedate", jo.getString("release_date"));
                        item.put("Message", jo.getString("message"));
                        item.put("MessageUserheadImg", headimg);
                        item.put("Messageid", jo.getString("messageid"));

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
        }));

        infThread.start();
        try {
            infThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        picThread.start();

        messageThread.start();


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


    private Bitmap getScaleBitmap(Bitmap bm){
        int bitmapWidth=bm.getWidth();
        int bitmapHeight=bm.getHeight();
        float scaleWidth;
        float scaleHeight;

        int newWidth;
        int newHeight;

        float scale=(float)bitmapWidth/(float)bitmapHeight;
        if(scale>=1){//比较宽,正方形

            newWidth=ImgViewSize-150;
            newHeight=(int)(newWidth/scale);


//            newHeight=ImgViewSize;
//            newWidth=(int)(newHeight*scale);
        }
        else{//比较长

//            newHeight=ImgViewSize+80;
//            newWidth=(int)(newHeight*scale);

            newWidth=ImgViewSize-150;
            newHeight=(int)(newWidth/scale);
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

    private void GetMessageFromServer(){
        new Thread(new Thread(new Runnable() {
            public void run() {
                try {
                    String state = Status.GetMessage_State;
                    Socket socket = new Socket(HOST, PORT+1);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);
                    Sending.put("ReleaseId", Releaseid);


                    //写入String
                    String msg = Sending.toString();

                    System.out.println("发送数据："+msg);
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    outputStream.close();
                    socket.close();


                    //接收状态
                    socket = new Socket(HOST, PORT+1);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    String s = "";
                    try {
                        System.out.println("messagethread接收服务器的数据");
                        //GetRowNumber=Integer.parseInt(inputStream.readUTF());
                        s = inputStream.readUTF();

                    } catch (Exception e) {
                        System.out.println("messagethread接收服务器数据异常");
                        e.printStackTrace();
                    }

                    Log.d("messagethread服务器发送的数据为 ", s);

                    inputStream.close();
                    socket.close();

                    JSONArray Server_JsonArray = new JSONArray(s);
                    //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < Server_JsonArray.length(); i++) {
                        HashMap<String, Object> item = new HashMap<String, Object>();

                        JSONObject jo = Server_JsonArray.getJSONObject(i);
                        Bitmap headimg = openImage(Cache_Head_Path + jo.getString("username") + ".jpg");

                        item.put("MessageUsername", jo.getString("username"));
                        item.put("MessageReleasedate", jo.getString("release_date"));
                        item.put("Message", jo.getString("message"));
                        item.put("MessageUserheadImg", headimg);
                        item.put("Messageid", jo.getString("messageid"));

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
        })).start();


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void Show_Message_Success() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("留言成功").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        RefershMessage();
                        Message_EditText.setText("");
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void Show_Message_Err() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("留言失败").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void RefershMessage(){

        Thread messageThread=new Thread(new Thread(new Runnable() {
            public void run() {
                try {

                    String state = Status.GetMessage_State;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);
                    Sending.put("ReleaseId", Releaseid);


                    //写入String
                    String msg = Sending.toString();

                    System.out.println("messagethread发送数据："+msg);
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    outputStream.close();
                    socket.close();


                    //接收状态
                    socket = new Socket(HOST, PORT);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    String s = "";
                    try {
                        System.out.println("messagethread接收服务器的数据");
                        //GetRowNumber=Integer.parseInt(inputStream.readUTF());
                        s = inputStream.readUTF();

                    } catch (Exception e) {
                        System.out.println("messagethread接收服务器数据异常");
                        e.printStackTrace();
                    }

                    Log.d("messagethread服务器发送的数据为 ", s);

                    inputStream.close();
                    socket.close();

                    JSONArray Server_JsonArray = new JSONArray(s);
                    //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < Server_JsonArray.length(); i++) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        JSONObject jo = Server_JsonArray.getJSONObject(i);

                        System.out.println("messageid: "+jo.getString("messageid"));
                        boolean gea=false;
                        for(int n=0; n<listItem.size();n++){
                            System.out.println("listItemmessageid: "+listItem.get(n).get("Messageid"));
                            if(listItem.get(n).get("Messageid").equals(jo.getString("messageid"))){
                                gea=true;
                            }
                        }

                        if(gea==true)
                            break;;

                        Bitmap headimg = openImage(Cache_Head_Path + jo.getString("username") + ".jpg");

                        item.put("MessageUsername", jo.getString("username"));
                        item.put("MessageReleasedate", jo.getString("release_date"));
                        item.put("Message", jo.getString("message"));
                        item.put("MessageUserheadImg", headimg);
                        item.put("Messageid", jo.getString("messageid"));

                        listItem.add(0,item);

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
        }));

        messageThread.start();
    }


}
