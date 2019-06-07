package de.hajo.beermat.screenshots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import de.hajo.beermat.LoginActivity
import de.hajo.beermat.R
import org.junit.Test
import org.junit.runner.RunWith

/**
 *  @author hansjoerg.keser
 *  @since 2019-05-28
 *
 *  execute certain tests in terminal like this:
 *  ./gradlew clean cAT -Pandroid.testInstrumentationRunnerArguments.class=de.hajo.beermat.screenshots.LoginActivityScreenshotEspTests.02
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityScreenshotEspTests : ScreenshotEspSetup<LoginActivity>(ActivityTestRule(LoginActivity::class.java)) {

	@Test
	fun `02`() {
		onView(withId(R.id.email)).check(matches(isDisplayed()))
		onView(withId(R.id.password)).check(matches(isDisplayed()))
		onView(withId(R.id.email_sign_in_button)).check(matches(isDisplayed()))
	}

}