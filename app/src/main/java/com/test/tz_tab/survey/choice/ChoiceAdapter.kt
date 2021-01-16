package com.test.tz_tab.survey.choice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.tz_tab.R
import com.test.tz_tab.models.SurveyChoices
import com.test.tz_tab.models.SurveyItem
import com.test.tz_tab.utils.Logger
import kotlinx.android.synthetic.main.item_survey_choice.view.*

class ChoiceAdapter(private val choices: SurveyChoices, var choosenItem: SurveyItem?, private val successCallback: ChoiceCallback) : RecyclerView.Adapter<ChoiceAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(item: SurveyItem) {
            itemView.radio_choice.isChecked = item == choosenItem
            itemView.radio_choice.text = item.value
            itemView.setOnClickListener {
                if (itemView.radio_choice.isChecked){
                    successCallback.choose(item)
                    return@setOnClickListener
                }
                itemView.radio_choice.isChecked = true
            }
            itemView.radio_choice.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    // save selected SurveyChoice
                    successCallback.choose(item)
                    Logger.log("Lol")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_survey_choice, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return choices.list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(choices.list[position])
    }
}