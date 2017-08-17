package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.Adpter.ChatAdapter;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.application.BmobIMApplication;
import com.amap.map3d.demo.chat_player.bean.Recorder;
import com.amap.map3d.demo.chat_player.view.AudioRecordButton;
import com.amap.map3d.demo.chat_player.view.BackEditText;
import com.amap.map3d.demo.chat_player.view.MediaManager;
import com.amap.map3d.demo.chat_player.view.SildingFinishLayout;
import com.amap.map3d.demo.chat_player.view.SlideBackLayout;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, IKeyBoardVisibleListener,MessageListHandler {
    private Toolbar toolBar;
    private static final int CAMERA = 1025;
    private static final int ALBUM = 1026;
    private File mFolder;
    private String mImgName;
    private ImageView voiseOrKeybord;
    private Button sendBtn;
    private ImageView emoBtn;
    private BackEditText chatEditView;
    private AudioRecordButton audioRecordBtn;
    private ListView textListView;
    boolean isVisiableForLast = false;
    private int keyboardHeight;
    private LinearLayout moer;
    private BmobIMApplication app;
    private AudioRecordButton a;
    private boolean isET = true;
    int moreheight;
    List<BmobIMMessage> list1;
    private Boolean flag=false;
    private ImageView yuyin,paizhao,xiangpian,wenjian,dili,biaoqing;
    InputMethodManager imm = null;
    private LinearLayout sendImg;
    private LinearLayout sendLocation;
    private LinearLayout camera;
    private SlideBackLayout mSlideBackLayout;
    BmobIMConversation c;
    private SildingFinishLayout chatMain;
    private LinearLayout textLayout;
    private ChatAdapter adapter;
    private LinearLayout xianshi;
    private View viewanim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        mSlideBackLayout = new SlideBackLayout(this);
        mSlideBackLayout.bind();
        app= (BmobIMApplication) getApplication();
        c= BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getBundle().getSerializable("c"));
        Log.e("zxcv", "onCreate: " + c);
        initView();
        initView1();
        //查询会话
        getQueryMessages();
        scrollToBottom();
        addOnSoftKeyBoardVisibleListener(this, this);
    }
    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return getIntent().getBundleExtra("name");
    }
    private void getQueryMessages() {
        //首次加载，可设置msg为null，
        //下拉刷新的时候，可用消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列，limit由开发者控制
        c.queryMessages(null, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null){
                    adapter = new ChatAdapter(list,ChatActivity.this);
                    Log.e("asfafs", "done: "+list );
                    list1=list;
                    textListView.setAdapter(adapter);
                }else {
                    Toast.makeText(ChatActivity.this, "e:" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
         if (v == sendBtn) {
            if (!TextUtils.isEmpty(chatEditView.getText())){
                String msg = chatEditView.getText().toString();
                sendTextMsg(msg);
            }else {
                Toast.makeText(this, "输入框为空", Toast.LENGTH_SHORT).show();
            }
        } else if (v == chatEditView) {
            moer.setVisibility(View.VISIBLE);
        } else if (v == dili) {
            startActivity(new Intent(ChatActivity.this,Map.class));
        }else if (v == yuyin) {
            if(flag==false) {
                yuyin.setImageResource(R.drawable.mai1);
                xianshi.setVisibility(View.VISIBLE);
                flag = true;
            }else{
                xianshi.setVisibility(View.GONE);
                flag=false;
            }
        }else if (v == paizhao) {
            getImgFromCamra();
        }else if (v == xiangpian) {
            getImgFromAlbum();
        }
    }
    /*
    * 设置从本地相册获取图片
   */
    private void getImgFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,ALBUM);
    }
    /*
     * 设置从相机获取图片
    */
    private void getImgFromCamra() {
        String state = Environment.getExternalStorageState();
        // 先检测是不是有内存卡。
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            mFolder = new File(Environment.getExternalStorageDirectory(), "bCache");
            // 判断手机中有没有这个文件夹，没有就新建。
            if (!mFolder.exists()) {
                mFolder.mkdirs();
            }
            // 自定义图片名字，这里是以毫秒数作为图片名。
            mImgName = System.currentTimeMillis() + ".jpg";
            Uri uri = Uri.fromFile(new File(mFolder, mImgName));
            // 调用系统拍照功能。
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA);
        } else {
            Toast.makeText(this, "未检测到SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendTextMsg(String text) {
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
       //可随意设置额外信息
        msg.setMsgType("1");
        c.sendMessage(msg, listener);
    }
    private void sendTextMsg2(String text) {
        BmobIMImageMessage image =new BmobIMImageMessage(text);
        c.sendMessage(image, listener);
    }
    /**
     * 消息发送监听器
     */
    public MessageSendListener listener =new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Logger.i("onProgress："+value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            //edit_msg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            //edit_msg.setText("");
            scrollToBottom();
            if (e != null) {
                toast(e.getMessage());
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            // 调用系统方法获取到的是被压缩过的图片，通过自定义路径轻松获取原始图片。
            String str=mFolder.getAbsolutePath()
                    + File.separator + mImgName;
            sendTextMsg2(str);
            //mIvPhoto.setImageBitmap(bm);
        }
        if (resultCode == RESULT_OK && requestCode == ALBUM) {
            //获取图片的路径：
            Uri originalUri = data.getData();        //获得图片的uri
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = managedQuery(originalUri, proj, null, null, null);
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            String path = cursor.getString(column_index);
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            sendTextMsg2(path);
        }
    }

    private void scrollToBottom() {
        textListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                textListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        chatMain = (SildingFinishLayout) findViewById(R.id.chat_main);
        textLayout = (LinearLayout) findViewById(R.id.text_layout);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
        moreheight = preferences.getInt("keyboardHeight", 0);
        sendImg = (LinearLayout) findViewById(R.id.send_img);
        sendLocation = (LinearLayout) findViewById(R.id.send_location);
        camera = (LinearLayout) findViewById(R.id.camera);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("会话");
        toolBar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.finish();
            }
        });
        voiseOrKeybord = (ImageView) findViewById(R.id.voise_or_keybord);
        sendBtn = (Button) findViewById(R.id.send_btn);
        emoBtn = (ImageView) findViewById(R.id.emo_btn);
        chatEditView = (BackEditText) findViewById(R.id.chat_edit_view);
        moer = (LinearLayout) findViewById(R.id.moer);
        audioRecordBtn = (AudioRecordButton) findViewById(R.id.recordButton);
        audioRecordBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
               sendLocalAudioMessage(filePath);
            }
        });
        textListView = (ListView) findViewById(R.id.text_listView);
        textListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list1.get(i).getMsgType().equals(BmobIMMessageType.VOICE.getType())){
                    if(list1.get(i).getFromId().equals(BmobUser.getCurrentUser().getObjectId())) {
                        // TODO Auto-generated method stub
                        BmobIMMessage msgs = list1.get(i);
                        BmobIMAudioMessage msg = BmobIMAudioMessage.buildFromDB(false, msgs);
                        Log.e("BBW", "onItemClick: " + msg.getContent());
                        String path = msg.getContent().split("&")[0];
                        // 播放动画
                        if (viewanim != null) {//让第二个播放的时候第一个停止播放
                            viewanim.setBackgroundResource(R.drawable.adj);
                            viewanim = null;
                        }
                        Toast.makeText(ChatActivity.this, path, Toast.LENGTH_SHORT).show();
                        viewanim = view.findViewById(R.id.id_recorder_anim);
                        viewanim.setBackgroundResource(R.drawable.play);
                        AnimationDrawable drawable = (AnimationDrawable) viewanim
                                .getBackground();
                        drawable.start();
                        // 播放音频
                        com.amap.map3d.demo.chat_player.MediaManager.playSound(path,
                                new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        viewanim.setBackgroundResource(R.drawable.adj);

                                    }
                                });
                    }else{
                        BmobIMMessage msgs = list1.get(i);
                        BmobIMAudioMessage msg = BmobIMAudioMessage.buildFromDB(false, msgs);
                        Log.e("BBW", "onItemClick: " + msg.getContent());
                        String path = msg.getContent().split("&")[0];
                        // 播放动画
                        if (viewanim != null) {//让第二个播放的时候第一个停止播放
                            viewanim.setBackgroundResource(R.drawable.s3);
                            viewanim = null;
                        }
                        viewanim = view.findViewById(R.id.id_recorder_anim);
                        viewanim.setBackgroundResource(R.drawable.play1);
                        AnimationDrawable drawable = (AnimationDrawable) viewanim
                                .getBackground();
                        drawable.start();
                        // 播放音频
                        Toast.makeText(ChatActivity.this, path, Toast.LENGTH_SHORT).show();
                        com.amap.map3d.demo.chat_player.MediaManager.playSound(path,
                                new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        viewanim.setBackgroundResource(R.drawable.s3);

                                    }
                                });

                    }
                }
            }
        });
        if (moreheight != 0) {
            //获取当前控件的布局对象
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) moer.getLayoutParams();
            //设置当前控件布局的高度
            params.height = moreheight;
        }

        voiseOrKeybord.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        emoBtn.setOnClickListener(this);
        chatEditView.setOnClickListener(this);
        chatMain.setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
            @Override
            public void onSildingFinish() {
                ChatActivity.this.finish();
            }
        });
        chatMain.setTouchView(textListView);
        sendLocation.setOnClickListener(this);
        textLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textLayout.setFocusable(true);
                textLayout.setFocusableInTouchMode(true);
                textLayout.requestFocus();
                if (moer.getVisibility()==View.VISIBLE){
                    moer.setVisibility(View.GONE);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(chatEditView.getWindowToken(), 0);
                return false;
            }
        });

    }
    public void sendLocationMessage(String adress,double s,double x) {
        //TODO 发送消息：6.10、发送位置消息
        //测试数据，真实数据需要从地图SDK中获取
        BmobIMLocationMessage location = new BmobIMLocationMessage(adress, x, s);
        c.sendMessage(location, listener);
    }
    private void sendLocalAudioMessage(String path) {
        BmobIMAudioMessage audio = new BmobIMAudioMessage(path);
        //TODO 发送消息：6.4、发送本地音频文件消息
        c.sendMessage(audio, listener);
    }
    private void initView1() {
        yuyin= (ImageView) findViewById(R.id.yuyin);
        xianshi= (LinearLayout) findViewById(R.id.xianshi);
        yuyin.setOnClickListener(this);
        a= (AudioRecordButton) findViewById(R.id.recordButton);
        paizhao= (ImageView) findViewById(R.id.paizhao);
        paizhao.setOnClickListener(this);
        xiangpian= (ImageView) findViewById(R.id.xiangce);
        xiangpian.setOnClickListener(this);
        wenjian= (ImageView) findViewById(R.id.wenjian);
        wenjian.setOnClickListener(this);
        dili= (ImageView) findViewById(R.id.dili);
        dili.setOnClickListener(this);
        biaoqing= (ImageView) findViewById(R.id.biaoqing);
        biaoqing.setOnClickListener(this);
        app= (BmobIMApplication) getApplication();
        int[] a=new int[]{R.drawable.imge2,R.drawable.imge3,R.drawable.imge4,R.drawable.imge5};
    }
    private void showSoft() {
        chatEditView.setFocusable(true);
        chatEditView.setFocusableInTouchMode(true);
        chatEditView.requestFocus();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        imm.showSoftInput(chatEditView, InputMethodManager.SHOW_FORCED);
    }

    private void hideSoft() {
        imm.hideSoftInputFromWindow(chatEditView.getWindowToken(), 0); //强制隐藏键盘
    }


    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        moer.setVisibility(View.VISIBLE);
        return screenHeight - rect.bottom != 0;
    }


    public void addOnSoftKeyBoardVisibleListener(Activity activity, final IKeyBoardVisibleListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕整体的高度
                int hight = decorView.getHeight();
                /**
                 * 获取状态栏高度——方法1
                 * */
                int statusBarHeight1 = -1;
                //获取status_bar_height资源的ID
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
                }
                //获得键盘高度
                keyboardHeight = hight - displayHight - statusBarHeight1;
                if (keyboardHeight != moreheight && keyboardHeight > 0) {
                    SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                    preferences.edit().putInt("keyboardHeight", keyboardHeight).commit();
                    //获取当前控件的布局对象
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) moer.getLayoutParams();
                    //设置当前控件布局的高度
                    params.height = keyboardHeight;
                }
                boolean visible = (double) displayHight / hight < 0.8;
                if (visible != isVisiableForLast) {
                    listener.onSoftKeyBoardVisible(visible, keyboardHeight);
                }
                isVisiableForLast = visible;
            }
        });
    }

    @Override
    public void onSoftKeyBoardVisible(boolean visible, int windowBottom) {

    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
//        adapter.notifyDataSetChanged();
        //查询会话
        Log.e("444", "onMessageReceive: "+2222);
        getQueryMessages();
        scrollToBottom();
    }

    @Override
    protected void onResume() {
        BmobIM.getInstance().addMessageListHandler(this);
        super.onResume();
        if(app.getBuffer()!=null){
            sendLocationMessage(app.getBuffer(),app.getLongitude(),app.getLatitude());
            app.setBuffer(null);
        }
    }

    @Override
    protected void onPause() {
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }
}
