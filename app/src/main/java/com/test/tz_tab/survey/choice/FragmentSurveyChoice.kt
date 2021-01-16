package com.test.tz_tab.survey.choice

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.tz_tab.MainActivity
import com.test.tz_tab.R
import com.test.tz_tab.enums.SurveyType
import com.test.tz_tab.models.SurveyChoices
import com.test.tz_tab.models.SurveyItem
import com.test.tz_tab.survey.intro.FragmentSurveyIntro
import com.test.tz_tab.survey.money.FragmentSurveyMoney
import com.test.tz_tab.utils.Logger
import kotlinx.android.synthetic.main.fragment_survey_list_selection.*

class FragmentSurveyChoice: Fragment(R.layout.fragment_survey_list_selection), ChoiceCallback {

    private var choosenItem: SurveyItem? = null
    private val choices: SurveyChoices? by lazy { arguments?.getSerializable("choices") as? SurveyChoices }
    private  val argSurveyType: String by lazy { arguments?.getString("type") ?: "" }
    // Gets text from arguments or some default text
    private val titleText: String by lazy { arguments?.getString("title") ?: "Welcome to the survey." }
    private val subtitleText: String by lazy { arguments?.getString("subtitle") ?: "We only have two questions and we need less than 1 minute of your time." }


    private var nextStageRunnable: Runnable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets text from arguments or some default text
        val titleText = arguments?.getString("titleChoice") ?: "Welcome to the survey."
        tv_list_title.text = titleText

        choices?.let {
            rec_survey_list.layoutManager = LinearLayoutManager(this.activity)
            rec_survey_list.adapter = ChoiceAdapter(it, choosenItem,this)
        }
    }

    override fun choose(item: SurveyItem) {
        if (choosenItem != item) {
            choosenItem?.let {
                val index = choices?.list?.indexOf(it)
                index?.let { pos ->
                    val adapter = rec_survey_list.adapter as? ChoiceAdapter
                    adapter?.choosenItem = item
                    adapter?.notifyItemChanged(pos)
                }
            }
            choosenItem = item
        }
        // Go next
        if (nextStageRunnable == null){
            nextStageRunnable = Runnable {
                openNextStage()
                nextStageRunnable = null
            }.also { Handler(Looper.getMainLooper()).postDelayed(it, 600) }

        }

    }

    private fun openNextStage(){
        Logger.log("Opened")

        val fragment =
            FragmentSurveyMoney()
        fragment.arguments = Bundle().apply {
            putString("titleMoney", "What total did you pay for your electricity last week?")
            putString("title", titleText)
            putString("subtitle", subtitleText)
            putString("type", argSurveyType)
        }
        (activity as? MainActivity)?.replaceFragment(fragment)
    }

}