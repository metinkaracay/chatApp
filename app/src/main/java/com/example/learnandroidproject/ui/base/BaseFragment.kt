package com.example.learnandroidproject.ui.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.tryOrLog
import com.example.learnandroidproject.common.util.KeyboardUtil
import com.example.learnandroidproject.ui.common.navigation.NavigationData
import io.reactivex.rxjava3.disposables.CompositeDisposable


abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    lateinit var binding: DB

    open val compositeDisposable by lazy(LazyThreadSafetyMode.NONE) {
        CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), getLayoutResId(), container, false)
        return binding.root
    }

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    open fun navigateToDestination(navController: NavController, navigationData: NavigationData) {
        tryOrLog {
            navController.navigate(
                navigationData.destinationId,
                navigationData.args,
                buildNavOptions(
                    enterAnim = navigationData.navigationAnimator?.enterAnim ?: R.anim.slide_in_left_anim,
                    exitAnim = navigationData.navigationAnimator?.exitAnim ?: R.anim.slide_out_left_anim,
                    popEnterAnim = navigationData.navigationAnimator?.popEnterAnim ?: R.anim.slide_in_right_anim,
                    popExitAnim = navigationData.navigationAnimator?.popExitAnim ?: R.anim.slide_out_right_anim,
                    popupTo = navigationData.popupTo
                )
            )
        }
    }

    open fun buildNavOptions(
        @AnimRes @AnimatorRes enterAnim: Int? = null,
        @AnimRes @AnimatorRes exitAnim: Int? = null,
        @AnimRes @AnimatorRes popEnterAnim: Int? = null,
        @AnimRes @AnimatorRes popExitAnim: Int? = null,
        @IdRes popupTo: Int? = null
    ): NavOptions {
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(enterAnim ?: androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(exitAnim ?: androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(popEnterAnim ?: androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(popExitAnim ?: androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        popupTo?.let { builder.setPopUpTo(it, false) }
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    open fun hideKeyboard() {
        KeyboardUtil.hideSoftKeyboard(requireActivity())
    }

    open fun showKeyboard(view: View) {
        KeyboardUtil.showSoftKeyboard(view)
    }

    open fun hideSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    open fun showSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
    }
}