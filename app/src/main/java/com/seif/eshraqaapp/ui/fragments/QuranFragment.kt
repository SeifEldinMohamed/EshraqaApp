package com.seif.eshraqaapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.viewmodels.QuranViewModel
import com.seif.eshraqaapp.ui.fragments.QuranFragmentArgs.fromBundle
import com.seif.eshraqaapp.databinding.FragmentQuranBinding


class QuranFragment : Fragment() {
lateinit var binding: FragmentQuranBinding
    private lateinit var quranViewModel: QuranViewModel
    private var score = 0
    private var quranHashMap = HashMap<String, String>()
    private lateinit var pref: SharedPreferences
    private var isSaveCounterNotFoundUsed = false
    private var isReadCounterNotFoundUsed = false
    private var isRevisionCounterNotFoundUsed = false
    private var isVacation = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quranViewModel = ViewModelProvider(requireActivity())[QuranViewModel::class.java]

        // show vacation feature for girls only and the character according to gender
        if(IntroSharedPref.readGander("Male", false)){
            binding.quranImageZahra.visibility = View.GONE
            binding.quranImageGheth.visibility = View.VISIBLE
            binding.checkVacation.visibility = View.GONE
        }
        else{
            binding.quranImageZahra.visibility = View.VISIBLE
            binding.quranImageGheth.visibility = View.GONE
            binding.checkVacation.visibility = View.VISIBLE

        }

        val dayDate = fromBundle(requireArguments()).quran.date

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = dayDate

        score = fromBundle(requireArguments()).quran.score
        // copy values to azkarHashMap so i can use in update azkar
        quranHashMap["question1"] = fromBundle(requireArguments()).quran.quran["question1"].toString()
        quranHashMap["question2"] = fromBundle(requireArguments()).quran.quran["question2"].toString()
        quranHashMap["question3"] = fromBundle(requireArguments()).quran.quran["question3"].toString()

        // set Menu
        setHasOptionsMenu(true)

        // vacation
        isVacation = fromBundle(requireArguments()).quran.isVacation
        binding.checkVacation.isChecked = isVacation

        binding.checkVacation.setOnCheckedChangeListener { buttonView, isChecked ->
            isVacation = isChecked
        }


        // show text day and score from coming data
        val numberOfQuran = fromBundle(requireArguments()).quran.quran.size
        binding.textDay.text = fromBundle(requireArguments()).quran.dayName
        binding.textScore.text = "${fromBundle(requireArguments()).quran.score}/$numberOfQuran"

        AppSharedPref.init(requireContext())
        Log.d("saveCounter",  fromBundle(requireArguments()).quran.numberOfSaveDaysToWork.toString())
        val saveCounter = 7 - fromBundle(requireArguments()).quran.numberOfSaveDaysToWork
        Log.d("saveCounter", AppSharedPref.readSaveCounter("saveCounter",saveCounter).toString())

        if (AppSharedPref.readSaveCounter("saveCounter",saveCounter) <= 0){
            binding.radioNotFoundSave.visibility = View.GONE
        }
        else{
            binding.radioNotFoundSave.visibility = View.VISIBLE
        }

        val readCounter = 7 - fromBundle(requireArguments()).quran.numberOfReadDaysToWork
        Log.d("readCounter", readCounter.toString())
        if (AppSharedPref.readReadCounter("readCounter", readCounter) <= 0){
            binding.radioNotFoundRead.visibility = View.GONE
        }else{
            binding.radioNotFoundRead.visibility = View.VISIBLE
        }

        val revisionCounter = 7 - fromBundle(requireArguments()).quran.numberOfRevisionDaysToWork
        Log.d("revisionCounter", revisionCounter.toString())
        if (AppSharedPref.readRevisionCounter("revisionCounter", revisionCounter) <= 0){
            binding.radioNotFoundRevision.visibility = View.GONE
        }else{
            binding.radioNotFoundRevision.visibility = View.VISIBLE
        }

            when(quranHashMap["question1"]){
                "yes" -> binding.radioSave.isChecked = true
                "notFound" -> binding.radioNotFoundSave.isChecked = true
                else -> Log.d("quran", "firstTime1")
            }
            when(quranHashMap["question2"]){
                "yes" -> binding.radioRead.isChecked = true
                "notFound" -> binding.radioNotFoundRead.isChecked = true
                else -> Log.d("quran", "firstTime2")
            }
            when(quranHashMap["question3"]){
                "yes" -> binding.radioRevision.isChecked = true
                "notFound" -> binding.radioNotFoundRevision.isChecked = true
                else -> Log.d("quran", "firstTime3")
            }


