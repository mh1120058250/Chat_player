package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;

/**
 * 2017/7/18.
 */

import java.io.File;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.User;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ActivityImgLoad extends Activity implements OnClickListener {
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;

    private static final String IMAGE_FILE_NAME = "header.jpg";

    private ImageView mImageHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_load);
        setupViews();
    }

    private void setupViews() {
        mImageHeader = (ImageView) findViewById(R.id.image_header);
        Button selectBtn1 = (Button) findViewById(R.id.btn_selectimage);
        Button selectBtn2 = (Button) findViewById(R.id.btn_takephoto);
        selectBtn1.setOnClickListener(this);
        selectBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_selectimage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);;
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case R.id.btn_takephoto:
                if (isSdcardExisting()) {
                    Intent cameraIntent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");//拍照
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                    cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                } else {
                    Toast.makeText(v.getContext(), "请插入sd卡", Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri originalUri=data.getData();//获取图片uri
                    resizeImage(originalUri);
                    //以下方法将获取的uri转为String类型哦。
                    String []imgs1={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor=this.managedQuery(originalUri, imgs1, null, null, null);
                    int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url=cursor.getString(index);
                    upload(img_url);
                    //showToast(img_url);
                    break;
                case CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                        String []imgs={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                        Cursor cursor1=this.managedQuery(getImageUri(), imgs, null, null, null);
                        int index1=cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor1.moveToFirst();
                        String img_url1=cursor1.getString(index1);
                        upload(img_url1);
                        //showToast(img_url1);



                    } else {
                        Toast.makeText(ActivityImgLoad.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;

                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isSdcardExisting() {//推断SD卡是否存在
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void resizeImage(Uri uri) {//重塑图片大小
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//能够裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    private void showResizeImage(Intent data) {//显示图片
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            mImageHeader.setImageDrawable(drawable);
        }
    }

    private Uri getImageUri() {//获取路径
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));
    }

    /**
     * 将图片上传
     * @param imgpath
     */
    private void upload(String imgpath){
        final BmobFile icon = new BmobFile(new File(imgpath));
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null)
                {
                    User person = new User();
                    //person.setAvatar(icon);
                    person.save(new SaveListener() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e==null)
                            {
                                showToast("图片上传成功");
                            }else {
                                showToast("图片上传失败"+e.toString());
                                Log.e("",e.toString());
                            }
                        }

                        @Override
                        public void done(Object o, Object o2) {

                        }
                    });

                }

            }
        });
    }
}
