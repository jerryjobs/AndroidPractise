package com.ikaowo.join.common;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

/**
 * Created by leiweibo on 1/9/16.
 */
public class AnimatorWrapper {

  public void startTranslationAnimation(final View view, int postDelay) {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        final ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());
        translation.setDuration(500);
        translation.start();
        translation.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {

          }

          @Override
          public void onAnimationEnd(Animator animation) {
            view.setVisibility(View.GONE);
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
