package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.Adpter.ChatMessageAdapter;
import com.amap.map3d.demo.chat_player.Adpter.GridViewAdapter;
import com.amap.map3d.demo.chat_player.MediaManager;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.application.BmobIMApplication;
import com.amap.map3d.demo.chat_player.bean.ChatMessage;
import com.amap.map3d.demo.chat_player.bean.Recorder;
import com.amap.map3d.demo.chat_player.bean.Weizhi;
import com.amap.map3d.demo.chat_player.util.HttpUtils;
import com.amap.map3d.demo.chat_player.view.AudioRecordButton;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.v3.exception.BmobException;

import static com.amap.map3d.demo.chat_player.R.id.info;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class Talking extends Activity implements ObseverListener,MessageListHandler,View.OnClickListener{
    private ListView mMsg;
    private static final int CAMERA = 1025;
    private static final int ALBUM = 1026;
    private AudioRecordButton a;
    private ChatMessageAdapter mAdapter;
    private List mDatas;
    private EditText mInputMsg;
    private ImageView yuyin,paizhao,xiangpian,wenjian,dili,biaoqing;
    private boolean flag;
    private LinearLayout xianshi;
    private Button mSengMsg;
    private View viewanim;
    private File mFolder;
    private String mImgName;
    private List imglist;
    BmobIMConversation c;
    private BmobIMApplication app;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //接收消息
            ChatMessage fromMsg = (ChatMessage) msg.obj;
            //添加消息
            mDatas.add(fromMsg);
            //刷新ListView
            mAdapter.notifyDataSetChanged();
            mMsg.setSelection(mDatas.size()-1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);
//在聊天页面的onCreate方法中，通过如下方法创建新的会话实例,这个obtain方法才是真正创建一个管理消息发送的会话
//        c=BmobIMConversation.obtain(BmobIMClient.getInstance(),(BmobIMConversation)getIntent().getBundleExtra("xiaoxi").getSerializable("c"));
        initView();
        initDatas();
        initListener();
    }
    private void initListener() {
        //发送消息
        if(app.getBuffer()!=null){
            Weizhi weizhi=new Weizhi(app.getBuffer(),app.getLatitude(),app.getLongitude());
            mDatas.add(weizhi);
            Toast.makeText(this, weizhi.getBuff(), Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
            mMsg.setSelection(mDatas.size() - 1);
            app.setBuffer(null);
        }
        a.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                // TODO Auto-generated method stub
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mMsg.setSelection(mDatas.size() - 1);
            }
        });

       mMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(mDatas.get(position) instanceof Recorder){
                // TODO Auto-generated method stub
                    Recorder re= (Recorder) mDatas.get(position);
                // 播放动画
                if (viewanim!=null) {//让第二个播放的时候第一个停止播放
                    viewanim.setBackgroundResource(R.drawable.adj);
                    viewanim=null;
                }
                viewanim = view.findViewById(R.id.id_recorder_anim);
                viewanim.setBackgroundResource(R.drawable.play);
                AnimationDrawable drawable = (AnimationDrawable) viewanim
                        .getBackground();
                drawable.start();

                // 播放音频
                MediaManager.playSound(re.getFilePathString(),
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                viewanim.setBackgroundResource(R.drawable.adj);

                            }
                        });
            }
        }
       });
        mSengMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取EditText内容
                final String toMsg = mInputMsg.getText().toString();
                //判空
                if (TextUtils.isEmpty(toMsg))
                {
                    Toast.makeText(Talking.this,"发送的消息不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsg.setSelection(mDatas.size()-1);
                //初始化EditText
                mInputMsg.setText("");
                new Thread(){
                    @Override
                    public void run() {
                        ChatMessage fromMsg = HttpUtils.sendMesage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMsg;
                        mHandler.sendMessage(m);
                    }
                }.start();
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(new ChatMessage("你好，我是图图！",ChatMessage.Type.INCOMING,new Date()));
        mAdapter = new ChatMessageAdapter(mDatas,this);
        mMsg.setAdapter(mAdapter);
    }

    private void initView() {
        mMsg = (ListView) findViewById(R.id.id_listview_msgs);
        mInputMsg = (EditText) findViewById(R.id.id_input_msg);
        mSengMsg = (Button) findViewById(R.id.id_send_msg);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yuyin:
                if(flag==false) {
                    yuyin.setImageResource(R.drawable.mai1);
                    xianshi.setVisibility(View.VISIBLE);
                    flag = true;
                }else{
                    xianshi.setVisibility(View.GONE);
                    flag=false;
                }
                break;
            case R.id.biaoqing:

                break;
            case R.id.dili:
                startActivity(new Intent(Talking.this,Map.class));
                break;
            case R.id.xiangce:
                getImgFromAlbum();
                break;
            case R.id.paizhao:
                getImgFromCamra();
                break;
            case R.id.wenjian:

                break;

        }
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

    /*
     * 设置从本地相册获取图片
    */
    private void getImgFromAlbum() {
        // 调用本地图库。
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm;
        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            // 调用系统方法获取到的是被压缩过的图片，通过自定义路径轻松获取原始图片。
            bm = BitmapFactory.decodeFile(mFolder.getAbsolutePath()
                    + File.separator + mImgName);
            //mIvPhoto.setImageBitmap(bm);
            mDatas.add(bm);
            mAdapter.notifyDataSetChanged();
            mMsg.setSelection(mDatas.size()-1);
        }

        if (resultCode == RESULT_OK && requestCode == ALBUM) {
            try {
                if (data != null) {
                    // 获取本地相册图片。
                    Uri uri = data.getData();
                    ContentResolver cr = getContentResolver();
                    bm = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    //mIvPhoto.setImageBitmap(bm);
                    mDatas.add(bm);
                    mAdapter.notifyDataSetChanged();
                    mMsg.setSelection(mDatas.size()-1);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 发送文本消息
     */
    private void sendMessage(){
        String text=mSengMsg.getText().toString();
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
        c.sendMessage(msg, listener);
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
            //mAdapter.addMessage(msg);
            mSengMsg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            mAdapter.notifyDataSetChanged();
            mSengMsg.setText("");
            scrollToBottom();
            if (e != null) {
                //toast(e.getMessage());
            }
        }
    };
    private void scrollToBottom() {
        //layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MediaManager.release();
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {

    }
}
