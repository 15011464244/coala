// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PriceActivity$$ViewInjector<T extends com.ems.express.ui.PriceActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427581, "field 'mTvFinalPrice'");
    target.mTvFinalPrice = finder.castView(view, 2131427581, "field 'mTvFinalPrice'");
    view = finder.findRequiredView(source, 2131427580, "field 'mTvProduct'");
    target.mTvProduct = finder.castView(view, 2131427580, "field 'mTvProduct'");
    view = finder.findRequiredView(source, 2131427485, "field 'mTvWeight'");
    target.mTvWeight = finder.castView(view, 2131427485, "field 'mTvWeight'");
    view = finder.findRequiredView(source, 2131427579, "field 'mLayoutResult'");
    target.mLayoutResult = view;
    view = finder.findRequiredView(source, 2131427559, "field 'mMailTypeSelection'");
    target.mMailTypeSelection = finder.castView(view, 2131427559, "field 'mMailTypeSelection'");
    view = finder.findRequiredView(source, 2131427577, "field 'mEtWeight'");
    target.mEtWeight = finder.castView(view, 2131427577, "field 'mEtWeight'");
  }

  @Override public void reset(T target) {
    target.mTvFinalPrice = null;
    target.mTvProduct = null;
    target.mTvWeight = null;
    target.mLayoutResult = null;
    target.mMailTypeSelection = null;
    target.mEtWeight = null;
  }
}
