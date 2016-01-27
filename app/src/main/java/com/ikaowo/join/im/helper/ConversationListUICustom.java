package com.ikaowo.join.im.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.common.framework.core.JApplication;
import com.ikaowo.join.R;

/**
 * Created by weibo on 16-1-27.
 */
public class ConversationListUICustom extends IMConversationListUI {
    public ConversationListUICustom(Pointcut pointcut) {
        super(pointcut);
    }

    @Override
    public boolean needHideTitleView(Fragment fragment) {
        return true;
    }

    /**
     * 该方法可以构造一个会话列表为空时的展示View
     *
     * @return empty view
     */
    @Override
    public View getCustomEmptyViewInConversationUI(Context context) {
        RelativeLayout view = new RelativeLayout(context);

        /** 以下为示例代码，开发者可以按需返回任何view*/
        TextView textView = new TextView(context);
        textView.setText(R.string.empty_hint_msg);
        textView.setTextColor(ContextCompat.getColor(context, R.color.c9));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.topMargin = JApplication.getJContext().dip2px(120);
        view.addView(textView, rlp);
        return view;
    }
}
