package com.component.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by weibo on 15-12-10.
 */
public class PhotoService {

    private Context context;
    private TakePhotoPopupWindow popupWindow;

    public PhotoService(Context context) {
        this.context = context;
    }

    /*
     *　采用默认的view
     */
    public void takePhoto(Context context, View attachView, final Fragment fragment,
                          final boolean aspect, final int aspectX, final int aspectY) {

        View contentView = LayoutInflater.from(context).inflate(R.layout.photo_select, null);
        Button takeBtn = (Button) contentView.findViewById(R.id.btn_take_photo);
        Button pickBtn = (Button) contentView.findViewById(R.id.btn_pick_photo);
        Button cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        popupWindow = new TakePhotoPopupWindow(contentView,
                takeBtn, pickBtn,cancelBtn,
                new TakePhotoPopupWindow.TakePhotoInterface() {
                    @Override
                    public void takePhoto() {
                        startPhoto(false, fragment, aspect, aspectX, aspectY);
                    }

                    @Override
                    public void pickPhoto() {
                        startPhoto(true, fragment, aspect, aspectX, aspectY);
                    }
                });

        popupWindow.showAtLocation(attachView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    // view自定义
    public void takePhoto(View attachView, final Fragment fragment,
                          View contentView, Button takeBtn, Button pickBtn, Button cancelBtn,
                          final boolean aspect,
                          final int aspectX, final int aspectY) {
        popupWindow = new TakePhotoPopupWindow(contentView, takeBtn, pickBtn, cancelBtn,
                new TakePhotoPopupWindow.TakePhotoInterface() {
                    @Override
                    public void takePhoto() {
                        startPhoto(false, fragment, aspect, aspectX, aspectY);
                    }

                    @Override
                    public void pickPhoto() {
                        startPhoto(true, fragment, aspect, aspectX, aspectY);
                    }
                });

        popupWindow.showAtLocation(attachView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void takePhoto(Context context, View attachView, final Fragment fragment) {
        takePhoto(context, attachView, fragment, true, 1, 1);
    }

    public void takePhoto(View attachView, final Fragment fragment,
                          View contentView, Button takeBtn, Button pickBtn, Button cancelBtn) {
        takePhoto(attachView, fragment, contentView, takeBtn, pickBtn, cancelBtn, true, 1, 1);
    }


    private void startPhoto(boolean selecteSystemPic, Fragment fragment, boolean aspectCrop, int aspectX, int aspectY) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra("aspect", aspectCrop);
        Bundle bundle = new Bundle();
        bundle.putInt("aspectX", aspectX);
        bundle.putInt("aspectY", aspectY);
        intent.putExtra(PhotoUtil.MEASURABLE_BUNDLE_NAME, bundle);
        if (selecteSystemPic) {
            intent.putExtra(PhotoUtil.PHOTO_ACTION, true);
            if (fragment != null) {
                fragment.startActivityForResult(intent, PhotoUtil.REQUEST_PICK_PHOTO);
            } else {
                ((Activity)context).startActivityForResult(intent, PhotoUtil.REQUEST_PICK_PHOTO);
            }
        } else {
            intent.putExtra(PhotoUtil.PHOTO_ACTION, false);
            if (fragment != null) {
                fragment.startActivityForResult(intent, PhotoUtil.REQUEST_TAKE_PHOTO);
            } else {
                ((Activity)context).startActivityForResult(intent, PhotoUtil.REQUEST_TAKE_PHOTO);
            }

        }
    }

}
