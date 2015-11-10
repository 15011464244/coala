// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class RegisterActivity$$ViewInjector<T extends com.ems.express.ui.RegisterActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427532, "field 'mRegistPhone'");
    target.mRegistPhone = finder.castView(view, 2131427532, "field 'mRegistPhone'");
    view = finder.findRequiredView(source, 2131427533, "field 'mCheckAuthCode'");
    target.mCheckAuthCode = finder.castView(view, 2131427533, "field 'mCheckAuthCode'");
    view = finder.findRequiredView(source, 2131427540, "field 'mRegistPWAgain'");
    target.mRegistPWAgain = finder.castView(view, 2131427540, "field 'mRegistPWAgain'");
    view = finder.findRequiredView(source, 2131427534, "field 'btnAuthCode' and method 'toGetAuthCode'");
    target.btnAuthCode = finder.castView(view, 2131427534, "field 'btnAuthCode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toGetAuthCode();
        }
      });
    view = finder.findRequiredView(source, 2131427415, "field 'imgProgress'");
    target.imgProgress = finder.castView(view, 2131427415, "field 'imgProgress'");
    view = finder.findRequiredView(source, 2131427536, "field 'invitedInstruction'");
    target.invitedInstruction = finder.castView(view, 2131427536, "field 'invitedInstruction'");
    view = finder.findRequiredView(source, 2131427535, "field 'mInvitedPhoneNumber'");
    target.mInvitedPhoneNumber = finder.castView(view, 2131427535, "field 'mInvitedPhoneNumber'");
    view = finder.findRequiredView(source, 2131427539, "field 'mRegistPW'");
    target.mRegistPW = finder.castView(view, 2131427539, "field 'mRegistPW'");
    view = finder.findRequiredView(source, 2131427530, "field 'mTabSetPwd' and method 'toSetPwd'");
    target.mTabSetPwd = finder.castView(view, 2131427530, "field 'mTabSetPwd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toSetPwd();
        }
      });
    view = finder.findRequiredView(source, 2131427531, "field 'mCheckSmsContainer'");
    target.mCheckSmsContainer = finder.castView(view, 2131427531, "field 'mCheckSmsContainer'");
    view = finder.findRequiredView(source, 2131427538, "field 'mSetPwdContainer'");
    target.mSetPwdContainer = finder.castView(view, 2131427538, "field 'mSetPwdContainer'");
    view = finder.findRequiredView(source, 2131427529, "field 'mTabCheck' and method 'toCheck'");
    target.mTabCheck = finder.castView(view, 2131427529, "field 'mTabCheck'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toCheck();
        }
      });
    view = finder.findRequiredView(source, 2131427414, "field 'rllayout'");
    target.rllayout = finder.castView(view, 2131427414, "field 'rllayout'");
    view = finder.findRequiredView(source, 2131427542, "method 'toRegister'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toRegister();
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
    view = finder.findRequiredView(source, 2131427528, "method 'toLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toLogin();
        }
      });
    view = finder.findRequiredView(source, 2131427537, "method 'toNextTep'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toNextTep();
        }
      });
  }

  @Override public void reset(T target) {
    target.mRegistPhone = null;
    target.mCheckAuthCode = null;
    target.mRegistPWAgain = null;
    target.btnAuthCode = null;
    target.imgProgress = null;
    target.invitedInstruction = null;
    target.mInvitedPhoneNumber = null;
    target.mRegistPW = null;
    target.mTabSetPwd = null;
    target.mCheckSmsContainer = null;
    target.mSetPwdContainer = null;
    target.mTabCheck = null;
    target.rllayout = null;
  }
}
