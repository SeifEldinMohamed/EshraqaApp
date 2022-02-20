package com.seif.eshraqaapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.databinding.FragmentAzkarBinding
import com.seif.eshraqaapp.ui.fragments.AzkarFragmentArgs.fromBundle


class AzkarFragment : Fragment() {
    private lateinit var binding: FragmentAzkarBinding
    private var score = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAzkarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = fromBundle(requireArguments()).azkar.date

        val numberOfAzkar = fromBundle(requireArguments()).azkar.azkar.size
        binding.textDay.text = fromBundle(requireArguments()).azkar.day
        binding.textScore.text = "${fromBundle(requireArguments()).azkar.score}/$numberOfAzkar"

        binding.sabahCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.masaaCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.sleepCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.afterPrayerCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdHamdCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdTkberCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdTsbe7Check.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdEstegpharCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                score++
            else
                score--
            binding.textScore.text = "$score/$numberOfAzkar"
        }
    }
}