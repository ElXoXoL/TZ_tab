package com.test.tz_tab.survey.money

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.test.tz_tab.utils.Logger
import java.lang.Exception

class MoneyWatcher(private val edit: MoneyEdit): TextWatcher {

    var textValue: String = ""
    var textBefore = ""

    override fun afterTextChanged(s: Editable?) {
        edit.removeTextChangedListener(this)
        val text = s?.toString() ?: ""

        Logger.log(text)
        if (text.contains(textBefore)) {
            val newText = text.replace(textBefore, "")
            textValue += newText
        } else {
            textValue = try {
                textValue.removeRange(textValue.length - 1, textValue.length - 0)
            } catch (e: Exception){
                ""
            }
        }

        textValue = if (textValue == "0") "" else textValue

        textBefore = getShowedText(textValue)
        edit.setText(textBefore)
        edit.setSelection(textBefore.length)

        edit.addTextChangedListener(this)
    }

    private fun getShowedText(value: String): String{
        var fixedValue = value
        while (fixedValue.length < 3){
            fixedValue = "0$fixedValue"
        }
        fixedValue = fixedValue.replace(",", "")
        fixedValue = fixedValue.substring(0, fixedValue.length - 2) + "," + fixedValue.substring(fixedValue.length - 2)
        return fixedValue
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}