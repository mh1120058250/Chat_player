package com.amap.map3d.demo.chat_player.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.R;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class New_one extends AppCompatActivity {
    private String url,title,img,content;
    //自定义布局
    private boolean flag=false;
    private Button share,share1,btn;
    LinearLayout mLinearLayout;
    LinearLayout.LayoutParams mLayoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.news);
        //初始化布局
        url = getIntent().getStringExtra("news");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        img = getIntent().getStringExtra("img");
        initLayout();
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        //Log.e("qqq", url);
        if(url==null|| url.isEmpty()){
            TextView tvContent = new TextView(getApplicationContext());
            tvContent.setTextSize(34);
            tvContent.setGravity(Gravity.CENTER);
            tvContent.setText("404");
            tvContent.setTextColor(Color.BLACK);
            //将TextView添加到LinerLayout中
            LinearLayout.LayoutParams  mLayoutParams1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLinearLayout.addView(tvContent, mLayoutParams1);
            url=null;
        }else if(url!=null) {
            Log.e("ttt", url);
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
            getData(url);
        }
    }

    private void getData(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("www", url);
                    Document document = Jsoup.connect(url).get();
                        //获取标题
                        final String title = document.getElementsByTag("h1").text();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvTitle = new TextView(getApplicationContext());
                                tvTitle.setTextSize(25);
                                tvTitle.setGravity(Gravity.CENTER);
                                tvTitle.setText(title);
                                tvTitle.setTextColor(Color.BLACK);
                                //将TextView添加到LinerLayout中
                                mLinearLayout.addView(tvTitle, mLayoutParams);
                            }
                        });
                        //获取时间
                        String time = document.getElementsByClass("post_time_source").text();
                        if (time.length() == 0) {
                            time = document.getElementsByClass("pub_time").text();
                            if (time.length() == 0) {
                                time = document.getElementsByClass("atc_bd").text();
                                if (time.length() == 0) {
                                    time = document.getElementsByClass("article-timestamp").text();

                                }
                            }
                        }
                        final String finalTime = time;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvTime = new TextView(getApplicationContext());
                                tvTime.setTextSize(18);
                                tvTime.setGravity(Gravity.CENTER);
                                tvTime.setText(finalTime);
                                tvTime.setTextColor(Color.BLACK);
                                //将TextView添加到LinerLayout中
                                mLinearLayout.addView(tvTime, mLayoutParams);
                            }
                        });
                        Log.e("news", time);
                        //获取新闻主题
                        Elements mainElements = document.getElementsByClass("post_text");
                        if (mainElements.toString().length() == 0) {
                            mainElements = document.getElementsByClass("cc_main");
                        }
                        //Log.e("news", mainElements.toString());

                        for (int i = 0; i < mainElements.size(); i++) {
                            //获取p元素集合
                            Elements newsElements = mainElements.get(i).getElementsByTag("p");
                            for (int j = 0; j < newsElements.size(); j++) {
                                //新闻内容
                                final String content = newsElements.get(j).text();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView tvContent = new TextView(getApplicationContext());
                                        tvContent.setTextSize(18);
                                        tvContent.setGravity(Gravity.CENTER);
                                        tvContent.setText(content);
                                        tvContent.setTextColor(Color.BLACK);
                                        //将TextView添加到LinerLayout中
                                        mLinearLayout.addView(tvContent, mLayoutParams);
                                    }
                                });
                                Log.e("news", content);
                                //图片名
                                final String imgName = newsElements.get(j).getElementsByTag("img").attr("alt");
                                Log.e("news", imgName);
                                //图片地址
                                final String img = newsElements.get(j).getElementsByTag("img").attr("src");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView imgView = new ImageView(getApplicationContext());
                                        //将TextView添加到LinerLayout中
                                        imgView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(New_one.this, "你点击了图片", Toast.LENGTH_SHORT).show();
                                                //Intent intent=new Intent(New_one.this,ImageActivity.class);
                                                 //intent.putExtra("img",img);
                                                //startActivity(intent);
                                            }
                                        });
                                        mLinearLayout.addView(imgView, mLayoutParams);
                                        Glide.with(getApplicationContext()).load(img).into(imgView);
                                    }
                                });
                                Log.e("news", img);
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initLayout() {
        btn= (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false) {
                    share1.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    flag=true;
                }else{
                    share1.setVisibility(View.GONE);
                    share.setVisibility(View.GONE);
                    flag=false;
                }
            }
        });
        mLinearLayout = (LinearLayout) findViewById(R.id.id_news_info_linerLayout);
        //建立布局样式
        mLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        share= (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share1.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
                flag=false;
            }
        });
        share1= (Button) findViewById(R.id.share1);

    }


}