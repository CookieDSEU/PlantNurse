package com.plantnurse.plantnurse.utils.carousel;
/**
 * Created by Eason_Tao on 2016/8/27.
 */
public class ItemTransformation {

    final float mScaleX;
    final float mScaleY;
    final float mTranslationX;
    final float mTranslationY;

    public ItemTransformation(final float scaleX, final float scaleY, final float translationX, final float translationY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
        mTranslationX = translationX;
        mTranslationY = translationY;
    }
}