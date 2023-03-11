package com.example.newsapp.ui.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.newsapp.ui.NewsActivity
import org.junit.Rule
import com.example.newsapp.R
import org.junit.Test

class LoginFragmentTest{
    @get:Rule
     var activityScenarioRule=ActivityScenarioRule(NewsActivity::class.java)
    @Test
    fun testLoginButton() {
        val username = "sdfghjklwertyuio"
        val password = "qwertyuioasdfghjk"

        // Click the login button
        onView(withId(R.id.etUserName)).perform(typeText(username))
        onView(withId(R.id.etPassword)).perform(typeText(password))
        onView(withId(R.id.btnLogin)).perform(click())

     // Verify that the snackbar message is displayed
       onView(withText("Please check your username & password,they should be min 6 symbols")).check(matches(isDisplayed()))
    }

}