package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.amap.map3d.demo.chat_player.R;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class Image_one extends Activity {
    private ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        init();
    }

    private void init() {
        img= (ImageView) findViewById(R.id.image_btn);
        Intent intent=getIntent();
        if(intent!=null)
        {
            byte [] bis=intent.getByteArrayExtra("bitmap");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
            img.setImageBitmap(bitmap);
        }
    }

}
