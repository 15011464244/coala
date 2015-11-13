// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class TimeQueryActivity$$ViewInjector<T extends com.ems.express.ui.TimeQueryActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427747, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427747, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427583, "field 'mSelecteDate'");
    target.mSelecteDate = finder.castView(view, 2131427583, "field 'mSelecteDate'");
    view = finder.findRequiredView(source, 2131427584, "field 'mDateArrowView'");
    target.mDateArrowView = finder.castView(view, 2131427584, "field 'mDateArrowView'");
    view = finder.findRequiredView(source, 2131427585, "field 'mDateSelectionView'");
    target.mDateSelectionView = view;
    view = finder.findRequiredView(source, 2131427588, "field 'mTvTimeLine'");
    target.mTvTimeLine = finder.castView(view, 2131427588, "field 'mTvTimeLine'");
    view = finder.findRequiredView(source, 2131427587, "field 'mTvDateLine'");
    target.mTvDateLine = finder.castView(view, 2131427587, "field 'mTvDateLine'");
    view = finder.findRequiredView(source, 2131427589, "field 'mDatePicker'");
    target.mDatePicker = finder.castView(view, 2131427589, "field 'mDatePicker'");
    view = finder.findRequiredView(source, 2131427378, "field 'mTvTime'");
    target.mTvTime = finder.castView(view, 2131427378, "field 'mTvTime'");
    view = finder.findRequiredView(source, 2131427586, "field 'mTvDate'");
    target.mTvDate = finder.castView(view, 2131427586, "field 'mTvDate'");
    view = finder.findRequiredView(source, 2131427590, "field 'mTimePicker'");
    target.mTimePicker = finder.castView(view, 2131427590, "field 'mTimePicker'");
  }

  @Override public void reset(T target) {
    target.mTitle = null;
    target.mSelecteDate = null;
    target.mDateArrowView = null;
    target.mDateSelectionView = null;
    target.mTvTimeLine = null;
    target.mTvDateLine = null;
    target.mDatePicker = null;
    target.mTvTime = null;
    target.mTvDate = null;
    target.mTimePicker = null;
  }
}
