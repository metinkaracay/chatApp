package com.example.learnandroidproject.ui.welcome.videoViewTest

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.MediaController
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentVideoViewBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import java.io.File

class VideoViewFragment : BaseFragment<FragmentVideoViewBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_video_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.video)*/

        //val videoUri = Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "road_video"))

        /*val videoResourceID = resources.getIdentifier("green_car", "raw", requireContext().packageName)
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")
        /*val videoPath = File(Environment.getExternalStorageDirectory(), "new_car").absolutePath
        val uri = Uri.parse(videoPath)*/

        Log.e("videouri","$videoUri")*/
        /*binding.video.setMediaController(mediaController)
        binding.video.setVideoURI(videoUri)

        binding.video.requestFocus()
        binding.video.start()

        binding.video.setOnPreparedListener { it.isLooping = true }*/

        /*val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.road)*/

        val videoResourceID = resources.getIdentifier("road", "raw", requireContext().packageName)
        val roadUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")

        Log.e("videouri","$roadUri")
        //binding.road.setMediaController(mediaController)

        binding.road.setVideoURI(roadUri)

        binding.road.requestFocus()
        binding.road.start()

        binding.road.setOnPreparedListener { it.isLooping = true }


        val videoResourceID2 = resources.getIdentifier("green_car", "raw", requireContext().packageName)
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID2")

        playVideo(videoUri)
        playVideo2(videoUri)
        playVideo3(videoUri)

    }

    fun playVideo(videoUri: Uri){
        try {
            binding.video.setVideoFromUri(requireContext(), videoUri)
            binding.video.setLooping(true)
        } catch (e: Exception) {
            Log.e("AgentPartyCallFragment", "playSuperAnimation: ${e.localizedMessage}")
        }
    }

    fun playVideo2(videoUri: Uri){
        try {
            binding.video2.setVideoFromUri(requireContext(), videoUri)
            binding.video2.setLooping(true)
        } catch (e: Exception) {
            Log.e("AgentPartyCallFragment", "playSuperAnimation: ${e.localizedMessage}")
        }
    }

    fun playVideo3(videoUri: Uri){
        try {
            binding.video3.setVideoFromUri(requireContext(), videoUri)
            binding.video3.setLooping(true)
        } catch (e: Exception) {
            Log.e("AgentPartyCallFragment", "playSuperAnimation: ${e.localizedMessage}")
        }
    }
}