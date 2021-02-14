package com.example.campusinformationplatform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Page extends AppCompatActivity {

    //全局变量
    private Global_Value gv;
    //网络端口
    private int PORT;

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private ImageView Add_Image;

    //密保问题
    private static final String[] Sec_Qes = {
            "你在哪个学校就读?",
            "你班主任的名字是什么?",
            "你的出生年月是多少?",
            "你喜欢什么科目?"};

    //用户名
    private EditText UserName;
    //用户密码
    private EditText UserPassword;
    //用户头像
    //private ImageView UserImg;
    //用户密保问题
    private String UserSecQes;
    //用户密保答案
    private EditText UserSecAns;
    //注册按钮
    private Button Sign_Up_Bt;
    //
    private Bitmap User_Img;

    private String Cache_Temp_PATH;// = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp";
    //public String Cache_Temp_PATH= getExternalCacheDir().getAbsolutePath()+"/temp";

    private String Cache_Head_Path;

    Uri imageUri;


    private String Sign_Up_State = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__page);

        gv = (Global_Value) getApplication();


        Cache_Temp_PATH=gv.getCache_Temp_PATH();
        Log.d("getexternalCacheDir",Cache_Temp_PATH);

        Cache_Head_Path=gv.getCache_Head_Path();

        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        //添加用户头像
        Add_Image = (ImageView) findViewById(R.id.Sign_Up_Img_UserHeadImg);

        Add_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });

        //下拉密保问题
        Spinner spinner = (Spinner) findViewById(R.id.Spinner_Sec);
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, Sec_Qes);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);


        UserName = (EditText) findViewById(R.id.Sign_Up_EditText_UserName);
        UserPassword = (EditText) findViewById(R.id.Sign_Up_EditText_UserPassWord);
        UserSecAns = (EditText) findViewById(R.id.Sign_Up_EditText_SecQes);

        Sign_Up_Bt = (Button) findViewById(R.id.Sign_Up_Bt);
        Sign_Up_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //对账户信息进行验证
                if (CheckUserName() && CheckUserPassword() && CheckSecQes()) {
                    //验证成功，向服务器发送注册信息
                    final String HOST = gv.getHost();
                    PORT=gv.getPort();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                //Status s=new Status();
                                String state=Status.SignUp_State;
//                                String sendmsg = "";
//
//                                sendmsg = "\""+state+"\"   \"" + UserName.getText() + "\"    \""
//                                        + UserPassword.getText() + "\"    \""
//                                        + UserSecQes + "\"    \""
//                                        + UserSecAns.getText() + "\"";



                                Socket socket = new Socket(HOST, PORT);

                                //DataOutputStream outputStream=new DataOutputStream(socket.getOutputStream());
                                try {

                                    //--

                                    JSONObject Sending=new JSONObject();

                                    Sending.put("Status", state);
                                    Sending.put("UserName", UserName.getText());
                                    Sending.put("UserPassword", UserPassword.getText());
                                    Sending.put("UserSecQes", UserSecQes);
                                    Sending.put("UserSecAns", UserSecAns.getText());
                                    String  result=Sending.toString();

                                    System.out.println("注册信息：" + result);

//--

                                    //写入文字
                                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                    outputStream.writeUTF(result);

                                    //写入图片
                                    FileOutputStream fos = null;
                                    try {
                                        fos = new FileOutputStream(Cache_Temp_PATH + "temp.jpg");
                                        User_Img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                        fos.flush();

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        try {
                                            if (fos != null) {
                                                fos.close();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }



                                    File Temp_Img = new File(Cache_Temp_PATH, "temp.jpg");
                                    if (!Temp_Img.exists()) {
                                        Log.e("", "文件不存在");

                                    }

                                    Temp_Img = new File(Cache_Temp_PATH + "temp.jpg");
                                    outputStream.writeLong(Temp_Img.length());

                                    byte[] data = new byte[(int) Temp_Img.length()];
                                    FileInputStream inputStream = new FileInputStream(Temp_Img);
                                    inputStream.read(data);

                                    outputStream.write(data);

                                    outputStream.flush();

                                    inputStream.close();
                                    outputStream.close();


                                    Log.d("输出到服务器完成", "66 ");
                                } catch (Exception e) {
                                    Log.d("输出到服务器失败", "00 ");
                                }

                                socket.close();


                                socket = new Socket(HOST, PORT);

                                DataInputStream inputStream=new DataInputStream(socket.getInputStream());

                                try{
                                    System.out.println("接收服务器的数据");
                                    Sign_Up_State=inputStream.readUTF();

                                }catch(Exception e){
                                    System.out.println("接收服务器数据异常");
                                    e.printStackTrace();
                                }

                                Log.d("服务器发送的数据为 ", Sign_Up_State);

                                inputStream.close();
                                socket.close();

                                Looper.prepare();//增加部分
                                if(Sign_Up_State.equals(Status.Re_SignUp_Success)){
                                    //将用户头像存入cache
                                    FileOutputStream fileOutputStream =
                                            new FileOutputStream(Cache_Head_Path+UserName.getText()+".jpg");
                                    User_Img.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                    fileOutputStream.close();

                                    File ok=new File(Cache_Head_Path+UserName.getText()+".jpg");
                                    if(ok.exists()){
                                        Log.d("file exist", "用户 "+UserName+" 头像已存入cache");
                                    }

                                    Show_SignUp_Success();
                                }
                                else if(Sign_Up_State.equals(Status.Re_SignUp_UserName_Repeat_Err))
                                    Show_SignUp_Err1();
                                else
                                    Show_SignUp_Err2();
                                Looper.loop();//增加



                            } catch (Exception e) {
                                System.out.println("出错");
                                e.printStackTrace();
                            }
                        }
                    }).start();



                } else {
                    return;
                }


            }
        });


    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {

        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        imageUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis()+".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//输出路径
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        //intent.putExtra("return-data", true);

        intent.putExtra("return-data", false);


        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                //Bitmap bitmap = data.getParcelableExtra("data");

                Bitmap bitmap= null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    User_Img = bitmap;
                    Add_Image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }


        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            Log.d("", "onItemSelected: " + Sec_Qes[arg2]);
            UserSecQes = Sec_Qes[arg2];

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public boolean CheckUserName() {
        //是否为空
        if (TextUtils.isEmpty(UserName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //是否为2-8字母数字
        Pattern p = Pattern.compile("^\\w{2,8}$");
        Matcher m = p.matcher(UserName.getText().toString());
        if (!m.matches()) {
            Toast.makeText(getApplicationContext(), "用户名不为2-8字母数字", Toast.LENGTH_LONG).show();
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
        //确认密码是否为空
        EditText PasswordAck = (EditText) findViewById(R.id.Sign_Up_EditText_UserPassWordAck);
        if (TextUtils.isEmpty(PasswordAck.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请输入确认密码！", Toast.LENGTH_SHORT).show();
            return false;
        }


        //是否为6-12字母数字
        Pattern p = Pattern.compile("^\\w{2,12}$");
        Matcher m = p.matcher(UserPassword.getText().toString());
        if (!m.matches()) {
            Toast.makeText(getApplicationContext(), "密码不为6-12字母数字", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!UserPassword.getText().toString().equals(PasswordAck.getText().toString())) {
            Toast.makeText(getApplicationContext(), "两次密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public boolean CheckSecQes() {
        //是否为空
        if (TextUtils.isEmpty(UserSecAns.getText().toString())) {
            Toast.makeText(getApplicationContext(), "密保答案不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        //是否为1-12汉字字母数字
        Pattern p = Pattern.compile("^\\w{1,12}$");
        Matcher m = p.matcher(UserSecAns.getText().toString());
        if (!m.matches()) {
            Toast.makeText(getApplicationContext(), "密码不为汉字字母数字", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

//
//    public void WriteToFile(String destination, InputStream input) throws IOException {
//        int index;
//        byte[] bytes = new byte[1024];
//        FileOutputStream downloadFile = new FileOutputStream(destination);
//        while ((index = input.read(bytes)) != -1) {
//            downloadFile.write(bytes, 0, index);
//            downloadFile.flush();
//        }
//        input.close();
//        downloadFile.close();
//        //System.out.println("xie ru cg");
//    }

    private void Show_SignUp_Err1() {//用户存在

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("用户名已存在").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情

                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void Show_SignUp_Err2() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("请稍后重试").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void Show_SignUp_Success() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("注册成功").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情

                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }


}
