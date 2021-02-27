package com.example.campusinformationplatform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


public class ForgetPassword_Page extends AppCompatActivity {

    private Global_Value gv;

    //网络端口
    private String HOST;
    private int PORT;

    private LinearLayout layout;

    private EditText ForgetPassword_EditText_UserName;
    private Button Confirm_Bt;

    private EditText ForgetPassword_EditText_SecAns;
    private TextView Forget_Sec_Qes;
    private Button Confirm_Bt2;

    private Context context;
    String user_sec_ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password__page);

        context=this;

        gv = (Global_Value) getApplication();
        HOST=gv.getHost();
        PORT=gv.getPort();

        layout=(LinearLayout)findViewById(R.id.Layout_Forget);

        ForgetPassword_EditText_UserName=(EditText)findViewById(R.id.ForgetPassword_EditText_UserName);
        Confirm_Bt=(Button) findViewById(R.id.Confirm_Bt);

        ForgetPassword_EditText_SecAns=(EditText)findViewById(R.id.ForgetPassword_EditText_SecAns);
        Forget_Sec_Qes=(TextView) findViewById(R.id.Forget_Sec_Qes);
        Confirm_Bt2=(Button) findViewById(R.id.Confirm_Bt2);

        Confirm_Bt2.setEnabled(false);

        Confirm_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String state = Status.SendForgetPassword_State;
                            Socket socket = new Socket(HOST, PORT);

                            try {
                                JSONObject Sending = new JSONObject();

                                Sending.put("Status", state);
                                Sending.put("UserName", ForgetPassword_EditText_UserName.getText());
                                String result = Sending.toString();


                                //写入String
                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                outputStream.writeUTF(result);

                                outputStream.flush();

                                outputStream.close();
                                socket.close();

                                System.out.println("登录信息：" + result);
                            }catch(Exception e){
                                System.out.println("接收服务器数据异常");
                                e.printStackTrace();
                            }

                        } catch (SocketException e){
                            e.printStackTrace();
                        }catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        //接收状态
                        Socket socket = null;
                        String s="";
                        try {
                            socket = new Socket(HOST, PORT);

                            DataInputStream inputStream=new DataInputStream(socket.getInputStream());

                            try{
                                System.out.println("接收服务器的数据");
                                s=inputStream.readUTF();

                            }catch(Exception e){
                                System.out.println("接收服务器数据异常");
                                e.printStackTrace();
                            }

                            Log.d("服务器发送的数据为 ", s);

                            inputStream.close();
                            socket.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                       try {
                           JSONObject Json_msg = new JSONObject(s);

                           String Re_state=Json_msg.getString("Status");

                           if(Re_state.equals(Status.Re_SignIn_User_Not_Exist_Err)){

                               Looper.prepare();//增加部分
                               Show_User_Not_Exist_Err();
                               Looper.loop();//增加
                           }
                           else if(Re_state.equals(Status.Re_SignIn_User_State_Locked_Err)){
                               Looper.prepare();//增加部分
                               Show_User_State_Locked_Err(Json_msg.getString("locked_reason"));
                               Looper.loop();//增加
                           }
                           else{
                               final String user_sec_qes=Json_msg.getString("user_sec_qus");
                               user_sec_ans=Json_msg.getString("user_sec_ans");

                               new Handler(context.getMainLooper()).post(new Runnable() {
                                   @Override
                                   public void run() {

                                       Confirm_Bt2.setEnabled(true);
                                       Forget_Sec_Qes.setText(user_sec_qes);
                                   }
                               });


                           }


                       }catch(Exception e){
                           System.out.println("数据异常");
                           e.printStackTrace();
                       }

                    }
                }).start();

            }
        });

        Confirm_Bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(user_sec_ans, ForgetPassword_EditText_SecAns.getText().toString());
                if(user_sec_ans.equals(ForgetPassword_EditText_SecAns.getText().toString())) {
                    final EditText et=new EditText(getApplicationContext());
                    et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    et.setHint("请输入新密码");

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layout.addView(et,p);
                    final EditText conet=new EditText(getApplicationContext());
                    conet.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    conet.setHint("请再次确认密码");

                    layout.addView(conet,p);


                    Button Confirm_Bt3=new Button(getApplicationContext());
                    Confirm_Bt3.setText("确定");
                    Confirm_Bt3.setTextColor(Color.BLACK);
                    Confirm_Bt3.setBackgroundColor(Color.WHITE);

                    Confirm_Bt3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(et.getText().toString().equals(conet.getText().toString())){

                                final String Password=et.getText().toString();
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            String state = Status.SendForgetPasswordUpdate_State;
                                            Socket socket = new Socket(HOST, PORT);

                                            try {
                                                JSONObject Sending = new JSONObject();

                                                Sending.put("Status", state);
                                                Sending.put("UpdatePassword", Password);
                                                Sending.put("UserName", ForgetPassword_EditText_UserName.getText());
                                                String result = Sending.toString();

                                                //写入String
                                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                                outputStream.writeUTF(result);

                                                outputStream.flush();

                                                outputStream.close();
                                                socket.close();

                                                System.out.println("登录信息：" + result);
                                            }catch(Exception e){
                                                System.out.println("接收服务器数据异常");
                                                e.printStackTrace();
                                            }

                                        } catch (SocketException e){
                                            e.printStackTrace();
                                        }catch (IOException ex) {
                                            ex.printStackTrace();
                                        }



                                    }
                                }).start();
                                Show_UpdatePassage_Suc();

                            }
                            else{
                                Show_PasswordNotLikely_Err();
                            }



                        }
                    });

                    layout.addView(Confirm_Bt3,p);

                    Confirm_Bt2.setEnabled(false);
                }
                else{
                    Show_User_SecAns_Err();
                }

            }
        });
    }

    private void Show_User_Not_Exist_Err() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("用户不存在").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void Show_PasswordNotLikely_Err() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("两次密码不一致").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
    private void Show_UpdatePassage_Suc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("设置新密码成功").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();

                        Intent intent = new Intent(ForgetPassword_Page.this , SignIn_Page.class);
                        startActivity(intent);


                    }
                });
        builder.create().show();
    }



    private void Show_User_State_Locked_Err(String res) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("用户被封禁")
                .setMessage("封禁原因："+res).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
    private void Show_User_SecAns_Err() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("密保答案错误").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

}
