package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SignUp_Page extends AppCompatActivity {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private ImageView Add_Image;

    //密保问题
    private static final String[] Sec_Qes={
            "你在哪个学校就读?",
            "你班主任的名字是什么?",
            "你的出生年月是多少?",
            "你喜欢什么科目?"};

    //用户名
    private EditText UserName;
    //用户密码
    private EditText UserPassword;
    //用户头像
    private ImageView UserImg;
    //用户密保问题
    private String UserSecQes;
    //用户密保答案
    private EditText UserSecAns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__page);

        //添加用户头像
        Add_Image= (ImageView) findViewById(R.id.sign_Up_Img_UserHeadImg);

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
                (this,android.R.layout.simple_spinner_item,Sec_Qes);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);



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
        intent.putExtra("outputX", 55);
        intent.putExtra("outputY", 55);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, "");//输出路径
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);


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
                Bitmap bitmap = data.getParcelableExtra("data");
                Add_Image.setImageBitmap(bitmap);

            }


        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
           // Spinner_Textview.setText("你的血型是："+Sec_Qes[arg2]);
            Log.d("", "onItemSelected: "+Sec_Qes[arg2]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}
