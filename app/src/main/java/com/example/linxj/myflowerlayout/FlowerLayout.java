package com.example.linxj.myflowerlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linxj on 2015/11/11.
 */
public class FlowerLayout extends ViewGroup {
    public FlowerLayout(Context context) {
        super(context,null);
    }

    public FlowerLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public FlowerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

/*    public FlowerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
       int count =getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        List<List<View>> AllViews = new ArrayList<List<View>>();
        List<Integer> Lineheight  = new ArrayList<Integer>();
        int width = getWidth(); // 获取当前ViewGroup的宽度
        List<View> lineView = new ArrayList<View>();
        for(int i = 0;i < count;i++){
            View view = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)view.getLayoutParams();
            //lineView = new ArrayList<View>();
            int childWidth = view.getMeasuredWidth();
            int childHeigth = view.getMeasuredHeight();
            //换行
            if(lineWidth + childWidth + lp.leftMargin+lp.rightMargin>width+getPaddingLeft()+getPaddingRight()){
                 AllViews.add(lineView);
                Lineheight.add(lineHeight);
                lineWidth = 0;
                lineHeight = childHeigth+lp.bottomMargin+lp.topMargin;
                lineView = new ArrayList<View>();
            }
            lineWidth += childWidth+lp.leftMargin+lp.rightMargin;
            lineHeight = Math.max(lineHeight,childHeigth+lp.topMargin+lp.bottomMargin);
            lineView.add(view);
            //onLayout();
        }
        //处理最后一行
        Lineheight.add(lineHeight);
        AllViews.add(lineView);

         int size = AllViews.size();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for(int i = 0;i < size;i++){
             lineView  = AllViews.get(i);
            lineHeight = Lineheight.get(i);
            for(int j =0;j < lineView.size();j++) {
                View view = lineView.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                if (view.getVisibility() == View.GONE) {
                    continue;
                }
                int lc = left + lp.leftMargin;
                int rc = lc + view.getMeasuredWidth();
                int tc = lp.topMargin + top;
                int bc = tc + view.getMeasuredHeight();
                view.layout(lc, tc, rc, bc);    //布局子控件

                left += view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
             top += lineHeight;
            left = getPaddingLeft();
        }


    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int SizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int SizeHight = MeasureSpec.getSize(heightMeasureSpec);
        int ModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int ModeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();
        for(int i = 0;i < count;i ++){
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec); //测量子view
            MarginLayoutParams lp = (MarginLayoutParams)view.getLayoutParams();
            int childWidth = view.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeigth = view.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
            //换行
            if(lineWidth + childWidth > SizeWidth + getPaddingLeft() + getPaddingRight()){
                width = Math.max(lineWidth,width);   //计算宽的最大值
                lineWidth=childWidth;       //重置行宽
                height+=childHeigth;     //换行
                lineHeight = childHeigth;
            }else{
                lineWidth+=childWidth;    //计算宽

                lineHeight = Math.max(lineHeight,height);
            }
              if(i == count-1){
                   width = Math.max(lineWidth,width);
                  height+=lineHeight;             //除了换行，一直没有计算heigth，所以最后一个要计算
              }
        }

        setMeasuredDimension(ModeWidth==MeasureSpec.EXACTLY?SizeWidth:width+getPaddingRight()+getPaddingLeft()
                                ,ModeHeight==MeasureSpec.EXACTLY?SizeHight:height+getPaddingBottom()+getPaddingTop());
    }
}
