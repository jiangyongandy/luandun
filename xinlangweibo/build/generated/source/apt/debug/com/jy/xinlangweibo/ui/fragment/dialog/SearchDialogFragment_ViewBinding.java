// Generated code from Butter Knife. Do not modify!
package com.jy.xinlangweibo.ui.fragment.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.widget.emotionkeyboard.EmoticonsEditText;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class SearchDialogFragment_ViewBinding<T extends SearchDialogFragment> implements Unbinder {
  protected T target;

  private View view2131624278;

  private View view2131624282;

  public SearchDialogFragment_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.rvHistorySuggesst = finder.findRequiredViewAsType(source, R.id.rv_history_suggesst, "field 'rvHistorySuggesst'", RecyclerView.class);
    target.etSearch = finder.findRequiredViewAsType(source, R.id.et_search, "field 'etSearch'", EmoticonsEditText.class);
    view = finder.findRequiredView(source, R.id.iv_search, "field 'ivSearch' and method 'onClick'");
    target.ivSearch = finder.castView(view, R.id.iv_search, "field 'ivSearch'", ImageView.class);
    view2131624278 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, R.id.tv_cancel, "field 'tvCancel' and method 'onClick'");
    target.tvCancel = finder.castView(view, R.id.tv_cancel, "field 'tvCancel'", TextView.class);
    view2131624282 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rvHistorySuggesst = null;
    target.etSearch = null;
    target.ivSearch = null;
    target.tvCancel = null;

    view2131624278.setOnClickListener(null);
    view2131624278 = null;
    view2131624282.setOnClickListener(null);
    view2131624282 = null;

    this.target = null;
  }
}
