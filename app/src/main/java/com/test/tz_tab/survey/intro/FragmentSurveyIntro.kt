package com.test.tz_tab.survey.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.test.tz_tab.MainActivity
import com.test.tz_tab.R
import com.test.tz_tab.enums.SurveyType
import com.test.tz_tab.models.SurveyChoices
import com.test.tz_tab.models.SurveyItem
import com.test.tz_tab.survey.choice.FragmentSurveyChoice
import kotlinx.android.synthetic.main.fragment_survey_intro.*

class FragmentSurveyIntro: Fragment(R.layout.fragment_survey_intro){

    private  val argSurveyType: String by lazy { arguments?.getString("type") ?: "" }

    // Gets text from arguments or some default text
    private val titleText: String by lazy { arguments?.getString("title") ?: "Welcome to the survey." }
    private val subtitleText: String by lazy { arguments?.getString("subtitle") ?: "We only have two questions and we need less than 1 minute of your time." }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfSurveyDone()

        tv_intro_title.text = titleText
        tv_intro_subtitle.text = subtitleText

        val surveyType = SurveyType.values().firstOrNull { it.name == argSurveyType }
        btn_start_survey.setOnClickListener { surveyClick(surveyType) }
    }

    private fun checkIfSurveyDone(){
        // If DONE, we can get survey results and put send them on snack dismiss

        if (arguments?.getBoolean("done") == true){
            // Clears "done" to pop up only 1 time
            arguments?.remove("done")
            (activity as? MainActivity)?.showSnackbar()
        }
    }

    // Pass type to be able to set different surveys
    private fun surveyClick(surveyType: SurveyType?){
        val fragment =
            FragmentSurveyChoice()

        val choices = SurveyChoices(
            listOf(
                SurveyItem(1, "0 - 5 hours"),
                SurveyItem(2, "6 - 12 hours"),
                SurveyItem(3, "13 - 18 hours"),
                SurveyItem(4, "19 - 24 hours")
            )
        )

        fragment.arguments = Bundle().apply {
            putString("titleChoice", "How many hours did you have electricity yesterday?")
            putString("title", titleText)
            putString("subtitle", subtitleText)
            putString("type", argSurveyType)
            putSerializable("choices", choices)
        }

        when (surveyType) {
            SurveyType.Choices -> (activity as? MainActivity)?.replaceFragment(
                fragment
            )
            else -> (activity as? MainActivity)?.replaceFragment(fragment)
        }
    }
}