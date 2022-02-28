package com.seif.eshraqaapp.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.databinding.FragmentQuranBinding
import com.seif.eshraqaapp.viewmodels.QuranViewModel
import com.seif.eshraqaapp.ui.fragments.QuranFragmentArgs.fromBundle



class QuranFragment : Fragment() {
lateinit var binding: FragmentQuranBinding
    private lateinit var quranViewModel: QuranViewModel
    private var score = 0
    private var quranHashMap = HashMap<String, Boolean>()
    private lateinit var pref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quranViewModel = ViewModelProvider(requireActivity())[QuranViewModel::class.java]

        val dayDate = "${fromBundle(requireArguments()).quran.currentDay} / " +
                "${fromBundle(requireArguments()).quran.currentMonth} / " +
                "${fromBundle(requireArguments()).quran.currentYear}"
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = dayDate


        score = fromBundle(requireArguments()).quran.score
        // copy values to azkarHashMap so i can use in update azkar
        quranHashMap["question1"] = fromBundle(requireArguments()).quran.quran["question1"] ?: false
        quranHashMap["question2"] = fromBundle(requireArguments()).quran.quran["question2"] ?: false
        quranHashMap["question3"] = fromBundle(requireArguments()).quran.quran["question3"] ?: false

        // set Menu
        setHasOptionsMenu(true)
        // show text day and score from coming data
        val numberOfQuran = fromBundle(requireArguments()).quran.quran.size
        binding.textDay.text = fromBundle(requireArguments()).quran.dayName
        binding.textScore.text = "${fromBundle(requireArguments()).quran.score}/$numberOfQuran"


        if(!quranViewModel.readIsFirstTimeToEnter(requireContext())){
            when(quranHashMap["question1"]){
                true -> binding.radioSaveYes.isChecked = true
                false -> binding.radioSaveNo.isChecked = true
                else -> Log.d("quran", "error in question 1")
            }
            when(quranHashMap["question2"]){
                true -> binding.radioReadYes.isChecked = true
                false -> binding.radioReadNo.isChecked = true
                else -> Log.d("quran", "error in question 2")
            }
            when(quranHashMap["question3"]){
                true -> binding.radioRevisionYes.isChecked = true
                false -> binding.radioRevisionNo.isChecked = true
                else -> Log.d("quran", "error in question 3")
            }
        }

        binding.radioSaveYes.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                quranHashMap["question1"] = true
            }
            else{
                score--
                quranHashMap["question1"] = false
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
        binding.radioReadYes.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                quranHashMap["question2"] = true
            }
            else{
                score--
                quranHashMap["question2"] = false
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
        binding.radioRevisionYes.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                quranHashMap["question3"] = true
            }
            else{
                score--
                quranHashMap["question3"] = false
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
//        binding.afterPrayerCheck.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked){
//                score++
//                quranHashMap["أذكار بعد الصلاة"] = true
//            }
//            else{
//                score--
//                quranHashMap["أذكار بعد الصلاة"] = false
//            }
//            binding.textScore.text = "$score/$numberOfQuran"
//        }
//        binding.wrdHamdCheck.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked){
//                score++
//                quranHashMap["ورد حمد"] = true
//            }
//            else{
//                score--
//                quranHashMap["ورد حمد"] = false
//            }
//            binding.textScore.text = "$score/$numberOfQuran"
//        }
//        binding.wrdTkberCheck.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked){
//                score++
//                quranHashMap["ورد تكبير"] = true
//            }
//            else{
//                score--
//                quranHashMap["ورد تكبير"] = false
//            }
//            binding.textScore.text = "$score/$numberOfQuran"
//        }
//        binding.wrdTsbe7Check.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked){
//                score++
//                quranHashMap["ورد تسبيح"] = true
//            }
//            else{
//                score--
//                quranHashMap["ورد تسبيح"] = false
//            }
//            binding.textScore.text = "$score/$numberOfQuran"
//        }
//        binding.wrdEstegpharCheck.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked){
//                score++
//                quranHashMap["ورد استغفار"] = true
//            }
//            else{
//                score--
//                quranHashMap["ورد استغفار"] = false
//            }
//            binding.textScore.text = "$score/$numberOfQuran"
//        }

        binding.quranImageZahra.setOnClickListener {
            showEshrakaMessageDialog()
        }

    }

    private fun showEshrakaMessageDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.zahra_message_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_message)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        txtMessage.text = fromBundle(requireArguments()).quran.weeklyUserMessage
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.azkar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save ->{
                initializeQuranData()
                updateQuranItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeQuranData() {
        if(quranViewModel.readIsFirstTimeToEnter(requireContext())){
           quranViewModel.writeIsFirstTimeToEnter()
        }
        }

    private fun updateQuranItem() {
        val quran = Quran(
            fromBundle(requireArguments()).quran.id,
            quranHashMap,
            fromBundle(requireArguments()).quran.currentDay,
            fromBundle(requireArguments()).quran.currentMonth,
            fromBundle(requireArguments()).quran.currentYear,
            fromBundle(requireArguments()).quran.dayName,
            score,
            fromBundle(requireArguments()).quran.numberOfDaysToWork,
            fromBundle(requireArguments()).quran.weeklyUserMessage
        )
        quranViewModel.updateQuran(quran)
        findNavController().navigate(R.id.action_quranFragment_to_quranDaysFragment)
    }

}