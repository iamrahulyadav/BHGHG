package com.kandara.medicalapp.Util.QuestionIndicator.draw.drawer.type;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.kandara.medicalapp.Util.QuestionIndicator.draw.data.Indicator;

class BaseDrawer {

    Paint paint;
    Indicator indicator;

    BaseDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        this.paint = paint;
        this.indicator = indicator;
    }
}
