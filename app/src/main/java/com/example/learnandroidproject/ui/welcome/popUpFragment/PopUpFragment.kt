package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentPopUpBinding
import com.example.learnandroidproject.ui.base.BaseDialogFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopUpFragment : BaseDialogFragment<FragmentPopUpBinding>() {

    private val viewModel: PopUpViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    private var callBackListener: ((String) -> Unit)? = null
    var startY: Float? = null

    fun setCallBackListener(listener: (String) -> Unit) {
        callBackListener = listener
    }

    var selectedImage: Uri? = null

    override fun getLayoutResId(): Int = R.layout.fragment_pop_up

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.Animation_Design_BottomSheetDialog

        with(viewModel){
        popupPageViewStateLiveData.observeNonNull(viewLifecycleOwner) {
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
            popUpCountDownTimer.observeNonNull(viewLifecycleOwner) { remainingTime ->
                if (remainingTime == 0) {
                    dismiss()
                }
            }
            uploadResponse.observe(viewLifecycleOwner){
                callBackListener?.invoke(it)
                dismiss()
            }
        }
        val selectedPhoto = welcomeViewModel.getClickedUserPhoto()
        val requestCode = arguments?.getInt(ARG_LINK)
        viewModel.decisionToFun(requestCode!!,selectedPhoto)
        handleViewOption(requestCode)
        binding.baseLinear.setOnTouchListener(onTouchListener)
    }

    fun handleViewOption(requestCode: Int){
        if (requestCode == 2){
            binding.userPhoto.setOnClickListener {
                checkStoragePermission()
            }
        }
        binding.saveButton.setOnClickListener {
            if (selectedImage != null){
                viewModel.postImage(selectedImage!!, requireContext())
            }else{
                Toast.makeText(requireContext(), "Fotoğraf Seçmediniz!",Toast.LENGTH_SHORT).show()
            }
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.logoutAcceptButton.setOnClickListener {
            dismiss()
        }
        binding.logoutAcceptButton.setOnClickListener {
            dismiss()
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //Daha önceden izin verilmiş
            startGalleryIntent()
        }
    }

    private fun startGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
            var selectedBitmap : Bitmap? = null

            if(selectedImage != null){

                if(Build.VERSION.SDK_INT >= 28){

                    val source = ImageDecoder.createSource(requireContext().contentResolver,selectedImage!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    binding.selectedUserImage.setImageBitmap(selectedBitmap)

                }else {

                    selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImage)
                    binding.selectedUserImage.setImageBitmap(selectedBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { View, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Parmağın dokunduğu anı takip etmek için başlangıç noktasını kaydet
                startY = event.rawY
            }
            MotionEvent.ACTION_UP -> {
                // Parmağın kalktığı anı takip etmek için bitiş noktasını kaydet
                val endY = event.rawY
                val distance = endY - startY!!

                // Parmağın aşağı doğru sürüklendiği yeterli mesafeye ulaşmışsa fragment'ı kapat
                if (distance > 300) {
                    dismiss()
                }
            }
        }
        true
    }
    companion object {
        private const val ARG_LINK = "requestCode"

        private var showing = false
        fun isShowing() = showing
        fun newInstance(requestCode: Int) = PopUpFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_LINK, requestCode)
            }
        }
    }
}