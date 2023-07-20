package com.example.learnandroidproject.ui.common.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import com.example.learnandroidproject.ui.common.navigation.NavigationAnimator

data class NavigationData(
    @IdRes val destinationId: Int,
    @IdRes val popupTo: Int? = null,
    val popupToInclusive: Boolean = false,
    val args: Bundle? = null,
    val navigationAnimator: NavigationAnimator? = null
)