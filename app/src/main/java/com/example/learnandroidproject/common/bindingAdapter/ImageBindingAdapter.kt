package com.example.learnandroidproject.common.bindingAdapter

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.learnandroidproject.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import jp.wasabeef.glide.transformations.BlurTransformation

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageViewResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImageWithUrl(view: ImageView, url: String?) {
        url?.let { Glide.with(view.context).load(it).into(view) }
    }

    @JvmStatic
    @BindingAdapter("profileImageUrl")
    fun loadProfileImageWithUrl(view: ImageView, url: String?) {
        url?.let { Glide.with(view.context).load(it).into(view) }
    }

    @JvmStatic
    @BindingAdapter("imageUri")
    fun loadImageWithUri(imageView: ImageView, uri: Uri?) {
        uri?.let {
            imageView.setImageURI(it)
        }
    }

    @JvmStatic
    @BindingAdapter("constraintDimensionRatio")
    fun setConstraintDimensionRatio(imageView: ImageView, ratio: String?) {
        imageView.updateLayoutParams<ConstraintLayout.LayoutParams> {
            dimensionRatio = ratio
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["url", "isBlurred"], requireAll = false)
    fun loadProfileImageWithBlur(view: ImageView?, imageUrl: String?, isBlurred: Boolean) {
        imageUrl?.let {
            if (isBlurred) {
                try {
                    view?.let { Glide.with(view.context).load(it)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 2))).into(view) }
                } catch (e: Exception) {
                    Log.e("exception_worked", "${e.message}")
                }
            } else {
                try {
                    view?.let { Glide.with(view.context).load(it).into(view) }
                } catch (e: Exception) {
                    Log.e("exception_worked", "${e.message}")
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["url", "gender", "isBlurred"], requireAll = false)
    fun loadProfileImageWithGender(view: ImageView?, imageUrl: String?, gender: Int, isBlurred: Boolean) {
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            if (isBlurred) {
                try {
                    view?.let { Glide.with(view.context).load(imageUrl)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 2))).into(view) }
                } catch (e: Exception) {
                    Log.e("exception_worked", "${e.message}")
                }

            } else {
                try {
                    view?.let { Glide.with(view.context).load(imageUrl).into(view) }
                } catch (e: Exception) {
                    Log.e("exception_worked", "${e.message}")
                }
            }
        } else {
            if (gender == 0) {
                view?.setImageResource(R.drawable.img_default_man)
            } else {
                view?.setImageResource(R.drawable.img_default_woman)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("customStrokeWidth")
    fun setCustomStrokeWidth(view: ShapeableImageView, @DimenRes resource: Int) {
        view.strokeWidth = view.context.resources.getDimension(resource)
    }

    @JvmStatic
    @BindingAdapter("customStrokeWidth")
    fun setCustomStrokeWidth(view: MaterialCardView, @DimenRes resource: Int) {
        view.strokeWidth = view.context.resources.getDimensionPixelSize(resource)
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "imageUri", "isUri"], requireAll = false)
    fun loadProfileImageUrlOrUri(imageView: ImageView?, imageUrl: String?, imageUri: Uri?, isUri: Boolean) {
        if (isUri) {
            imageUri?.let { imageView?.setImageURI(it) }
        } else {
            if (imageUrl.isNullOrEmpty().not()) {
                if (imageUrl == "null"){
                    imageView?.setImageResource(R.drawable.avatar)
                }else{
                    try {
                        imageView?.let { Glide.with(imageView.context).load(imageUrl).into(it) }
                    } catch (e: Exception) {
                        Log.e("exception_worked", "${e.message}")
                    }
                }
            }
        }
    }
}