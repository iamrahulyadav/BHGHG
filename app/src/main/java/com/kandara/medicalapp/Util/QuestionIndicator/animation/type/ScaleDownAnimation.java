package com.kandara.medicalapp.Util.QuestionIndicator.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.support.annotation.NonNull;
import com.kandara.medicalapp.Util.QuestionIndicator.animation.controller.ValueController;

public class ScaleDownAnimation extends ScaleAnimation {

	public ScaleDownAnimation(@NonNull ValueController.UpdateListener listener) {
		super(listener);
	}

	@NonNull
	@Override
	protected PropertyValuesHolder createScalePropertyHolder(boolean isReverse) {
		String propertyName;
		int startRadiusValue;
		int endRadiusValue;

		if (isReverse) {
			propertyName = ANIMATION_SCALE_REVERSE;
			startRadiusValue = (int) (radius * scaleFactor);
			endRadiusValue = radius;
		} else {
			propertyName = ANIMATION_SCALE;
			startRadiusValue = radius;
			endRadiusValue = (int) (radius * scaleFactor);
		}

		PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue);
		holder.setEvaluator(new IntEvaluator());

		return holder;
	}
}
