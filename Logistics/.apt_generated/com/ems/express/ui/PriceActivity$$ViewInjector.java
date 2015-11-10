// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PriceActivity$$ViewInjector<T extends com.ems.express.ui.PriceActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427518, "field 'mTvProduct'");
    target.mTvProduct = finder.castView(view, 2131427518, "field 'mTvProduct'");
    view = finder.findRequiredView(source, 2131427519, "field 'mTvFinalPrice'");
    target.mTvFinalPrice = finder.castView(view, 2131427519, "field 'mTvFinalPrice'");
    view = finder.findRequiredView(source, 2131427497, "field 'mMailTypeSelection'");
    target.mMailTypeSelection = finder.castView(view, 2131427497, "field 'mMailTypeSelection'");
    view = finder.findRequiredView(source, 2131427424, "field 'mTvWeight'");
    target.mTvWeight = finder.castView(view, 2131427424, "field 'mTvWeight'");
    view = finder.findRequiredView(source, 2131427517, "field 'mLayoutResult'");
    target.mLayoutResult = view;
    view = finder.findRequiredView(source, 2131427515, "field 'mEtWeight'");
    target.mEtWeight = finder.castView(view, 2131427515, "field 'mEtWeight'");
  }

  @Override public void reset(T target) {
    target.mTvProduct = null;
    target.mTvFinalPrice = null;
    target.mMailTypeSelection = null;
    target.mTvWeight = null;
    target.mLayoutResult = null;
    target.mEtWeight = null;
  }
}
