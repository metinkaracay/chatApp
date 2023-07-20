package com.example.learnandroidproject.ui.welcome.testImageFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentTestImageBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class TestImageFragment : BaseFragment<FragmentTestImageBinding>() {

    private val viewModel: TestImageViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_test_image

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel){
            testImagePageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }


        binding.button.setOnClickListener {
            checkStoragePermission()
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
            val selectedImage = data.data
            var secilenBitmap : Bitmap? = null

            if(selectedImage != null){

                if(Build.VERSION.SDK_INT >= 28){

                    val source = ImageDecoder.createSource(requireContext().contentResolver,selectedImage!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    binding.imageView.setImageBitmap(secilenBitmap)
                    viewModel.postImage(selectedImage!!, requireContext())

                }else {

                    secilenBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImage)
                    binding.imageView.setImageBitmap(secilenBitmap)
                    viewModel.postImage(selectedImage!!, requireContext())

                }

            }

            //chattingViewModel.uploadCloudStorage("image",requireContext(),selectedImage)
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
}