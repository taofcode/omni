package com.mobilebanking.core.uiutils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
* 
* This view will auto determine the width or height by determining if the 
* height or width is set and scale the other dimension depending on the images dimension
* 
* This view also contains an ImageChangeListener which calls changed(boolean isEmpty) once a 
* change has been made to the ImageView
* 
* @author Maurycy Wojtowicz
*
*/
public class ScaleImageView extends ImageView {
	
	public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
         Drawable d = getDrawable();

         if(d!=null){
                 // ceil not round - avoid thin vertical gaps along the left/right edges
                 int width = MeasureSpec.getSize(widthMeasureSpec);
                 int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
                 setMeasuredDimension(width, height);
         }else{
                 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         }
    }

}