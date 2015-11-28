// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PersonalCenterActivity$$ViewInjector<T extends com.ems.express.ui.PersonalCenterActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427551, "field 'mAddressView'");
    target.mAddressView = finder.castView(view, 2131427551, "field 'mAddressView'");
    view = finder.findRequiredView(source, 2131427545, "field 'mPhoneView'");
    target.mPhoneView = finder.castView(view, 2131427545, "field 'mPhoneView'");
    view = finder.findRequiredView(source, 2131427543, "field 'mNameView'");
    target.mNameView = finder.castView(view, 2131427543, "field 'mNameView'");
    view = finder.findRequiredView(source, 2131427548, "field 'mQrCodeView'");
    target.mQrCodeView = finder.castView(view, 2131427548, "field 'mQrCodeView'");
    view = finder.findRequiredView(source, 2131427541, "field 'mIconView'");
    target.mIconView = finder.castView(view, 2131427541, "field 'mIconView'");
    view = finder.findRequiredView(source, 2131427544, "method 'toEditPhone'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditPhone();
        }
      });
    view = finder.findRequiredView(source, 2131427546, "method 'showQrcode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showQrcode();
        }
      });
    view = finder.findRequiredView(source, 2131427542, "method 'toEditName'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditName();
        }
      });
    view = finder.findRequiredView(source, 2131427549, "method 'toShare'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toShare();
        }
      });
    view = finder.findRequiredView(source, 2131427550, "method 'toEditAddress'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditAddress();
        }
      });
    view = finder.findRequiredView(source, 2131427540, "method 'toEditImage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toEditImage();
        }
      });
  }

  @Override public void reset(T target) {
    target.mAddressView = null;
    target.mPhoneView = null;
    target.mNameView = null;
    target.mQrCodeView = null;
    target.mIconView = null;
  }
}
