// Generated code from Butter Knife. Do not modify!
package com.jy.xinlangweibo.ui.fragment.dialog;

import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.widget.NestListView.NestFullListView;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class SearchDialogFragment$SeachSuggestItem_ViewBinding<T extends SearchDialogFragment.SeachSuggestItem> implements Unbinder {
  protected T target;

  public SearchDialogFragment$SeachSuggestItem_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.tvItemSearchType = finder.findRequiredViewAsType(source, R.id.tv_item_search_type, "field 'tvItemSearchType'", TextView.class);
    target.nflRecordList = finder.findRequiredViewAsType(source, R.id.nfl_recordList, "field 'nflRecordList'", NestFullListView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvItemSearchType = null;
    target.nflRecordList = null;

    this.target = null;
  }
}
