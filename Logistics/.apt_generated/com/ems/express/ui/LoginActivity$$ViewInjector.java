// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends com.ems.express.ui.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427475, "field 'rllayout'");
    target.rllayout = finder.castView(view, 2131427475, "field 'rllayout'");
    view = finder.findRequiredView(source, 2131427471, "field 'mLoginPhone'");
    target.mLoginPhone = finder.castView(view, 2131427471, "field 'mLoginPhone'");
    view = finder.findRequiredView(source, 2131427476, "field 'imgProgress'");
    target.imgProgress = finder.castView(view, 2131427476, "field 'imgProgress'");
    view = finder.findRequiredView(source, 2131427470, "field 'tv_register' and method 'toRegister'");
    target.tv_register = finder.castView(view, 2131427470, "field 'tv_register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toRegister();
        }
      });
    view = finder.findRequiredView(source, 2131427442, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427442, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427472, "field 'mLoginPW'");
    target.mLoginPW = finder.castView(view, 2131427472, "field 'mLoginPW'");
    view = finder.findRequiredView(source, 2131427474, "method 'toLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toLogin();
        }
      });
    view = finder.findRequiredView(source, 2131427473, "method 'toForgetPW'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toForgetPW();
        }
      });
    view = finder.findRequiredView(source, 2131427469, "method 'toBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toBack();
        }
      });
  }

  @Override public void reset(T target) {
    target.rllayout = null;
    target.mLoginPhone = null;
    target.imgProgress = null;
    target.tv_register = null;
    target.mTitle = null;
    target.mLoginPW = null;
  }
}
