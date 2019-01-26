package de.hajo.beermat

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author hansjoerg.keser
 * @since 26.01.19
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class FirstEspTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun assertButton() {
        onView(withId(R.id.tv_beer)).check(matches(isDisplayed()))
    }

}