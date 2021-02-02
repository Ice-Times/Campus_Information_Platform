package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Release_Page extends AppCompatActivity {

    //private int[] images = {R.drawable.add_img2,};

    private List<Bitmap> BitmapList = new ArrayList<Bitmap>();
    private GridView gridImgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release__page);



        BitmapList.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_img2),
                300,300, true));//类型的转换);
        gridImgView = (GridView) findViewById(R.id.Release_Img_GridView);
        gridImgView.setAdapter(new MyAdapter(this,BitmapList));

        gridImgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.d(String.valueOf(position), "onItemClick: ");


                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);


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
                options.inSampleSize = 32;
                Bitmap bitmap_Pic = BitmapFactory.decodeFile(getRealFilePath(this, uri), options);



                BitmapList.add(Bitmap.createBitmap(bitmap_Pic,0,0,100,100));


                gridImgView.setAdapter(new MyAdapter(this,BitmapList));

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


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        //private int[] images;

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
            iv.setImageBitmap(BitmapList.get(position));

            return v;
        }
    }



}
