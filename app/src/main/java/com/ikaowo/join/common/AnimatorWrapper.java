package com.ikaowo.join.common;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;

import com.ikaowo.join.eventbus.AnimationUpdateCallback;

import de.greenrobot.event.EventBus;

/**
 * Created by leiweibo on 1/9/16.
 */
public class AnimatorWrapper {

    public void startTranslationAnimation(final View view, int postDelay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator translation = ObjectAnimator.ofFloat(view, "y", 0, -view.getHeight());
                translation.setDuration(500);
                translation.start();

                translation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(final ValueAnimator animation) {
                        EventBus.getDefault().post(new AnimationUpdateCallback() {
                            @Override
                            public float getUpdatedValue() {
                                return (float) animation.getAnimatedValue();
                            }
                        });
                    }
                });
                translation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }, postDelay);
    }
}