        binding.radioSave.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                quranHashMap["question1"] = "yes"
            }
            else{
                score--
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
        binding.radioNotFoundSave.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                quranHashMap["question1"] = "notFound"
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }

        binding.radioRead.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                quranHashMap["question2"] = "yes"
            }
            else{
                score--
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
        binding.radioNotFoundRead.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                quranHashMap["question2"] = "notFound"
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
        binding.radioRevision.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                quranHashMap["question3"] = "yes"
            }
            else{
                score--
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }
        binding.radioNotFoundRevision.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                quranHashMap["question3"] = "notFound"
            }
            binding.textScore.text = "$score/$numberOfQuran"
        }

        binding.quranImageGheth.setOnClickListener {
            showEshrakaMessageDialog()
        }

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
        val frameImage = dialog.findViewById<ImageView>(R.id.img_frame_message)
        val characterImage = dialog.findViewById<ImageView>(R.id.characterImage)

        if(IntroSharedPref.readGander("Male", false)){
            frameImage.setImageResource(R.drawable.gheth_frame_dialog)
            characterImage.setImageResource(R.drawable.gheth_normal)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
        }
        else{ // female
            frameImage.setImageResource(R.drawable.zahra_frame_dialog)
            characterImage.setImageResource(R.drawable.zahra_normal)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
        }

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
                updateCounters()
                updateQuranItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateCounters() {
        isSaveCounterNotFoundUsed = fromBundle(requireArguments()).quran.isSaveCounterNotFoundUsed
        isReadCounterNotFoundUsed = fromBundle(requireArguments()).quran.isReadCounterNotFoundUsed
        isRevisionCounterNotFoundUsed = fromBundle(requireArguments()).quran.isRevisionCounterNotFoundUsed

        // i add this another check to handle if user click save for more than one time to avoid each time not found is counting
        if(quranHashMap["question1"] == "notFound" && !isSaveCounterNotFoundUsed){
            isSaveCounterNotFoundUsed = true
            val saveCounter:Int = 7 - fromBundle(requireArguments()).quran.numberOfSaveDaysToWork
            var currentSaveCounter =  AppSharedPref.readSaveCounter("saveCounter",saveCounter)
            Log.d("saveCounter", currentSaveCounter.toString())
            currentSaveCounter--
            AppSharedPref.updateSaveCounter("saveCounter", currentSaveCounter)
        }
        else if(fromBundle(requireArguments()).quran.isSaveCounterNotFoundUsed &&
            quranHashMap["question1"] == "yes"){
                isSaveCounterNotFoundUsed = false
            val saveCounter:Int = 7 - fromBundle(requireArguments()).quran.numberOfSaveDaysToWork
            var currentSaveCounter =  AppSharedPref.readSaveCounter("saveCounter",saveCounter)
            if (currentSaveCounter != saveCounter){
                currentSaveCounter++
                AppSharedPref.updateSaveCounter("saveCounter", currentSaveCounter)
            }
        }
        if(quranHashMap["question2"] == "notFound" && !isReadCounterNotFoundUsed){
            isReadCounterNotFoundUsed = true
            val readCounter:Int = 7 - fromBundle(requireArguments()).quran.numberOfReadDaysToWork
            var currentReadCounter =  AppSharedPref.readReadCounter("readCounter", readCounter)
            Log.d("readCounter", currentReadCounter.toString())
            currentReadCounter--
            AppSharedPref.updateReadCounter("readCounter", currentReadCounter)
        }
        else if(fromBundle(requireArguments()).quran.isReadCounterNotFoundUsed &&
            quranHashMap["question2"] == "yes"){
            isReadCounterNotFoundUsed = false
            val readCounter:Int = 7 - fromBundle(requireArguments()).quran.numberOfReadDaysToWork
            var currentReadCounter =  AppSharedPref.readReadCounter("readCounter", readCounter)
            if (currentReadCounter != readCounter){
                currentReadCounter++
                AppSharedPref.updateReadCounter("readCounter", currentReadCounter)
            }
        }

        if(quranHashMap["question3"] == "notFound" && !isRevisionCounterNotFoundUsed){
            isRevisionCounterNotFoundUsed = true
            val revisionCounter:Int = 7 - fromBundle(requireArguments()).quran.numberOfRevisionDaysToWork
            var currentRevisionCounter =  AppSharedPref.readRevisionCounter("revisionCounter", revisionCounter)
            Log.d("revisionCounter", currentRevisionCounter.toString())
            currentRevisionCounter--
            AppSharedPref.updateRevisionCounter("revisionCounter", currentRevisionCounter)
        }
        else if(fromBundle(requireArguments()).quran.isRevisionCounterNotFoundUsed &&
            quranHashMap["question3"] == "yes"){
            isRevisionCounterNotFoundUsed = false
            val revisionCounter:Int = 7 - fromBundle(requireArguments()).quran.numberOfRevisionDaysToWork
            var currentRevisionCounter =  AppSharedPref.readReadCounter("revisionCounter", revisionCounter)
            if (currentRevisionCounter != revisionCounter){
                currentRevisionCounter++
                AppSharedPref.updateRevisionCounter("revisionCounter", currentRevisionCounter)
            }
        }
    }

    private fun updateQuranItem() {
        Log.d("quran", "update $quranHashMap")
        val quran = Quran(
            fromBundle(requireArguments()).quran.id,
            quranHashMap,
            fromBundle(requireArguments()).quran.date,
            fromBundle(requireArguments()).quran.dayName,
            fromBundle(requireArguments()).quran.mCalendar,
            score,
            fromBundle(requireArguments()).quran.weeklyUserMessage,
            false,
            fromBundle(requireArguments()).quran.numberOfSaveDaysToWork,
            fromBundle(requireArguments()).quran.numberOfReadDaysToWork,
            fromBundle(requireArguments()).quran.numberOfRevisionDaysToWork,
            isVacation,
            isSaveCounterNotFoundUsed,
            isReadCounterNotFoundUsed,
            isRevisionCounterNotFoundUsed
            )
        quranViewModel.updateQuran(quran)
        findNavController().navigate(R.id.action_quranFragment_to_quranDaysFragment)
    }
}