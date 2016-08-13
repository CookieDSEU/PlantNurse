package com.kot32.ksimplelibrary.widgets.view.kenburnsview;

import android.graphics.RectF;
import android.transition.Transition;

public interface TransitionGenerator {

    /**
     * Generates the next transition to be played by the {@link KenBurnsView}.
     * @param drawableBounds the bounds of the drawable to be shown in the {@link KenBurnsView}.
     * @param viewport the rect that represents the viewport where
     *                 the transition will be played in. This is usually the bounds of the
     *                 {@link KenBurnsView}.
     * @return a {@link Transition} object to be played by the {@link KenBurnsView}.
     */
    public com.kot32.ksimplelibrary.widgets.view.kenburnsview.Transition generateNextTransition(RectF drawableBounds, RectF viewport);

}