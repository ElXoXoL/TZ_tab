package com.test.tz_tab.survey.money

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.test.tz_tab.MainActivity
import com.test.tz_tab.R
import com.test.tz_tab.enums.SurveyType
import com.test.tz_tab.models.SurveyChoices
import com.test.tz_tab.models.SurveyItem
import com.test.tz_tab.survey.choice.FragmentSurveyChoice
import com.test.tz_tab.survey.intro.FragmentSurveyIntro
import kotlinx.android.synthetic.main.fragment_survey_money.*

class FragmentSurveyMoney: Fragment(R.layout.fragment_survey_money){

    private  val argSurveyType: String by lazy { arguments?.getString("type") ?: "" }
    // Gets text from arguments or some default text
    private val titleText: String by lazy { arguments?.getString("title") ?: "Welcome to the survey." }
    private val subtitleText: String by lazy { arguments?.getString("subtitle") ?: "We only have two questions and we need less than 1 minute of your time." }

    private var watcher: MoneyWatcher? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets text from arguments or some default text
        // Like title we can change Currency
        val titleText = arguments?.getString("titleMoney") ?: "Welcome to the survey."
        tv_money_title.text = titleText

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            et_money_value.showSoftInputOnFocus = true
        }
        et_money_value.showKeyboard()

        val surveyType = SurveyType.values().firstOrNull { it.name == argSurveyType }

        et_money_value.onEnterAction = { surveyClick(surveyType) }
        btn_start_survey.setOnClickListener { surveyClick(surveyType) }
    }

    override fun onResume() {
        super.onResume()
        watcher = MoneyWatcher(et_money_value)
        et_money_value.addTextChangedListener(watcher)
        et_money_value.setText(arguments?.getString("moneyValue") ?: "")
    }

    override fun onPause() {
        et_money_value.removeTextChangedListener(watcher)
        super.onPause()
    }

    // Pass type to be able to set different surveys
    private fun surveyClick(surveyType: SurveyType?){
        if (watcher?.textValue.isNullOrEmpty()){
            Toast.makeText(this.activity, "Please, insert value", Toast.LENGTH_SHORT).show()
            return
        }

        arguments?.putString("moneyValue", watcher?.textValue ?: "")

        val fragment =
            FragmentSurveyIntro()

        fragment.arguments = Bundle().apply {
            putString("title", titleText)
            putString("subtitle", subtitleText)
            putString("type", argSurveyType)
            putBoolean("done", true)

            // We can pass the results here
        }


        when (surveyType) {
            SurveyType.Choices -> (activity as? MainActivity)?.replaceFragment(
                fragment
            )
            else -> (activity as? MainActivity)?.replaceFragment(fragment)
        }
    }
}