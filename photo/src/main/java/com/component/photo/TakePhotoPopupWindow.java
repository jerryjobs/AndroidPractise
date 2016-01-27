package com.component.photo;

import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Created by weibo on 15-12-10.
 */
public class TakePhotoPopupWindow extends PopupWindow {

    public TakePhotoPopupWindow(final View contentView, Button takeBtn, Button pickBtn,
                                Button cancelBtn, final TakePhotoInterface takePhotoInterface) {

        super(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                true);
        //取消按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePhotoInterface != null) {
                    dismiss();
                    takePhotoInterface.takePhoto();
                }
            }
        });
        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePhotoInterface != null) {
                    dismiss();
                    takePhotoInterface.pickPhoto();
                }
            }
        });
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.popup_anim);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        contentView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });
    }

    public void setAnimation(int animation) {
        setAnimationStyle(animation);
    }

    public interface TakePhotoInterface {
        void takePhoto();

        void pickPhoto();
    }
}