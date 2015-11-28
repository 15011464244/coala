// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class TimeQueryActivity$$ViewInjector<T extends com.ems.express.ui.TimeQueryActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427649, "field 'mTvDateLine'");
    target.mTvDateLine = finder.castView(view, 2131427649, "field 'mTvDateLine'");
    view = finder.findRequiredView(source, 2131427652, "field 'mTimePicker'");
    target.mTimePicker = finder.castView(view, 2131427652, "field 'mTimePicker'");
    view = finder.findRequiredView(source, 2131427647, "field 'mDateSelectionView'");
    target.mDateSelectionView = view;
    view = finder.findRequiredView(source, 2131427645, "field 'mSelecteDate'");
    target.mSelecteDate = finder.castView(view, 2131427645, "field 'mSelecteDate'");
    view = finder.findRequiredView(source, 2131427648, "field 'mTvDate'");
    target.mTvDate = finder.castView(view, 2131427648, "field 'mTvDate'");
    view = finder.findRequiredView(source, 2131427651, "field 'mDatePicker'");
    target.mDatePicker = finder.castView(view, 2131427651, "field 'mDatePicker'");
    view = finder.findRequiredView(source, 2131427811, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427811, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427439, "field 'mTvTime'");
    target.mTvTime = finder.castView(view, 2131427439, "field 'mTvTime'");
    view = finder.findRequiredView(source, 2131427650, "field 'mTvTimeLine'");
    target.mTvTimeLine = finder.castView(view, 2131427650, "field 'mTvTimeLine'");
    view = finder.findRequiredView(source, 2131427646, "field 'mDateArrowView'");
    target.mDateArrowView = finder.castView(view, 2131427646, "field 'mDateArrowView'");
  }

  @Override public void reset(T target) {
    target.mTvDateLine = null;
    target.mTimePicker = null;
    target.mDateSelectionView = null;
    target.mSelecteDate = null;
    target.mTvDate = null;
    target.mDatePicker = null;
    target.mTitle = null;
    target.mTvTime = null;
    target.mTvTimeLine = null;
    target.mDateArrowView = null;
  }
}
