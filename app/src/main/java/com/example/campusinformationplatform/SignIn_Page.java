package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class SignIn_Page extends AppCompatActivity {

    private Global_Value gv;

    //网络端口
    private String HOST;
    private int PORT;

    //用户名
    private EditText UserName;
    //用户密码
    private EditText UserPassword;

    private String Sign_In_State = null;

    private String Cache_Head_Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in__page);

        gv = (Global_Value) getApplication();
        HOST=gv.getHost();
        PORT=gv.getPort();

        Cache_Head_Path=gv.getCache_Head_Path();

        UserName=(EditText)findViewById(R.id.Sign_In_EditText_UserName);
        UserPassword=(EditText)findViewById(R.id.Sign_In_EditText_UserPassWord);

        Button SignIn_Bt=(Button)findViewById(R.id.Sign_In_Bt);

        //登录
        SignIn_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                new Thread(new Runnable() {
//                    public void run() {
//                        try {
//                            String state = Status.SignIn_State;
//                            Socket socket = new Socket(HOST, PORT);
//
//
//                            try {
//                                JSONObject Sending = new JSONObject();
//
//                                Sending.put("Status", state);
//                                Sending.put("UserName", UserName.getText());
//                                Sending.put("UserPassword", UserPassword.getText());
//                                String result = Sending.toString();
//
//                                System.out.println("登录信息：" + result);
//
//
//                                //写入String
//                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//                                outputStream.writeUTF(result);
//
//                                outputStream.flush();
//
//                                outputStream.close();
//                                socket.close();
//
//
//                                //接收状态
//                                socket = new Socket(HOST, PORT);
//
//                                DataInputStream inputStream=new DataInputStream(socket.getInputStream());
//
//                                try{
//                                    System.out.println("接收服务器的数据");
//                                    Sign_In_State=inputStream.readUTF();
//
//                                }catch(Exception e){
//                                    System.out.println("接收服务器数据异常");
//                                    e.printStackTrace();
//                                }
//
//                                Log.d("服务器发送的数据为 ", Sign_In_State);
//
//                                inputStream.close();
//                                socket.close();
//
//
//                            }catch (JSONException|IOException ex) {
//                                ex.printStackTrace();
//                            }
//
//
//                        } catch (SocketException e){
//                            e.printStackTrace();
//                        }catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//
//                    }
//
//
//
//                }).start();



                gv.setUserName("ice");
                Intent i = new Intent(SignIn_Page.this , Main_Page.class);
                startActivity(i);


            }




        });


        Button To_SignUp_Bt=(Button)findViewById(R.id.To_SignUp_Page_Bt);
        To_SignUp_Bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

//                Intent i = new Intent(SignIn_Page.this , SignUp_Page.class);
//                startActivity(i);

//                try {
////                    FileInputStream inputStream = new FileInputStream(Cache_Head_Path + "qw.jpg");
////                    inputStream.close();
////                    Log.d("", "kyky");
////                }catch (IOException ex) {
////                    ex.printStackTrace();
////                }


                Intent i = new Intent(SignIn_Page.this , SignUp_Page.class);
                startActivity(i);

            }
        });


    }
}
