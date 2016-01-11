package com.ikaowo.join.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.util.JToast;
import com.ikaowo.join.R;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.VerifyCodeRequest;
import com.ikaowo.join.network.VerifyCodeInterface;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weibo on 15-12-21.
 */
public class VerifyCodeHelper {

  private Context context;
  private TextView getVerifyBtn;
  private EditText phoneEt;
  private VerifyCodeInterface verifyCodeInterface;
  private CountDownTimer countDownTimer;

  public VerifyCodeHelper(final Context context, final EditText phoneEt,
      final TextView getVerifyBtn) {

    this.context = context;
    this.getVerifyBtn = getVerifyBtn;
    this.verifyCodeInterface =
        JApplication.getNetworkManager().getServiceByClass(VerifyCodeInterface.class);
    this.phoneEt = phoneEt;
    this.countDownTimer = new CountDownTimer(60 * 1000, 1 * 1000) {
      @Override public void onTick(long millisUntilFinished) {
        String text =
            context.getString(R.string.verify_code_time_remaining, millisUntilFinished / 1000);
        getVerifyBtn.setText(text);
      }

      @Override public void onFinish() {
        getVerifyBtn.setEnabled(true);
        getVerifyBtn.setText(context.getString(R.string.get_verify_code));
      }
    };
  }

  public void initVerifyBtn() {
    this.phoneEt.setInputType(InputType.TYPE_CLASS_NUMBER);
    this.getVerifyBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        VerifyCodeRequest request = new VerifyCodeRequest();
        request.phone = phoneEt.getText().toString().trim();
        Call<BaseResponse> call = verifyCodeInterface.sendVerifyCodeRequest(request);
        getVerifyBtn.setEnabled(false);
        call.enqueue(new NetworkCallback<BaseResponse>(context) {
          @Override public void onSuccess(BaseResponse baseResponse) {
            if (baseResponse.status != 200) {
              resetBtn(context, getVerifyBtn, R.string.get_verify_code);
              JToast.toastLong(baseResponse.msg);
            } else {
              countDownTimer.start();
              JToast.toastLong("验证码已发送至手机，请查收");
            }
          }

          public void onFailed(Response response, Retrofit retrofit) {
            super.onFailed(response, retrofit);
            resetBtn(context, getVerifyBtn, R.string.get_verify_code);
          }

          public void onFailure(Throwable t) {
            resetBtn(context, getVerifyBtn, R.string.get_verify_code);
          }
        });
      }
    });
  }

  private void resetBtn(Context context, TextView btn, int strRes) {
    if (countDownTimer != null) {
      countDownTimer.cancel();
    }
    btn.setEnabled(true);
    btn.setText(context.getString(strRes));
  }
}
