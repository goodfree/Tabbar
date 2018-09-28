package com.kongzue.tabbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.kongzue.tabbar.interfaces.OnTabChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/28 00:28
 */
public class TabBarView extends LinearLayout {
    
    private OnTabChangeListener onTabChangeListener;
    private int focusIndex = 0;
    
    private int tabPaddingVertical;
    private int iconPadding;
    private int textSize;
    private int focusColor;
    private int normalColor;
    
    private Context context;
    
    protected List<View> tabViews;
    
    public TabBarView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    
    public TabBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        laodAttrs(context, attrs);
    }
    
    public TabBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
        laodAttrs(context, attrs);
    }
    
    private void laodAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabBar);
        iconPadding = typedArray.getDimensionPixelOffset(R.styleable.TabBar_iconPadding, iconPadding);
        textSize = typedArray.getDimensionPixelOffset(R.styleable.TabBar_textSize, textSize);
        tabPaddingVertical = typedArray.getDimensionPixelOffset(R.styleable.TabBar_tabPaddingVertical, tabPaddingVertical);
        focusColor = typedArray.getColor(R.styleable.TabBar_focusColor, focusColor);
        normalColor = typedArray.getColor(R.styleable.TabBar_normalColor, normalColor);
        typedArray.recycle();
    }
    
    private void init() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        //默认值
        iconPadding = dp2px(5);
        tabPaddingVertical = dp2px(5);
        textSize = dp2px(12);
        focusColor = Color.rgb(62, 120, 238);
        normalColor = Color.rgb(96, 96, 96);
    }
    
    public TabBarView setTab(List<Tab> tabDatas) {
        removeAllViews();
        tabViews = new ArrayList<>();
        for (int i = 0; i < tabDatas.size(); i++) {
            Tab tab = tabDatas.get(i);
            View item = LayoutInflater.from(context).inflate(R.layout.item_tab, null, false);
            item.setPadding(0, tabPaddingVertical, 0, tabPaddingVertical);
            addView(item);
            
            ImageView imgIcon = item.findViewById(R.id.img_icon);
            TextView txtName = item.findViewById(R.id.txt_name);
            
            imgIcon.setImageBitmap(tab.getIcon());
            txtName.setText(tab.getName());
            
            imgIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            
            LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.CENTER;
            item.setLayoutParams(params);
            
            tabViews.add(item);
        }
        refreshFocusTabStatus();
        setEvents();
        return this;
    }
    
    private void refreshFocusTabStatus() {
        for (int i = 0; i < tabViews.size(); i++) {
            View item = tabViews.get(i);
            ImageView imgIcon = item.findViewById(R.id.img_icon);
            TextView txtName = item.findViewById(R.id.txt_name);
            
            if (i == focusIndex) {
                setImageViewColor(imgIcon, focusColor);
                txtName.setTextColor(focusColor);
            } else {
                setImageViewColor(imgIcon, normalColor);
                txtName.setTextColor(normalColor);
            }
        }
    }
    
    private void setEvents() {
        for (int i = 0; i < tabViews.size(); i++) {
            View item = tabViews.get(i);
            final int selectIndex=i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    focusIndex = selectIndex;
                    refreshFocusTabStatus();
                    if (onTabChangeListener!=null)onTabChangeListener.onTabChanged(focusIndex);
                }
            });
        }
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
    
    public int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
    
    public float px2dp(int pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }
    
    public void setImageViewColor(ImageView view, int color) {
        Drawable modeDrawable = view.getDrawable().mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }
    
    public OnTabChangeListener getOnTabChangeListener() {
        return onTabChangeListener;
    }
    
    public TabBarView setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
        return this;
    }
    
    public int getFocusIndex() {
        return focusIndex;
    }
    
    public TabBarView setNormalFocusIndex(int focusIndex) {
        this.focusIndex = focusIndex;
        refreshFocusTabStatus();
        return this;
    }
    
    public int getTabPaddingVertical() {
        return tabPaddingVertical;
    }
    
    public TabBarView setTabPaddingVertical(int tabPaddingVertical) {
        this.tabPaddingVertical = tabPaddingVertical;
        return this;
    }
    
    public int getIconPadding() {
        return iconPadding;
    }
    
    public TabBarView setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        return this;
    }
    
    public int getTextSize() {
        return textSize;
    }
    
    public TabBarView setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }
    
    public int getFocusColor() {
        return focusColor;
    }
    
    public TabBarView setFocusColor(int focusColor) {
        this.focusColor = focusColor;
        return this;
    }
    
    public int getNormalColor() {
        return normalColor;
    }
    
    public TabBarView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }
}