package com.test.tz_tab.survey.choice

import com.test.tz_tab.enums.SurveyType
import com.test.tz_tab.models.SurveyItem

interface ChoiceCallback {

    fun choose(item: SurveyItem)

}