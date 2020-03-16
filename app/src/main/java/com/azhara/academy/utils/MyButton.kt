package com.azhara.academy.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.azhara.academy.R

class MyButton: AppCompatButton {

    private var enabledBackground: Drawable? = null
    private var disableBackground: Drawable? = null
    private var textColour: Int = 0

    constructor(context: Context): super(context){
        init()
    }


    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int): super(context, attrs, defStyleAttrs){
        init()
    }

    private fun init() {
        val resource = resources
        enabledBackground = resource.getDrawable(R.drawable.bg_button)
        disableBackground = resource.getDrawable(R.drawable.bg_button_disable)
        textColour = ContextCompat.getColor(context, android.R.color.background_light)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disableBackground
        setTextColor(textColour)
        textSize = 12f
        gravity = CENTER
    }

}