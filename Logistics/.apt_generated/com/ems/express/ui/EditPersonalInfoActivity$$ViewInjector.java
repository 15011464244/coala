// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class EditPersonalInfoActivity$$ViewInjector<T extends com.ems.express.ui.EditPersonalInfoActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427812, "field 'mBtnSave' and method 'toSave'");
    target.mBtnSave = finder.castView(view, 2131427812, "field 'mBtnSave'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toSave();
        }
      });
    view = finder.findRequiredView(source, 2131427431, "field 'mNameView'");
    target.mNameView = finder.castView(view, 2131427431, "field 'mNameView'");
    view = finder.findRequiredView(source, 2131427811, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427811, "field 'mTitle'");
  }

  @Override public void reset(T target) {
    target.mBtnSave = null;
    target.mNameView = null;
    target.mTitle = null;
  }
}
