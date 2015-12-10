package com.common.framework.umeng;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibo on 15-12-1.
 */
public class UmengShareService {

    private final String shareString = "com.umeng.share";
    private final String qq_AppId = "1104536732";
    private final  String qq_AppKey = "9e0chYqF1F3mGkkG";
    private final  String weixin_AppId = "wx38a5a30cf440be9a";
    private final  String weixin_AppSecret = "35f902992899642c5f389db0c8ebcad2";
    private final  UMSocialService mController = UMServiceFactory.getUMSocialService(shareString);

    /*
     * 初始化UMSocialService
     */
    private void initUMSocialService(Activity context) {

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, weixin_AppId, weixin_AppSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, weixin_AppId, weixin_AppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //QQ
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, qq_AppId, qq_AppKey);
        qqSsoHandler.addToSocialSDK();
        //QQ空间
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, qq_AppId, qq_AppKey);
        qZoneSsoHandler.addToSocialSDK();
        //微博

        //复制连接

    }

    public  SharePopupWindow openShareWindow(Context context, View target, String title,
                                                   String content, String targetUrl,
                                                   String picUrl) {
        initUMSocialService((Activity) context);
        SharePopupWindow shareWindow = new SharePopupWindow(context, title, content, targetUrl, picUrl);
        shareWindow.showAtLocation(target, Gravity.BOTTOM, 0, 0);

        return shareWindow;
    }

    /**
     * 分享到
     *
     * @param sBean
     */
    public  void shareTo(final Activity context, ShareBean sBean, String title, String content, String targetUrl, String picUrl) {
        //防止内容为空时,分享只有一个头像的问题。
        if (TextUtils.isEmpty(content))
            content = " ";

        UMImage umImage;

        if (TextUtils.isEmpty(picUrl)) {
            //应用logo update 3.1.0
            umImage = new UMImage(context, context.getString(R.string.app_icon));
        } else {
            umImage = new UMImage(context, picUrl);
        }
//    	Toast.makeText(context, "类型："+sBean.media, Toast.LENGTH_SHORT).show();
        switch (sBean.media) {
            case WEIXIN://微信

                WeiXinShareContent weixinContent = new WeiXinShareContent();
                //设置分享文字
                weixinContent.setShareContent(content);
                //设置title
                weixinContent.setTitle(title);
                //设置分享内容跳转URL
                weixinContent.setTargetUrl(targetUrl);
                //设置分享图片
                weixinContent.setShareImage(umImage);
                mController.setShareMedia(weixinContent);

                break;
            case WEIXIN_CIRCLE://微信朋友圈

                //设置微信朋友圈分享内容
                CircleShareContent circleMedia = new CircleShareContent();
                circleMedia.setShareContent(content);
                //设置朋友圈title
                circleMedia.setTitle(title);
                circleMedia.setShareImage(umImage);
                circleMedia.setTargetUrl(targetUrl);
                mController.setShareMedia(circleMedia);

                break;
            case QQ://qq

                QQShareContent qqShareContent = new QQShareContent();
                //设置分享文字
                qqShareContent.setShareContent(content);
                //设置分享title
                qqShareContent.setTitle(title);
                //设置分享图片
                qqShareContent.setShareImage(umImage);
                //设置点击分享内容的跳转链接
                qqShareContent.setTargetUrl(targetUrl);
                mController.setShareMedia(qqShareContent);

                break;
            case QZONE://qq空间

                QZoneShareContent qzone = new QZoneShareContent();
                //设置分享文字
                qzone.setShareContent(content);
                //设置点击消息的跳转URL
                qzone.setTargetUrl(targetUrl);
                //设置分享内容的标题
                qzone.setTitle(title);
                //设置分享图片
                qzone.setShareImage(umImage);
                mController.setShareMedia(qzone);

                break;
            case SINA://新浪

                SinaShareContent sinaShareContent = new SinaShareContent();

                if (!TextUtils.isEmpty(content)) {
                    if (content.length() > 30) {
                        content = content.substring(0, 30) + "...";
                    }
                }
                String contentT = content + targetUrl;
                sinaShareContent.setShareContent(contentT);
                sinaShareContent.setShareImage(umImage);
                mController.setShareMedia(sinaShareContent);


                break;
            case DOUBAN://用豆瓣来代替复制链接

                //保存到剪贴板
                ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(targetUrl);
                Toast.makeText(context, "复制链接成功", Toast.LENGTH_SHORT).show();

                break;
        }
        if (sBean.media != SHARE_MEDIA.DOUBAN && sBean.media != SHARE_MEDIA.YNOTE)
            mController.postShare(context, sBean.media,
                    new SocializeListeners.SnsPostListener() {
                        @Override
                        public void onStart() {
                            //  Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete(SHARE_MEDIA platform, int eCode,
                                               SocializeEntity entity) {
                            if (eCode == 200) {
                                Toast.makeText(context, "分享成功.", Toast.LENGTH_SHORT).show();
                            } else {
                                String eMsg = "";
                                if (eCode == -101) {
                                    eMsg = "没有授权";
                                }
                                Toast.makeText(context, "分享失败[" + eCode + "] " +
                                        eMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

     class SharePopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {

        private ShareAdapter sAdapter;
        private String title, content, targetUrl, picUrl;
        private String[] shareBeanValues = new String[]{"微信好友", "朋友圈", "QQ", "QQ空间", "微博"};
        private int[] shareBeanDrawids = new int[]{R.drawable.umeng_socialize_wechat, R.drawable.umeng_socialize_wxcircle,
                R.drawable.umeng_socialize_qq_on, R.drawable.umeng_socialize_qzone_on,
                R.drawable.umeng_socialize_sina_on};
        private SHARE_MEDIA[] shareBeanMedias = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA};
        private View mMenuView;
        private GridView gridView;
        private TextView cancelTv;
        private List<ShareBean> shareBeans = new ArrayList();
        private Context context;

        public SharePopupWindow(Context context, String title, String content, String targetUrl, String picUrl) {
            super(context);

            this.context = context;
            this.title = title;
            this.content = content;
            this.targetUrl = targetUrl;
            this.picUrl = picUrl;

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.share_popupwindow_view, null);
            gridView = (GridView) mMenuView.findViewById(R.id.gridView);
            cancelTv = (TextView) mMenuView.findViewById(R.id.cancel);
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            shareBeans = initShareBean();
            sAdapter = new ShareAdapter();
            gridView.setAdapter(sAdapter);
            gridView.setOnItemClickListener(this);
            //设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            //设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            //设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            //设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);

            //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            mMenuView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = mMenuView.findViewById(R.id.gridView).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });

        }

        private List<ShareBean> initShareBean() {
            List<ShareBean> sBeans = new ArrayList();
            ShareBean sBean;

            for (int i = 0; i < shareBeanMedias.length; i++) {
                sBean = new ShareBean(shareBeanMedias[i], shareBeanDrawids[i], shareBeanValues[i]);
                sBeans.add(sBean);
            }

            return sBeans;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShareBean sBean = shareBeans.get(position);
            shareTo((Activity) context, sBean, title, content, targetUrl, picUrl);
            if (sBean.media != SHARE_MEDIA.YNOTE)
                this.dismiss();
        }

        /**
         * 分享adapter
         *
         * @author Administrator
         */
        class ShareAdapter extends BaseAdapter {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                HolderView holder = null;
                if (convertView == null) {
                    holder = new HolderView();
                    convertView = LayoutInflater.from(context).inflate(R.layout.share_item, null);
                    holder.img = convertView.findViewById(R.id.img);
                    holder.text = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(holder);
                } else {
                    holder = (HolderView) convertView.getTag();
                }

                ShareBean sBean = shareBeans.get(position);
                if (!TextUtils.isEmpty(sBean.value)) {
                    holder.img.setVisibility(View.VISIBLE);
                    holder.text.setVisibility(View.VISIBLE);
                    holder.img.setBackgroundResource(sBean.drawid);
                    holder.text.setText(sBean.value);
                } else {
                    holder.img.setVisibility(View.INVISIBLE);
                    holder.text.setVisibility(View.INVISIBLE);
                }
                return convertView;
            }

            @Override
            public int getCount() {
                return shareBeans.size();
            }

            @Override
            public Object getItem(int position) {
                return shareBeans.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            class HolderView {

                View img;
                TextView text;

            }

        }

    }

     class ShareBean {
        private SHARE_MEDIA media;
        private int drawid;
        private String value;
        private long postId;
        private long shareId; //shared type’s id // post id, grade id or advice id and so on,
        private int type; // 1: post, 2:grade, 3:advice;
        private String title;
        private String content;
        private String imgUrl;
        private String detailUrl;

        public ShareBean(long postId, long shareId, int type,
                         SHARE_MEDIA media,
                         String title, String content,
                         String imgUrl, String detailUrl) {
            this.postId = postId;
            this.shareId = shareId;
            this.type = type;
            this.media = media;
            this.title = title;
            this.content = content;
            this.imgUrl = imgUrl;
            this.detailUrl = detailUrl;
        }

        public ShareBean(SHARE_MEDIA media, int drawid, String value) {
            this.media = media;
            this.drawid = drawid;
            this.value = value;
        }
    }

}
