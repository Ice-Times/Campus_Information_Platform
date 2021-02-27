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
import android.graphics.Matrix;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UpdateRelease extends AppCompatActivity {
    private Global_Value gv;

    //网络端口
    private String HOST;
    private int PORT;

    private String Type;
    private String Title;
    private String Release_date;
    private String ddescribe;
    private String Picture_num;
    private String Releaseid="";


    public TextView UpdateType;
    public EditText UpdateTitle;

    public EditText UpdateDescribe;

    public ImageView UpdatePicrelease1;
    public ImageView UpdatePicrelease2;
    public ImageView UpdatePicrelease3;
    public ImageView UpdatePicrelease4;
    public ImageView UpdatePicrelease5;
    public ImageView UpdatePicrelease6;


    private List<Bitmap> BitmapList = new ArrayList<Bitmap>();
    private List<Bitmap> SendingImgList = new ArrayList<Bitmap>();

    private int gridImgViewSize=300;

    private GridView gridImgView;

    UpdateRelease.MyAdapter adapt;
    int clickNum=0;
    //描述中最大输入字数
    private int MaxInputWords = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_release);

        gv = (Global_Value) getApplication();

        HOST = gv.getHost();
        PORT = gv.getPort();

        UpdateTitle=(EditText)findViewById(R.id.Update_Release_Title_EditTextview);
        UpdateDescribe=(EditText)findViewById(R.id.Update_Release_Describe_EditTextview);


        BitmapList.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_img2),
                gridImgViewSize,gridImgViewSize, true));//类型的转换);
        gridImgView = (GridView) findViewById(R.id.Update_Release_Img_GridView);
        adapt=new UpdateRelease.MyAdapter(this,BitmapList);

        gridImgView.setAdapter(adapt);

        gridImgView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction() ? true
                        : false;
            }
        });




        Thread infThread=new Thread(new Thread(new Runnable() {
            public void run() {
                try {
                    String state = Status.GetInformtionDetails_State;
                    Socket socket = new Socket(HOST, PORT);
                    JSONObject Sending = new JSONObject();

                    Sending.put("Status", state);
                    Sending.put("releaseid", gv.getUpdate_Releaseid());

                    //写入String
                    String msg = Sending.toString();
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(msg);

                    outputStream.flush();

                    outputStream.close();
                    socket.close();


                    socket = new Socket(HOST, PORT);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    msg = "";
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
                    Title=Json_msg.getString("title");
                    Release_date=Json_msg.getString("release_date");
                    ddescribe=Json_msg.getString("ddescribe");
                    Picture_num=Json_msg.getString("picture_num");







                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            UpdateTitle.setText(Title);
                            UpdateDescribe.setText(ddescribe);

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


                            final Bitmap showPic;
                            if(bitmap.getHeight()>bitmap.getWidth()){
                                //比较高
                                showPic = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2-150, 300, 300);

                            }
                            else{
                                showPic = Bitmap.createBitmap(bitmap, bitmap.getWidth()/2-150, 0, 300, 300);
                            }


                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    BitmapList.add(BitmapList.size()-1,showPic);
                                    adapt.notifyDataSetInvalidated();

                                }
                            });




                            System.out.println("接收cg");

                        }catch (Exception e) {
                            System.out.println("接收服务器数据异常");
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    System.out.println("接收服务器数据异常");
                    e.printStackTrace();
                }


            }
        });


        infThread.start();
        try {
            infThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        picThread.start();

        final TextView WordCount=(TextView)findViewById(R.id.Update_Release_WordCount_Textview);

        UpdateDescribe.addTextChangedListener(new TextWatcher() {
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
                //TextView显示剩余字数
                selectionStart=UpdateDescribe.getSelectionStart();
                selectionEnd = UpdateDescribe.getSelectionEnd();
                WordCount.setText("" + number+"/"+MaxInputWords);

                if(wordNum.length() == MaxInputWords){
                    WordCount.setTextColor(android.graphics.Color.RED);
                }
                else{
                    WordCount.setTextColor(android.graphics.Color.rgb(156, 156, 156));
                }

                if (wordNum.length() > MaxInputWords) {
                    WordCount.setTextColor(android.graphics.Color.RED);
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    UpdateDescribe.setText(s);
                    UpdateDescribe.setSelection(tempSelection);//设置光标在最后

                }

            }
        });





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

 //                               gv.setEnlargeImage(EnlargeImage);

//                                Intent i = new Intent(Release_Page.this , EnlargeImg_Page.class);
//                                startActivity(i);

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



                //if(SendingImgList.size()==0)
               // SendingImgList.add(bitmap_Pic);
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
                adapt.notifyDataSetInvalidated();
                setListViewHeightBasedOnChildren(gridImgView);

            }
        }
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

    private void showWhetheOrNotToDeleteImg(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("11")
                .setMessage("是否删除图片？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //SendingImgList.remove(position-1);
                BitmapList.remove(position);
                gridImgView.setAdapter(new UpdateRelease.MyAdapter(getApplicationContext(),BitmapList));


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




    private class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        private List<Bitmap> BitmapList = new ArrayList<Bitmap>();

        public MyAdapter(Context context, List<Bitmap> BitmapList){
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

    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }


}
