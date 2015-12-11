package com.ikaowo.join.modules.user.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ikaowo.join.R;
import com.ikaowo.join.util.ResourceUtil;

/**
 * Created by weibo on 15-12-11.
 */
public class DeletableEditTextView extends AppCompatEditText implements View.OnFocusChangeListener {
    private Drawable deleteIconDrawable;


    public DeletableEditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DeletableEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeletableEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        deleteIconDrawable = ResourceUtil.getDrawable(context, R.drawable.edittext_delete_icon);

        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                showOrHideDeleteDrawable();
            }
        });
    }

    private void showOrHideDeleteDrawable() {
        if (length() == 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, deleteIconDrawable, null);
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (deleteIconDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&
                    (x < (getWidth() - getPaddingRight()));
            //获取删除图标的边界，返回一个Rect对象
            Rect rect = deleteIconDrawable.getBounds();
            //获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));

            if (isInnerWidth && isInnerHeight) {
                setText("");
            }

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showOrHideDeleteDrawable();
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }
}
