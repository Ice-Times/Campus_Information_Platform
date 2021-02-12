package com.example.campusinformationplatform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Release_Page extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    //private int[] images = {R.drawable.add_img2,};

    private Global_Value gv;

    private String HOST;
    private int PORT;

    private List<Bitmap> BitmapList = new ArrayList<Bitmap>();
    private List<Bitmap> SendingImgList = new ArrayList<Bitmap>();

    private ArrayList<Uri> EnlargeImage = new ArrayList<Uri>();

    private GridView gridImgView;

    private int gridImgViewSize=300;

    int clickNum=0;

    private AlertDialog.Builder builder;

    private EditText title_Edittext;
    private EditText describe_Edittext;
    private CheckBox[] checkBoxes=new CheckBox[4];


    //缓存
    private String Cache_Temp_PATH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release__page);

        gv = (Global_Value) getApplication();
        HOST=gv.getHost();
        PORT=gv.getPort();

        Cache_Temp_PATH=gv.getCachePath()+"/temp";

        title_Edittext=(EditText)findViewById(R.id.Release_Title_EditTextview);
        describe_Edittext=(EditText)findViewById(R.id.Release_Describe_EditTextview);

        checkBoxes[0] = (CheckBox) findViewById(R.id.cb1);//二手交易
        checkBoxes[1] = (CheckBox) findViewById(R.id.cb2);//失物招领
        checkBoxes[2] = (CheckBox) findViewById(R.id.cb3);//寻物启事
        checkBoxes[3] = (CheckBox) findViewById(R.id.cb4);//其他

        checkBoxes[0].setOnCheckedChangeListener(this);
        checkBoxes[1].setOnCheckedChangeListener(this);
        checkBoxes[2].setOnCheckedChangeListener(this);
        checkBoxes[3].setOnCheckedChangeListener(this);



        BitmapList.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_img2),
                gridImgViewSize,gridImgViewSize, true));//类型的转换);
        gridImgView = (GridView) findViewById(R.id.Release_Img_GridView);
        gridImgView.setAdapter(new MyAdapter(this,BitmapList));

        gridImgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                Log.d(String.valueOf(position), "onItemClick: ");

                if(position==BitmapList.size()-1){//添加图片
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 2);

                }else{
                    clickNum++;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (clickNum == 1) {//单击,放大图片
                                Log.d("", "单击");

                                gv.setEnlargeImage(EnlargeImage);

                                Intent i = new Intent(Release_Page.this , EnlargeImg_Page.class);
                                startActivity(i);

                            }else if(clickNum==2){//双击,删除图片

                                Log.d("", "双击");

                                showWhetheOrNotToDeleteImg(position);

                            }
                            else{

                            }
                            //防止handler引起的内存泄漏
                            handler.removeCallbacksAndMessages(null);
                            clickNum = 0;
                        }
                    },300);
                }



            }
        });

        Button Release_Bt=(Button)findViewById(R.id.Release_Bt);
        Release_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "发布");
                new Thread(new Runnable() {
                    public void run() {
                        try{

                            String title=title_Edittext.getText().toString();
                            String describe=describe_Edittext.getText().toString();
                            String checkBoxName="";

                            for(int i=0;i<checkBoxes.length;i++)
                            {
                                if(checkBoxes[i].isChecked())
                                {
                                    if(i==0)
                                        checkBoxName="二手交易";
                                    else if(i==1)
                                        checkBoxName="失物招领";
                                    else
                                        checkBoxName="寻物启事";


                                }

                            }

                            Socket socket = new Socket(HOST, PORT);

                            String state=Status.Release_State;

                            try {
                                JSONObject Sending=new JSONObject();
                                Sending.put("UserName", "ice");
                                Sending.put("Status", state);
                                Sending.put("Type", checkBoxName);
                                Sending.put("Title", title);
                                Sending.put("Describe", describe);
                                Sending.put("NumOfImg", String.valueOf(SendingImgList.size()));

                                String  result=Sending.toString();

                                //写入文字
                                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                outputStream.writeUTF(result);

                                //传输图片
                                for(int i=0;i<SendingImgList.size();i++){

                                    FileOutputStream fos = null;
                                    try {
                                        fos = new FileOutputStream(Cache_Temp_PATH +String.valueOf(i)+".jpg");
                                        SendingImgList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

                                    File Temp_Img = new File(Cache_Temp_PATH+String.valueOf(i)+".jpg");
                                    if (!Temp_Img.exists()) {

                                        Log.e("", String.valueOf(i)+".jpg"+"不存在");

                                    }

                                   // Temp_Img = new File(Cache_Temp_PATH + String.valueOf(i)+".jpg");
                                    outputStream.writeLong(Temp_Img.length());
                                    byte[] data = new byte[(int) Temp_Img.length()];
                                    FileInputStream inputStream = new FileInputStream(Temp_Img);
                                    inputStream.read(data);

                                    outputStream.write(data);
                                    inputStream.close();


                                }


                                outputStream.flush();
                                outputStream.close();



                            }catch(Exception e){
                                System.out.println("发送异常");
                                e.printStackTrace();
                            }

                            socket.close();

                        }catch(Exception e){
                            System.out.println("发送异常");
                            e.printStackTrace();
                        }




                    }
                }).start();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();

                Log.e("uri", uri.toString());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap_Pic = BitmapFactory.decodeFile(getRealFilePath(this, uri), options);



                EnlargeImage.add(uri);


                //if(SendingImgList.size()==0)
                    SendingImgList.add(bitmap_Pic);
//                else
//                    SendingImgList.add(SendingImgList.size()-1,bitmap_Pic);

                System.out.println("bitmap_Pic H:"+String.valueOf( bitmap_Pic.getHeight()));
                System.out.println("bitmap_Pic W:"+String.valueOf( bitmap_Pic.getWidth()));

                Bitmap ScaleBitmap=getScaleBitmap(bitmap_Pic);

                System.out.println("temp H:"+String.valueOf( ScaleBitmap.getHeight()));
                System.out.println("temp W:"+String.valueOf( ScaleBitmap.getWidth()));

                Bitmap showPic;
                if(ScaleBitmap.getHeight()>ScaleBitmap.getWidth()){
                    //比较高
                    showPic = Bitmap.createBitmap(ScaleBitmap, 0, ScaleBitmap.getHeight()/2-150, 300, 300);

                }
                else{
                    showPic = Bitmap.createBitmap(ScaleBitmap, ScaleBitmap.getWidth()/2-150, 0, 300, 300);
                }



                BitmapList.add(BitmapList.size()-1,showPic);
                //BitmapList.add(showPic);

                gridImgView.setAdapter(new MyAdapter(this,BitmapList));




            }
        }
    }

    private void showWhetheOrNotToDeleteImg(final int position) {

        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("11")
                .setMessage("是否删除图片？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SendingImgList.remove(position-1);
                BitmapList.remove(position);
                gridImgView.setAdapter(new MyAdapter(getApplicationContext(),BitmapList));


                Toast.makeText(getApplicationContext(), "删除图片成功",Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
        }
        });



        builder.create().show();
    }



    //url
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);

            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
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
           newHeight=gridImgViewSize;
           newWidth=(int)(newHeight*scale);
        }
        else{//比较长
            newWidth=gridImgViewSize;
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


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        private List<Bitmap> BitmapList = new ArrayList<Bitmap>();

        public MyAdapter(Context context,List<Bitmap> BitmapList){
            this.BitmapList = BitmapList;

            layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return BitmapList.size();
        }

        @Override
        public Object getItem(int position) {
            return BitmapList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = layoutInflater.inflate(R.layout.gridimageviewitem,null);

            ImageView iv = (ImageView) v.findViewById(R.id.image_gridView_item);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridImgViewSize,
                    gridImgViewSize);
            iv.setLayoutParams(params);

            iv.setImageBitmap(BitmapList.get(position));

            return v;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        Log.d("111", "onCheckedChanged: ");
        if(b) {
            for (int i = 0; i < checkBoxes.length; i++) {
                //不等于当前选中的就变成false
                if (checkBoxes[i].getText().toString().equals(compoundButton.getText().toString())) {
                    checkBoxes[i].setChecked(true);
                } else {
                    checkBoxes[i].setChecked(false);
                }
            }
        }
    }

}
