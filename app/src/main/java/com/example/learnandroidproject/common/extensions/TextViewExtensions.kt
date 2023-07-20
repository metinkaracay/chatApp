package com.example.learnandroidproject.common.extensions

import android.graphics.Paint
import android.widget.TextView

fun TextView.underline(){
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}