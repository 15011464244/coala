// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class RegisterActivity$$ViewInjector<T extends com.ems.express.ui.RegisterActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427592, "field 'mTabSetPwd' and method 'toSetPwd'");
    target.mTabSetPwd = finder.castView(view, 2131427592, "field 'mTabSetPwd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toSetPwd();
        }
      });
    view = finder.findRequiredView(source, 2131427593, "field 'mCheckSmsContainer'");
    target.mCheckSmsContainer = finder.castView(view, 2131427593, "field 'mCheckSmsContainer'");
    view = finder.findRequiredView(source, 2131427475, "field 'rllayout'");
    target.rllayout = finder.castView(view, 2131427475, "field 'rllayout'");
    view = finder.findRequiredView(source, 2131427591, "field 'mTabCheck' and method 'toCheck'");
    target.mTabCheck = finder.castView(view, 2131427591, "field 'mTabCheck'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toCheck();
        }
      });
    view = finder.findRequiredView(source, 2131427598, "field 'invitedInstruction'");
    target.invitedInstruction = finder.castView(view, 2131427598, "field 'invitedInstruction'");
    view = finder.findRequiredView(source, 2131427595, "field 'mCheckAuthCode'");
    target.mCheckAuthCode = finder.castView(view, 2131427595, "field 'mCheckAuthCode'");
    view = finder.findRequiredView(source, 2131427600, "field 'mSetPwdContainer'");
    target.mSetPwdContainer = finder.castView(view, 2131427600, "field 'mSetPwdContainer'");
    view = finder.findRequiredView(source, 2131427602, "field 'mRegistPWAgain'");
    target.mRegistPWAgain = finder.castView(view, 2131427602, "field 'mRegistPWAgain'");
    view = finder.findRequiredView(source, 2131427596, "field 'btnAuthCode' and method 'toGetAuthCode'");
    target.btnAuthCode = finder.castView(view, 2131427596, "field 'btnAuthCode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toGetAuthCode();
        }
      });
    view = finder.findRequiredView(source, 2131427476, "field 'imgProgress'");
    target.imgProgress = finder.castView(view, 2131427476, "field 'imgProgress'");
    view = finder.findRequiredView(source, 2131427594, "field 'mRegistPhone'");
    target.mRegistPhone = finder.castView(view, 2131427594, "field 'mRegistPhone'");
    view = finder.findRequiredView(source, 2131427597, "field 'mInvitedPhoneNumber'");
    target.mInvitedPhoneNumber = finder.castView(view, 2131427597, "field 'mInvitedPhoneNumber'");
    view = finder.findRequiredView(source, 2131427601, "field 'mRegistPW'");
    target.mRegistPW = finder.castView(view, 2131427601, "field 'mRegistPW'");
    view = finder.findRequiredView(source, 2131427604, "method 'toRegister'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toRegister();
        }
      });
    view = finder.findRequiredView(source, 2131427599, "method 'toNextTep'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toNextTep();
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
    view = finder.findRequiredView(source, 2131427590, "method 'toLogin'");
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
    target.mTabSetPwd = null;
    target.mCheckSmsContainer = null;
    target.rllayout = null;
    target.mTabCheck = null;
    target.invitedInstruction = null;
    target.mCheckAuthCode = null;
    target.mSetPwdContainer = null;
    target.mRegistPWAgain = null;
    target.btnAuthCode = null;
    target.imgProgress = null;
    target.mRegistPhone = null;
    target.mInvitedPhoneNumber = null;
    target.mRegistPW = null;
  }
}
