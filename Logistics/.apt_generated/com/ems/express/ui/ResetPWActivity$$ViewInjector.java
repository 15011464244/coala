// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ResetPWActivity$$ViewInjector<T extends com.ems.express.ui.ResetPWActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427545, "field 'mPW'");
    target.mPW = finder.castView(view, 2131427545, "field 'mPW'");
    view = finder.findRequiredView(source, 2131427544, "field 'mETAuthCode'");
    target.mETAuthCode = finder.castView(view, 2131427544, "field 'mETAuthCode'");
    view = finder.findRequiredView(source, 2131427534, "field 'mTVauthCode' and method 'getAuthCode'");
    target.mTVauthCode = finder.castView(view, 2131427534, "field 'mTVauthCode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getAuthCode();
        }
      });
    view = finder.findRequiredView(source, 2131427543, "field 'mPhone'");
    target.mPhone = finder.castView(view, 2131427543, "field 'mPhone'");
    view = finder.findRequiredView(source, 2131427546, "method 'toCommit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toCommit();
        }
      });
  }

  @Override public void reset(T target) {
    target.mPW = null;
    target.mETAuthCode = null;
    target.mTVauthCode = null;
    target.mPhone = null;
  }
}
