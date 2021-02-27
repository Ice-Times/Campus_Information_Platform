package com.example.campusinformationplatform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


        SharedPreferences preferences = getSharedPreferences("Name_Schoolname", Activity.MODE_PRIVATE);
        String UUserName = preferences.getString("UserName",null);
        String SchoolName = preferences.getString("SchoolName", null);
        if(UUserName!=null){
        Log.d("UserName", UUserName);
        Log.d("SchoolName", SchoolName);
        }
        if(UUserName!=null) {
            gv.setUserName(UUserName);
            gv.setSchoolName(SchoolName);
            Intent i = new Intent(SignIn_Page.this , Main_Page.class);
            i.putExtra("Inf","SignIn");
            startActivity(i);
            //startActivityForResult(i, 1);
        }


        Button To_ForgetPassword_Page=(Button)findViewById(R.id.To_ForgetPassword_Page_Bt);
        //登录
        SignIn_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckUserName() && CheckUserPassword()) {

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String state = Status.SignIn_State;
                                Socket socket = new Socket(HOST, PORT);


                                try {
                                    JSONObject Sending = new JSONObject();

                                    Sending.put("Status", state);
                                    Sending.put("UserName", UserName.getText());
                                    Sending.put("UserPassword", UserPassword.getText());
                                    String result = Sending.toString();

                                    System.out.println("登录信息：" + result);


                                    //写入String
                                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                    outputStream.writeUTF(result);

                                    outputStream.flush();

                                    //outputStream.close();
                                    //socket.close();


                                    //接收状态
                                    //socket = new Socket(HOST, PORT);

                                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                                    try {
                                        System.out.println("接收服务器的数据");
                                        Sign_In_State = inputStream.readUTF();

                                    } catch (Exception e) {
                                        System.out.println("接收服务器数据异常");
                                        e.printStackTrace();
                                    }

                                    Log.d("服务器发送的数据为 ", Sign_In_State);

                                    inputStream.close();
                                    socket.close();


                                    final JSONObject js = new JSONObject(Sign_In_State);

                                    new Handler(getApplicationContext().getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                String state = js.getString("Status");

                                                if (state.equals(Status.Re_SignIn_Success)) {

                                                    SharedPreferences preferences = getSharedPreferences("Name_Schoolname", Activity.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("UserName", UserName.getText().toString());
                                                    editor.putString("SchoolName", js.getString("SchoolName"));
                                                    editor.commit();//写入


                                                    gv.setUserName(UserName.getText().toString());
                                                    gv.setSchoolName(js.getString("SchoolName"));
                                                    Intent i = new Intent(SignIn_Page.this, Main_Page.class);
                                                    i.putExtra("Inf", "SignIn");
                                                    startActivity(i);
                                                    //startActivityForResult(i, 1);
                                                } else {
                                                    Show_SignIn_Err1();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


//                                        gv.setUserName("ice");
//
//                                        Intent i = new Intent(SignIn_Page.this , Main_Page.class);
//                                        startActivity(i);

                                        }
                                    });


                                } catch (JSONException | IOException ex) {
                                    ex.printStackTrace();
                                }


                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }


                    }).start();

                }
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


        To_ForgetPassword_Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn_Page.this , ForgetPassword_Page.class);
                startActivity(i);
            }
        });

    }


    private void Show_SignIn_Err1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("密码错误或用户不存在").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public boolean CheckUserName() {
        //是否为空
        if (TextUtils.isEmpty(UserName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean CheckUserPassword() {
        //是否为空
        if (TextUtils.isEmpty(UserPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
