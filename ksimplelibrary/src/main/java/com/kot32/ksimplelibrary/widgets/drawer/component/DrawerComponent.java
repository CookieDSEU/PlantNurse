package com.kot32.ksimplelibrary.widgets.drawer.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kot32.ksimplelibrary.manager.task.DownloadImageTask;
import com.kot32.ksimplelibrary.util.tools.DisplayUtil;
import com.kot32.ksimplelibrary.widgets.view.kenburnsview.KenBurnsView;

/**
 * Created by kot32 on 15/11/25.
 * 侧滑菜单的各种组件
 */
public class DrawerComponent {

    private abstract static class Component extends RelativeLayout {

        public Component(Context context) {
            super(context);
        }

        public Component(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }


    /**
     * 侧滑菜单的头部组件，能够自定义圆角头像以及背景图片的展示模式
     */
    public static class DrawerHeader extends Component {

        private ImageView background;

        private DrawerHeaderStyle headerStyle;

        private Context mContext;

        private SimpleDraweeView avatar;

        private TextView nick;

        private TextView introduction;

        private static final int ID_AVATAR = 101;

        private static final int ID_NICKNAME = 102;

        /**
         * NORMAL 普通背景图模式
         * KENBURNS 平移渐变背景图模式
         */
        public enum DrawerHeaderStyle {
            NORMAL, KENBURNS
        }

        public DrawerHeader(String imagePath, Context context) {
            this(DrawerHeaderStyle.NORMAL, imagePath, context);
        }

        public DrawerHeader(int drawableID, Context context) {
            this(DrawerHeaderStyle.NORMAL, drawableID, context);
        }

        public DrawerHeader(DrawerHeaderStyle style, String imagePath, Context context) {
            super(context);
            headerStyle = style;
            mContext = context;
            init();
            if (style == DrawerHeaderStyle.NORMAL) {
                background.setImageURI(Uri.parse(imagePath));
            } else {
                new DownloadImageTask(new DownloadImageTask.OnSuccessCallBack() {
                    @Override
                    public void onLoaded(Bitmap bitmap) {
                        int fitSize = DisplayUtil.getWindowSize(mContext).x / 6 * 5;
                        if (bitmap.getWidth() > fitSize) {
                            float ratio = ((float) fitSize) / bitmap.getWidth();
                            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * ratio), (int) (bitmap.getHeight() * ratio), false);
                        }

                        background.setImageBitmap(bitmap);
                    }
                }).execute(imagePath);
            }
        }

        public DrawerHeader(DrawerHeaderStyle style, int drawableID, Context context) {
            super(context);
            headerStyle = style;
            mContext = context;
            init();
            if (style == DrawerHeaderStyle.NORMAL) {
                background.setImageURI(Uri.parse("res://" + mContext.getPackageName() + "/" + drawableID));
            } else {
                background.setImageResource(drawableID);
            }

        }

        private void init() {

            if (headerStyle == DrawerHeaderStyle.NORMAL) {
                background = new SimpleDraweeView(mContext);
            } else if (headerStyle == DrawerHeaderStyle.KENBURNS) {
                background = new KenBurnsView(mContext);
            }

            addView(background, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        public void addAvatar(int placeHolderImageID, String avatarURL) {

            LayoutParams layoutParams = new LayoutParams(DisplayUtil.dip2px(mContext, 67), DisplayUtil.dip2px(mContext, 67));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
            layoutParams.setMargins(DisplayUtil.dip2px(mContext, 25), DisplayUtil.dip2px(mContext, 23), 0, 0);
            addAvatar(placeHolderImageID, avatarURL, layoutParams, null);
        }

        public void addAvatar(int placeHolderImageID, String avatarURL,OnClickListener onClickListener) {

            LayoutParams layoutParams = new LayoutParams(DisplayUtil.dip2px(mContext, 67), DisplayUtil.dip2px(mContext, 67));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
            layoutParams.setMargins(DisplayUtil.dip2px(mContext, 25), DisplayUtil.dip2px(mContext, 23), 0, 0);
            addAvatar(placeHolderImageID, avatarURL, layoutParams, onClickListener);
        }


        public void addAvatar(int placeHolderImageID, String avatarURL, @NonNull LayoutParams layoutParams, final OnClickListener onClickListener) {
            avatar = new SimpleDraweeView(mContext);
            avatar.setId(ID_AVATAR);
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setPlaceholderImage(getResources().getDrawable(placeHolderImageID))
                    .setRoundingParams(RoundingParams.asCircle())
                    .build();
            avatar.setHierarchy(hierarchy);
            avatar.setImageURI(Uri.parse(avatarURL));
            avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                }
            });
            addView(avatar, layoutParams);

        }

        public void addNickName(String nickName) {
            LayoutParams nickParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (avatar != null) {
                nickParams.addRule(RelativeLayout.BELOW, ID_AVATAR);
                nickParams.setMargins(DisplayUtil.dip2px(mContext, 25), DisplayUtil.dip2px(mContext, 15), 0, 0);
            } else {
                nickParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
                nickParams.setMargins(DisplayUtil.dip2px(mContext, 25), 0, 0, DisplayUtil.dip2px(mContext, 30));
            }

            addNickName(nickName, nickParams);
        }

        public void addNickName(String nickName, LayoutParams layoutParams) {
            nick = new TextView(mContext);
            nick.setText(nickName);
            nick.setTextColor(Color.WHITE);
            nick.setTextSize(17);
            nick.setId(ID_NICKNAME);
            addView(nick, layoutParams);
        }

        public void addIntroduction(String introString) {
            LayoutParams nickParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (avatar != null) {
                nickParams.addRule(RelativeLayout.BELOW, ID_NICKNAME);
                nickParams.setMargins(DisplayUtil.dip2px(mContext, 25), DisplayUtil.dip2px(mContext, 8), 0, 0);
            } else {
                nickParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
                nickParams.setMargins(DisplayUtil.dip2px(mContext, 25), 0, 0, DisplayUtil.dip2px(mContext, 15));
            }

            addIntroduction(introString, nickParams);
        }

        public void addIntroduction(String introString, LayoutParams layoutParams) {
            introduction = new TextView(mContext);
            introduction.setText(introString);
            introduction.setTextColor(0x55ffffff);
            introduction.setTextSize(13);
            addView(introduction, layoutParams);
        }


        public void changeAvatorURL(String avatarURL) {
            if (avatar != null) {
                avatar.setImageURI(Uri.parse(avatarURL));
            }
        }

        public void changeNickName(String nickString) {
            if (nick != null) {
                nick.setText(nickString);
            }
        }

        public void changeIntroduction(String introString) {
            if (introduction != null) {
                introduction.setText(introString);
            }
        }


    }


}
