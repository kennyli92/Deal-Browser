package com.target.dealbrowserpoc.extensions

import android.app.Application
import androidx.fragment.app.Fragment

fun Fragment.application(): Application? = this.activity?.application