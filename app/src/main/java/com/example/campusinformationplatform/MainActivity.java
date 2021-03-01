package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    //全局变量
    private Global_Value gv;
    //网络端口
    private int PORT;
    private String HOST;
    private ProgressBar progress_bar;

    private String Cache_Head_Path;

    private TextView progress_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        gv = (Global_Value) getApplication();

        HOST = gv.getHost();
        PORT=gv.getPort();

        Cache_Head_Path=gv.getCache_Head_Path();

        progress_textview=(TextView)findViewById(R.id.progress_textview);

        if(getSupportActionBar()!=null){

            getSupportActionBar().hide();

            getWindow().setFlags(

                    WindowManager.LayoutParams.FLAG_FULLSCREEN,

                    WindowManager.LayoutParams.FLAG_FULLSCREEN

            );

        }

        progress_bar = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = new Socket(HOST, PORT+2);

                    JSONObject Sending=new JSONObject();
                    Sending.put("Status", Status.Buffer_State);

                    //写入String
                    String msg = Sending.toString();
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    String Username=inputStream.readUTF();
                    while(!Username.equals("eof")){

                        long size = inputStream.readLong();
                        byte[] data = new byte[(int) size];
                        int len = 0;
                        while (len < size) {
                            len += inputStream.read(data, len, (int) size - len);
                        }

                        File file = new File(Cache_Head_Path+Username+".jpg");
                        FileOutputStream fstream = new FileOutputStream(file);
                        BufferedOutputStream stream= new BufferedOutputStream(fstream);
                        stream.write(data);

                        stream.close();
                        fstream.close();

                        Username=inputStream.readUTF();
                    }

                    inputStream.close();
                    socket.close();


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        progress_textview.setText("正在载入用户数据");
        final Intent intent=new Intent(this,SignIn_Page.class);
        Timer timer=new Timer();
        int DELAY=2*1000;
        TimerTask task=new TimerTask()
        {
            @Override
            public void run(){
                startActivity(intent);
            }
        };
        timer.schedule(task,DELAY);




    }
}
