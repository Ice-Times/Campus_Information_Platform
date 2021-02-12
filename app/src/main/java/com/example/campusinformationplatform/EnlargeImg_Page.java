package com.example.campusinformationplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.FileOutputStream;
import java.util.ArrayList;


public class EnlargeImg_Page extends AppCompatActivity {
    private Global_Value gv;

    private ArrayList<Uri> EnlargeImage = new ArrayList<Uri>();

    private Banner mBanner;
    private ArrayList<String> images;
    private ArrayList<String> imageTitle;


    private String Cache_Path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_img__page);

        gv=(Global_Value) getApplication();

        EnlargeImage=gv.getEnlargeImage();

        Cache_Path=gv.getCachePath()+"/cache";

        //初始化数据
        initData();
        //初始化view
        initView();


    }



    private void initData() {
        //设置图片资源:url或本地资源
        images = new ArrayList<>();


        //images.add(R.drawable.add_img2);



//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        Bitmap bitmap_Pic = BitmapFactory.decodeFile(getRealFilePath(this, EnlargeImage.get(0)), options);

        Bitmap bt=getBitmapByFile(EnlargeImage.get(0).toString(),80,90);

        //images.add(EnlargeImage.get(0).toString());

        try {
            FileOutputStream fileOutputStream =
                    new FileOutputStream(Cache_Path + "oo.jpg");
            bt.compress(Bitmap.CompressFormat.JPEG, 10, fileOutputStream);

            fileOutputStream.close();
        }catch (Exception e){

        }

        images.add(Cache_Path+"oo.jpg");





        //images.add(getBitmapByFile(EnlargeImage.get(0).toString(),80,90));

        //设置图片标题:自动对应
        imageTitle = new ArrayList<>();
        imageTitle.add("十大星级品牌联盟，全场2折起");

        //imageTitle.add("嗨购5折不要停");


    }

    private void initView() {
        mBanner = findViewById(R.id.banner);
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR   显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(imageTitle);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        //可选样式:
        //Banner.LEFT   指示器居左
        //Banner.CENTER 指示器居中
        //Banner.RIGHT  指示器居右
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置是否允许手动滑动轮播图
        mBanner.setViewPagerIsScroll(true);
        //设置是否自动轮播（不设置则默认自动）
        mBanner.isAutoPlay(true);
        //设置轮播图片间隔时间（不设置默认为2000）
        mBanner.setDelayTime(1500);
        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(images)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Toast.makeText(EnlargeImg_Page.this, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();

    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .into(imageView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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


    public static Bitmap getBitmapByFile(String path, int width, int height) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, option);
        option.inSampleSize = Math.max(1, Math.min(option.outWidth / width, option.outHeight / height));
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, option);
    }

    public static Bitmap decodFromResource(Resources res, int resid, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resid, options);

        options.inSampleSize = calculateBitmapSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resid, options);


    }


    //计算Bitmap大小
    public static int calculateBitmapSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outHeight;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }



}
