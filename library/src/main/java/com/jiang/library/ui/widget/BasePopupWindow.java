package com.jiang.library.ui.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by JIANG on 2016/10/5.
 */
public class BasePopupWindow extends PopupWindow implements View.OnClickListener {

    private ViewGroup activityView;
    private Builder builder;
    private OnDismissListenerDelegate onDismissListenerDelegate;
    @Nullable
    View blackOverlay;
    int blackOverlayColor;

    private BasePopupWindow(Builder builder) {
        super(builder.popupWindowView, builder.width,
                builder.heigth, builder.focusable);
        this.builder = builder;
        this.activityView = (ViewGroup) builder.activity.findViewById(android.R.id.content);
        blackOverlayColor = builder.blackOverlayColor;
        // 设置允许在外点击消失
        setOutsideTouchable(builder.touchable);
        // 刷新状态
        update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        setBackgroundDrawable(builder.background);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                activityView.removeView(blackOverlay);
                if (onDismissListenerDelegate != null)
                    onDismissListenerDelegate.onDismiss();
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        blackOverlay = new View(builder.activity);
        blackOverlay.setBackgroundColor(blackOverlayColor);
        blackOverlay.setOnClickListener(this);
        activityView.addView(blackOverlay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (!isShowing()) {
            super.showAtLocation(parent, gravity, x, y);
        } else {
            dismiss();
        }
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        blackOverlay = new View(builder.activity);
        blackOverlay.setBackgroundColor(blackOverlayColor);
        activityView.addView(blackOverlay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (!isShowing()) {
            super.showAsDropDown(anchor, xoff, yoff);
        } else {
            dismiss();
        }
    }

    @Override
    public void onClick(View v) {
//        if(v == blackOverlay) {
//            activityView.removeView(blackOverlay);
//            blackOverlay = null;
//        }
    }

    public interface OnDismissListenerDelegate {
        void onDismiss();
    }

    public void setOnDismissListenerDelegate(OnDismissListenerDelegate onDismissListenerDelegate) {
        this.onDismissListenerDelegate = onDismissListenerDelegate;
    }

    public static class Builder {
        private ViewGroup activityView;
        private Activity activity;
        private View popupWindowView;
        private int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int heigth = ViewGroup.LayoutParams.WRAP_CONTENT;
        private boolean focusable = true;
        private boolean touchable = true;
        private Drawable background;
        private int blackOverlayColor = Color.parseColor("#32000000");
        private OnDismissListenerDelegate onDismissListenerDelegate;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setPopupWindowView(View popupWindowView) {
            this.popupWindowView = popupWindowView;
            return this;
        }

        public Builder setPopupWindowView(int layoutRes) {
            this.popupWindowView = LayoutInflater.from(activity).inflate(layoutRes, null);
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeigth(int heigth) {
            this.heigth = heigth;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            this.touchable = touchable;
            return this;
        }

        public Builder setBackground(Drawable background) {
            this.background = background;
            return this;
        }

        public void setBlackOverlayColor(int blackOverlayColor) {
            this.blackOverlayColor = blackOverlayColor;
        }

        public Builder setOnDismissListener(OnDismissListenerDelegate onDismissListenerDelegate) {
            this.onDismissListenerDelegate = onDismissListenerDelegate;
            return this;
        }


        public BasePopupWindow show(View anchor, int xoff, int yoff) {
            initEmptyParams();
            BasePopupWindow basePopupWindow = new BasePopupWindow(this);
            if (basePopupWindow.isShowing()) {
                basePopupWindow.dismiss();
            } else {
                basePopupWindow.showAsDropDown(anchor, xoff, yoff);
            }
            return basePopupWindow;
        }

        public BasePopupWindow build() {
            initEmptyParams();
            return new BasePopupWindow(this);
        }

        private void initEmptyParams() {
            if (popupWindowView == null)
                throw new RuntimeException("popupWindowView can't is null");
            if (background == null)
                background = new ColorDrawable(0xffffff);
        }
    }
}
