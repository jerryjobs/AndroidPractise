package com.ikaowo.join.modules.user.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ikaowo.join.R;
import com.ikaowo.join.util.ResourceUtil;

/**
 * Created by weibo on 15-12-11.
 */
public class DeletableEditTextView extends AppCompatEditText implements View.OnFocusChangeListener {
    private Drawable deleteIconDrawable;
    private TextChangeListener textChangeListener;
    private boolean showDeleteIcon;

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

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DeletableEditTextView);
        showDeleteIcon = typedArray.getBoolean(R.styleable.DeletableEditTextView_show_delete_icon, true);

        deleteIconDrawable = ResourceUtil.updateDrawableColor(
                context, R.drawable.abc_ic_clear_mtrl_alpha, android.R.color.darker_gray);

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
                if (textChangeListener != null) {
                    textChangeListener.onChanged(s);
                }
            }
        });
    }

    private void showOrHideDeleteDrawable() {
        if (!showDeleteIcon) {
            return;
        }

        if (length() == 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, deleteIconDrawable, null);
        }
    }

    public void setTextChangeListener(TextChangeListener listener) {
        this.textChangeListener = listener;
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

    public interface TextChangeListener {
        void onChanged(Editable s);
    }
}
