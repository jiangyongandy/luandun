package com.jy.xinlangweibo.widget.emotionkeyboard.I;

import com.jy.xinlangweibo.bean.EmoticonBean;

public interface IView {
    void onItemClick(EmoticonBean bean);
    void onItemDisplay(EmoticonBean bean);
    void onPageChangeTo(int position);
}