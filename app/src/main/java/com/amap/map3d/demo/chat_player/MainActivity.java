package com.amap.map3d.demo.chat_player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.Fragments.ConversationFragment;
import com.amap.map3d.demo.chat_player.Fragments.Fragment_message;
import com.amap.map3d.demo.chat_player.Fragments.Fragment_people;
import com.amap.map3d.demo.chat_player.activity.ActivityImgLoad;
import com.amap.map3d.demo.chat_player.activity.Search;
import com.amap.map3d.demo.chat_player.activity.Talking;
import com.amap.map3d.demo.chat_player.bean.User;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView circleImageView,cir1;
    private DrawerLayout linearLayout;
    private FragmentManager fm;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final String IMAGE_FILE_NAME = "header.jpg";
    private Fragment f1,f2,f3;
    private final int REQUEST_CODE = 1;
    private ImageView b1,b2,b3,imageView;
    private Button exitbtn,img,admin;
    private EditText sou;
    private LinearLayout layout;
    private TextView username,text;
    private PopupWindow mPopupWindow;
    private Toolbar mtoolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        //connect server
        final User user = BmobUser.getCurrentUser(User.class);
        /**
         * FIXME 连接前先判断uid收为空
         */
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        // Logger.i(bmobIMUserInfo.getUserId() + "\n" + bmobIMUserInfo.getName());
                    } else {
                        //Logger.e(e);
                    }
                }
            });
            //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        circleImageView= (CircleImageView) findViewById(R.id.circleImageView);
        linearLayout= (DrawerLayout) findViewById(R.id.dl_main);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout.isDrawerOpen(Gravity.LEFT))
                {
                    linearLayout.closeDrawers();
                }else {
                    linearLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        init();
    }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        admin= (Button) findViewById(R.id.admin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Talking.class));
            }
        });
        text= (TextView) findViewById(R.id.text);
        mtoolBar= (Toolbar) findViewById(R.id.toolBar);
        img= (Button) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ActivityImgLoad.class));
            }
        });
        exitbtn= (Button) findViewById(R.id.exit);
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("app",MODE_PRIVATE);
                preferences.edit().putBoolean("first_run",true).commit();
                SharedPreferences preferences1 = getSharedPreferences("app",MODE_PRIVATE);
                preferences1.edit().putString("first_run",null).commit();
                BmobIM.getInstance().disConnect();
                finish();
            }
        });
        sou= (EditText) findViewById(R.id.sou);
        sou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sou.setText("");
              startActivity(new Intent(MainActivity.this,Search.class));
            }
        });
        f1 = new ConversationFragment();
        f2 = new Fragment_people();
        f3 =  new Fragment_message();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.dl_container,f3).commit();
        fm.beginTransaction().add(R.id.dl_container,f2).commit();
        fm.beginTransaction().add(R.id.dl_container,f1).commit();
        b1= (ImageView) findViewById(R.id.btn_fragment1);
        b2= (ImageView) findViewById(R.id.btn_fragment2);
        b3= (ImageView) findViewById(R.id.btn_fragment3);
        imageView= (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpMyOverflow();
            }
        });
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b1.setImageResource(R.drawable.xiaoxi2);
        b2.setImageResource(R.drawable.lianxiren);
        b3.setImageResource(R.drawable.xinwen);
        username= (TextView) findViewById(R.id.username);
        SharedPreferences sharedata = getSharedPreferences("data", 0);
        String data = sharedata.getString("item", null);
        username.setText(" "+data);
    }

    private void popUpMyOverflow() {
        //获取状态栏的高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //获取状态栏高度+ToolBar高度
        int yOffset = frame.top+ mtoolBar.getHeight();
        if (mPopupWindow ==null)
        {
            //初始化popUpWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.diogal,null);
            //popView，true设置focusAble
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true才会有效)
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭PopUpWindow
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让她显示在右上角
            mPopupWindow.showAtLocation( mtoolBar,Gravity.RIGHT | Gravity.TOP,0,yOffset);
            //设置item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);
            popView.findViewById(R.id.ll_item3).setOnClickListener(this);
        }else {
            mPopupWindow.showAtLocation(mtoolBar,Gravity.RIGHT | Gravity.TOP,0,yOffset);
        }
    }

    private void alertDialog() {
        View myView = null;
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        myView = inflater.inflate(R.layout.diogal,null);
        //创建弹窗,添加视图
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(myView).create();
        //显示弹窗
        dialog.show();
        //隐藏弹窗
        Toast.makeText(this, "qqqqqqqqq", Toast.LENGTH_SHORT).show();
        //dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment1:
                fm.beginTransaction().replace(R.id.dl_container, f1).commit();
                linearLayout.closeDrawers();
                text.setText("消息");
                b1.setImageResource(R.drawable.xiaoxi2);
                b2.setImageResource(R.drawable.lianxiren);
                b3.setImageResource(R.drawable.xinwen);
                break;
            case R.id.btn_fragment2:
                fm.beginTransaction().replace(R.id.dl_container, f2).commit();
                linearLayout.closeDrawers();
                text.setText("联系人");
                b1.setImageResource(R.drawable.xiaoxi);
                b2.setImageResource(R.drawable.lianxiren2);
                b3.setImageResource(R.drawable.xinwen);
                break;
            case R.id.btn_fragment3:
                fm.beginTransaction().replace(R.id.dl_container, f3).commit();
                linearLayout.closeDrawers();
                text.setText("热门新闻");
                b1.setImageResource(R.drawable.xiaoxi);
                b2.setImageResource(R.drawable.lianxiren);
                b3.setImageResource(R.drawable.xinwen2);
                break;
            case R.id.ll_item1:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.ll_item2:
                startActivity(new Intent(this,Search.class));
                break;
            case R.id.ll_item3:
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
        //点击PopupWindow的item后，关闭popUpWindow
        if (mPopupWindow!=null && mPopupWindow.isShowing())
        {
            //关闭PopUpWindow
            mPopupWindow.dismiss();
        }
    }
    private Uri getImageUri() {//获取路径
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));
    }
    private boolean isSdcardExisting() {//推断SD卡是否存在
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode ==REQUEST_CODE)
        {
            //处理扫描结果(显示结果)
            if (data !=null)
            {
                Bundle bundle = data.getExtras();
                if (bundle == null)
                {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE)==CodeUtils.RESULT_SUCCESS){
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    toast(result);
                }else if (bundle.getInt(CodeUtils.RESULT_TYPE)==CodeUtils.RESULT_FAILED){
                    toast("解析二维码失败！");
                }
            }
        }
    }
    public void refresh() {
        onCreate(null);
    }
}
