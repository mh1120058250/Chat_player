package com.amap.map3d.demo.chat_player.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.amap.map3d.demo.chat_player.R;


/**
 * 自定义View继承SwipeRefreshLayout，添加上拉加载更多的布局属性
 *  2017/6/22.
 */

public class SwipeRefreshView extends SwipeRefreshLayout{
    private final int mScaledTouchSlop;
    private final View mFooterView;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;
    //正在加载状态
    private boolean isLoading;
    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer,null);

        //表示控件移动的最小距离，手移动的距离大于这个距离，才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.e("===", "---->"+mScaledTouchSlop );
    }

    //确定子控件的位置
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取ListView，设置ListView的布局位置
        if (mListView == null)
        {
            //判断容器中有多少个子控件
            if (getChildCount()>0)
            {
                //判断第一个子控件是不是ListView
                if (getChildAt(0) instanceof ListView)
                {
                    //创建ListView对象
                    mListView = (ListView) getChildAt(0);
                    //设置ListView的滑动监听
                    setListViewOnScroll();
                }
            }
        }
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY,mUpY;

    //拦截用户触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //判断用户的触摸动作
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动过程中判断是否能下拉加载
                if (canLoadMore())
                {
                    //加载数据
                    loadData();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                mUpY = getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 处理加载更多数据的逻辑
     */
    public void loadData() {

        Log.e("===", "加载数据。。。" );
        if (mOnLoadListener !=null)
        {
            //设置加载状态，让布局显示出来
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     * @param loading
     */
    public void setLoading(boolean loading) {
        //修改当前状态
        isLoading = loading;
        if (isLoading)
        {
            //显示布局
            mListView.addFooterView(mFooterView);
        }else {
            //隐藏布局
            mListView.removeFooterView(mFooterView);
            //重置滑动坐标
            mDownY = 0;
            mUpY = 0;
        }
    }


    /**
     * 判断用户是否满加载更多的条件
     * @return
     */
    private boolean canLoadMore() {
        //1、是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition1)
        {
            Log.d("===", "是上拉状态");
        }
        //2、当前页面可见的item是最后一个条目
        boolean condition2 = false;
        if (mListView !=null && mListView.getAdapter() !=null)
        {
            //获取ListView最后一个可见条目
            condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        if (condition2)
        {
            Log.e("===", "是最后一个条目" );
        }
        //3、正在加载状态
        boolean  condition3 = !isLoading;
        if (condition3)
        {
            Log.e("===", "不是正在加载状态" );
        }
        return condition1 && condition2  && condition3;
    }

    /**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //移动过程中判断是否能下拉加载更多
                if (canLoadMore())
                {
                    //加载数据
                    loadData();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 上拉加载的接口回调
     */
    public interface OnLoadListener{
        void onLoad();
    }
    public void setOnLoadListener(OnLoadListener listener)
    {
        this.mOnLoadListener = listener;
    }
}
