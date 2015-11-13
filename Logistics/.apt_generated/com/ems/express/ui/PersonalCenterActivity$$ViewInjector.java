// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PersonalCenterActivity$$ViewInjector<T extends com.ems.express.ui.PersonalCenterActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427489, "field 'mAddressView'");
    target.mAddressView = finder.castView(view, 2131427489, "field 'mAddressView'");
    view = finder.findRequiredView(source, 2131427487, "field 'mQrCodeView'");
    target.mQrCodeView = finder.castView(view, 2131427487, "field 'mQrCodeView'");
    view = finder.findRequiredView(source, 2131427480, "field 'mIconView'");
    target.mIconView = finder.castView(view, 2131427480, "field 'mIconView'");
    view = finder.findRequiredView(source, 2131427484, "field 'mPhoneView'");
    target.mPhoneView = finder.castView(view, 2131427484, "field 'mPhoneView'");
    view = finder.findRequiredView(source, 2131427482, "field 'mNameView'");
    target.mNameView = finder.castView(view, 2131427482, "field 'mNameView'");
    view = finder.findRequiredView(source, 2131427485, "method 'showQrcode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showQrcode();
        }
      });
    view = finder.findRequiredView(source, 2131427488, "method 'toEditAddress'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditAddress();
        }
      });
    view = finder.findRequiredView(source, 2131427483, "method 'toEditPhone'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditPhone();
        }
      });
    view = finder.findRequiredView(source, 2131427479, "method 'toEditImage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditImage();
        }
      });
    view = finder.findRequiredView(source, 2131427481, "method 'toEditName'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditName();
        }
      });
  }

  @Override public void reset(T target) {
    target.mAddressView = null;
    target.mQrCodeView = null;
    target.mIconView = null;
    target.mPhoneView = null;
    target.mNameView = null;
  }
}
