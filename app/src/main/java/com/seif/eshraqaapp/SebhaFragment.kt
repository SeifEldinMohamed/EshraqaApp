package com.seif.eshraqaapp

import android.app.Dialog
import android.media.MediaParser
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.databinding.FragmentSebhaBinding
import com.seif.eshraqaapp.ui.fragments.AzkarFragmentArgs


class SebhaFragment : Fragment() {
lateinit var binding: FragmentSebhaBinding
private var score:Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSebhaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clickSoundEffect = MediaPlayer.create(requireContext(), R.raw.sebha_click_sound_effect)
        val cutSoundEffect = MediaPlayer.create(requireContext(), R.raw.sebha_cut_sound_effect)

        binding.txtCounter.text = AppSharedPref.readSebhaScore("sebha_score", 0).toString()
        score = AppSharedPref.readSebhaScore("sebha_score", 0)

        when(AppSharedPref.readSebhaMessage("message", 8)){
            1-> binding.txtSebhaMessage.text = getString(R.string.tsbeh1)
            2-> binding.txtSebhaMessage.text = getString(R.string.tsbeh2)
            3-> binding.txtSebhaMessage.text = getString(R.string.tsbeh3)
            4-> binding.txtSebhaMessage.text = getString(R.string.tsbeh4)
            5-> binding.txtSebhaMessage.text = getString(R.string.tsbeh5)
            6-> binding.txtSebhaMessage.text = getString(R.string.tsbeh6)
            7-> binding.txtSebhaMessage.text = getString(R.string.tsbeh7)
            8-> binding.txtSebhaMessage.text = getString(R.string.tsbeh8)
        }

        if (AppSharedPref.readSoundChoice("sound", true))
            binding.btnSound.setImageResource(R.drawable.ic_sound)
        else
            binding.btnSound.setImageResource(R.drawable.ic_sound_mute)

        binding.btnSebha.setOnClickListener {
            score++
            if(AppSharedPref.readSoundChoice("sound", true)){
                if(score == 33L ||score == 100L){
                    cutSoundEffect.start()
                    Toast.makeText(requireContext(), "اللهم تقبل ❤", Toast.LENGTH_SHORT).show()
                }
                else{
                    clickSoundEffect.start()
                }
            }
            binding.txtCounter.text = score.toString()
        }
        binding.btnReset.setOnClickListener {
            score = 0
            binding.txtCounter.text = score.toString()
        }
        binding.btnSound.setOnClickListener {
            if(AppSharedPref.readSoundChoice("sound", true)){
                binding.btnSound.setImageResource(R.drawable.ic_sound_mute)
                AppSharedPref.writeSoundChoice("sound", false)
            }
            else{
                binding.btnSound.setImageResource(R.drawable.ic_sound)
                AppSharedPref.writeSoundChoice("sound", true)
            }
        }
        binding.btnSehaMessage.setOnClickListener {
            showSebhaMessagesDialog()
        }
    }

    private fun showSebhaMessagesDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sebha_dialog)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save_tsbeh)
        val btnBack = dialog.findViewById<TextView>(R.id.btn_back_tsbeh)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup_tsbeh)

        when(AppSharedPref.readSebhaMessage("message", 8)){
            1 -> radioGroup.check(R.id.tsbeh1_radio)
            2 -> radioGroup.check(R.id.tsbeh2_radio)
            3 -> radioGroup.check(R.id.tsbeh3_radio)
            4 -> radioGroup.check(R.id.tsbeh4_radio)
            5 -> radioGroup.check(R.id.tsbeh5_radio)
            6 -> radioGroup.check(R.id.tsbeh6_radio)
            7 -> radioGroup.check(R.id.tsbeh7_radio)
            8 -> radioGroup.check(R.id.tsbeh8_radio)
        }

        btnSave.setOnClickListener {
            when(radioGroup.checkedRadioButtonId){
                R.id.tsbeh1_radio -> AppSharedPref.writeSebhaMessage("message", 1)
                R.id.tsbeh2_radio -> AppSharedPref.writeSebhaMessage("message", 2)
                R.id.tsbeh3_radio -> AppSharedPref.writeSebhaMessage("message", 3)
                R.id.tsbeh4_radio -> AppSharedPref.writeSebhaMessage("message", 4)
                R.id.tsbeh5_radio -> AppSharedPref.writeSebhaMessage("message", 5)
                R.id.tsbeh6_radio -> AppSharedPref.writeSebhaMessage("message", 6)
                R.id.tsbeh7_radio -> AppSharedPref.writeSebhaMessage("message", 7)
                R.id.tsbeh8_radio -> AppSharedPref.writeSebhaMessage("message", 8)
            }

            when(AppSharedPref.readSebhaMessage("message", 8)){
                1-> binding.txtSebhaMessage.text = getString(R.string.tsbeh1)
                2-> binding.txtSebhaMessage.text = getString(R.string.tsbeh2)
                3-> binding.txtSebhaMessage.text = getString(R.string.tsbeh3)
                4-> binding.txtSebhaMessage.text = getString(R.string.tsbeh4)
                5-> binding.txtSebhaMessage.text = getString(R.string.tsbeh5)
                6-> binding.txtSebhaMessage.text = getString(R.string.tsbeh6)
                7-> binding.txtSebhaMessage.text = getString(R.string.tsbeh7)
                8-> binding.txtSebhaMessage.text = getString(R.string.tsbeh8)
            }

            dialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppSharedPref.writeSebhaScore("sebha_score", score)
    }
}