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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Release_Page extends AppCompatActivity {

    //private int[] images = {R.drawable.add_img2,};

    private List<Bitmap> BitmapList = new ArrayList<Bitmap>();
    private List<Bitmap> SendingImgList = new ArrayList<Bitmap>();

    private GridView gridImgView;

    private int gridImgViewSize=300;

    int clickNum=0;

    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release__page);



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
                            if (clickNum == 1) {//单击
                                Log.d("", "单击");

                            }else if(clickNum==2){//双击
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

                if(SendingImgList.size()==0)
                    SendingImgList.add(bitmap_Pic);
                else
                    SendingImgList.add(SendingImgList.size()-1,bitmap_Pic);

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



}
