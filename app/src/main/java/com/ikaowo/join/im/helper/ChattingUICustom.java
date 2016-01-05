package com.ikaowo.join.im.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.kit.common.IMUtility;
import com.ikaowo.join.R;

/**
 * 聊天界面的自定义风格1：文字和图片小猪气泡风格
 * Created by mayongge on 15-9-23.
 */
public class ChattingUICustom extends IMChattingPageUI {

  public ChattingUICustom(Pointcut pointcut) {
    super(pointcut);
  }

  /**
   * isv需要返回自定义的view. openIMSDK会回调这个方法，获取用户设置的view. Fragment 聊天界面的fragment
   */
  @Override
  public View getCustomTitleView(final Fragment fragment,
                                 final Context context, LayoutInflater inflater,
                                 final YWConversation conversation) {
    // 单聊和群聊都会使用这个方法，所以这里需要做一下区分
    // 本demo示例是处理单聊，如果群聊界面也支持自定义，请去掉此判断

    //TODO 重要：必须以该形式初始化view---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局**中的高度和宽度无效，均变为wrap_content
    View view = inflater.inflate(R.layout.activity_im_chat_custom_chatting_title, new RelativeLayout(context), false);
    view.setBackgroundColor(Color.parseColor("#1ba7e1"));
    TextView textView = (TextView) view.findViewById(R.id.title);
    String title = null;
    if (conversation.getConversationType() == YWConversationType.P2P) {
      YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation
              .getConversationBody();
      if (!TextUtils.isEmpty(conversationBody.getContact().getShowName())) {
        title = conversationBody.getContact().getShowName();
      } else {
        IYWContact contact = IMUtility.getContactProfileInfo(conversationBody.getContact().getUserId(), conversationBody.getContact().getAppKey());
        //生成showName，According to id。

        if (contact != null && !TextUtils.isEmpty(contact.getShowName())) {
          title = contact.getShowName();
        }
      }
      //如果标题为空，那么直接使用Id
      if (TextUtils.isEmpty(title)) {
        title = conversationBody.getContact().getUserId();
      }
    } else {
      if (conversation.getConversationBody() instanceof YWTribeConversationBody) {
        title = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
        if (TextUtils.isEmpty(title)) {
          title = "聊天";
        }
      } else {
        if (conversation.getConversationType() == YWConversationType.SHOP) { //为OpenIM的官方客服特殊定义了下、
          title = AccountUtils.getShortUserID(conversation.getConversationId());
        }
      }
    }
    textView.setText(title);
    textView.setTextColor(Color.parseColor("#FFFFFF"));
    textView.setTextSize(15);
    TextView backView = (TextView) view.findViewById(R.id.back);
    backView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        fragment.getActivity().finish();
      }
    });

    return view;
  }
}
