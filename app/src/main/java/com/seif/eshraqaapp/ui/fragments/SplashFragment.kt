package com.seif.eshraqaapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.SharedPref


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPref.init(requireContext())
        val splashTime = 1200L
        Handler(Looper.getMainLooper()).postDelayed({
            val signed: Boolean = SharedPref.readSignedUp("signed", false)
            if (!signed) {
                view.findNavController().navigate(R.id.action_splashFragment_to_introFragment)
            } else {
                view.findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
                requireActivity().finish()
            }
        }, splashTime)
    }
}