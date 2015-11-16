// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends com.ems.express.ui.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427409, "field 'tv_register' and method 'toRegister'");
    target.tv_register = finder.castView(view, 2131427409, "field 'tv_register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toRegister();
        }
      });
    view = finder.findRequiredView(source, 2131427381, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427381, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427415, "field 'imgProgress'");
    target.imgProgress = finder.castView(view, 2131427415, "field 'imgProgress'");
    view = finder.findRequiredView(source, 2131427414, "field 'rllayout'");
    target.rllayout = finder.castView(view, 2131427414, "field 'rllayout'");
    view = finder.findRequiredView(source, 2131427411, "field 'mLoginPW'");
    target.mLoginPW = finder.castView(view, 2131427411, "field 'mLoginPW'");
    view = finder.findRequiredView(source, 2131427410, "field 'mLoginPhone'");
    target.mLoginPhone = finder.castView(view, 2131427410, "field 'mLoginPhone'");
    view = finder.findRequiredView(source, 2131427412, "method 'toForgetPW'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toForgetPW();
        }
      });
    view = finder.findRequiredView(source, 2131427408, "method 'toBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toBack();
        }
      });
    view = finder.findRequiredView(source, 2131427413, "method 'toLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toLogin();
        }
      });
  }

  @Override public void reset(T target) {
    target.tv_register = null;
    target.mTitle = null;
    target.imgProgress = null;
    target.rllayout = null;
    target.mLoginPW = null;
    target.mLoginPhone = null;
  }
}
