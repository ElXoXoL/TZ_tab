package com.test.tz_tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.test.tz_tab.survey.intro.FragmentSurveyIntro
import com.test.tz_tab.utils.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentCount: Int
        get() = supportFragmentManager.backStackEntryCount

    private val dismissCallback = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            // Delete prev fragments

            for (i in 0 until fragmentCount - 1){
                val entry = supportFragmentManager.getBackStackEntryAt(i)
                Logger.log("Popped ${entry.name}")
                supportFragmentManager.popBackStack(entry.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            // We can send cached results on snack dismiss
        }
    }

    private val successSnackbar: Snackbar by lazy {
        Snackbar.make(this.window.decorView, R.string.text_snack_success, Snackbar.LENGTH_LONG)
            .setAction(R.string.text_undo) {
                // Responds to click on the action
                successSnackbar.removeCallback(dismissCallback)
                supportFragmentManager.popBackStack()
                checkBackEnabled()
            }
            .setActionTextColor(ContextCompat.getColor(this.applicationContext, R.color.colorAccent))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        val fragmentIntroSurvey =
            FragmentSurveyIntro()
        fragmentIntroSurvey.arguments = Bundle().apply {
            putString("title", "Welcome to the survey.")
            putString("subtitle", "We only have two questions and we need less than 1 minute of your time.")
            putString("type", "Choices")
        }
        replaceFragment(fragmentIntroSurvey)
    }

    override fun onBackPressed() {

        // On back press if snack is shown - hide it
        if (successSnackbar.isShown){
            successSnackbar.dismiss()
            return
        }

        if (fragmentCount == 1){
            finish()
            return
        }

        super.onBackPressed()
        checkBackEnabled()
    }

    internal fun replaceFragment(fragment: Fragment) {
        // Firstly dismiss snack
        if (successSnackbar.isShown){
            successSnackbar.dismiss()
            return
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment::class.java.name)
            .addToBackStack(fragment::class.java.name)
            .commitAllowingStateLoss().also {
                checkBackEnabled(true)
            }
    }

    // Pass true if fragment was just added, because fragment count isn't raising immediately
    private fun checkBackEnabled(isFragmentAdded: Boolean = false) {
        Logger.log(fragmentCount)
        supportActionBar?.setDisplayHomeAsUpEnabled(fragmentCount > if (isFragmentAdded) 0 else 1)
    }

    internal fun showSnackbar(){
        successSnackbar.addCallback(dismissCallback)
        successSnackbar.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}