package com.seif.eshraqh.ui.fragments

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.seif.eshraqh.R
import com.seif.eshraqh.data.sharedPreference.IntroSharedPref
import com.seif.eshraqh.databinding.FragmentSignUpBinding
import java.util.*


class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view1: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view1, savedInstanceState)

        val calender = Calendar.getInstance()
        val dataPicker: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, day)
                updateLabel(calender)
            }
        binding.etBirthdate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dataPicker,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.btnLogin.setOnClickListener {
            if (binding.etUsername.text!!.isEmpty()) {
                Toast.makeText(requireContext(), "أدخل اسمك !", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (binding.etBirthdate.text!!.isEmpty()) {
                Toast.makeText(requireContext(), "أدخل تاريخ ميلادك !", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (!binding.rbMale.isChecked && !binding.rbFemale.isChecked) {
                Toast.makeText(requireContext(), "أدخل نوع جنسك !", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            IntroSharedPref.writeSignedUp("signed", true)
            if (binding.rbMale.isChecked) {
                IntroSharedPref.writeGander("Male", true)
            } else if (binding.rbFemale.isChecked) {
                IntroSharedPref.writeGander("Female", true)
            }
            IntroSharedPref.writePersonalInfo("Username", binding.etUsername.text.toString())
            findNavController().navigate(R.id.action_signUpFragment_to_mainActivity)
            requireActivity().finish()
        }
    }

    private fun updateLabel(calender: Calendar) {
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        binding.etBirthdate.setText(sdf.format(calender.time))
    }
}