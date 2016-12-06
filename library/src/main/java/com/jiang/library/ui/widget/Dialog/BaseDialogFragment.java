package com.jiang.library.ui.widget.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiang.library.R;

import java.io.Serializable;


/**
 * Created by JIANG on 2016/11/9.
 */

public class BaseDialogFragment extends DialogFragment {

    private Builder builder;
    private TextView title;
    private TextView content;
    private TextView tvLeft;
    private TextView tvRight;
    private PositiveBtnOnclickListener positiveBtnOnclickListener;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View v = inflater.inflate(R.layout.dialog_base, container, false);
        title = (TextView) v.findViewById(R.id.tv_dialogBase_title);
        content = (TextView) v.findViewById(R.id.tv_dialogBase_content);
        tvLeft = (TextView) v.findViewById(R.id.tv_dialogBase_left);
        tvRight = (TextView) v.findViewById(R.id.tv_dialogBase_right);
        Serializable serializable = getArguments().getSerializable("builder");
        if(builder == null || !(serializable instanceof Builder)) {
            throw new RuntimeException("BaseDialogFragment's builder need is not null or builder isn't the type of Builder");
        }
        builder = (Builder) serializable;
        if(!builder.title.isEmpty())
            title.setText(builder.title);
        if(!builder.conTent.isEmpty())
            content.setText(builder.conTent);
        if(!builder.leftText.isEmpty())
            tvLeft.setText(builder.leftText);
        if(!builder.rightText.isEmpty())
            tvRight.setText(builder.rightText);
        if(builder.positiveBtnOnclickListener != null)
            positiveBtnOnclickListener = builder.positiveBtnOnclickListener;

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveBtnOnclickListener != null) {
                    positiveBtnOnclickListener.onClick();
                }else {
                    dismiss();
                }
            }
        });
        return v;
    }

    public interface PositiveBtnOnclickListener {
        void onClick();
    }

    public static class Builder implements Serializable{

        private String conTent;
        private String title;
        private String rightText;
        private String leftText;
        private PositiveBtnOnclickListener positiveBtnOnclickListener;

        public Builder() {

        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder conTent(String conTent) {
            this.conTent = conTent;
            return this;
        }

        public Builder rightText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public Builder leftText(String leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder setPositveBtnOnClickListener(PositiveBtnOnclickListener listener) {
            this.positiveBtnOnclickListener = listener;
            return this;
        }

        public BaseDialogFragment build() {
            BaseDialogFragment dialogFragment = new BaseDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("builder",this);
            dialogFragment.setArguments(bundle);
            return  dialogFragment;
        }

        public BaseDialogFragment show(FragmentManager fragmentManager,String tag) {
            BaseDialogFragment dialogFragment = build();
            dialogFragment.show(fragmentManager,tag);
            return dialogFragment;
        }

    }

}
