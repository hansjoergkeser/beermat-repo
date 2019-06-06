package de.hajo.beermat.screenshots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.doubleClick
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import de.hajo.beermat.MainActivity
import de.hajo.beermat.R
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

/**
 *  @author hansjoerg.keser
 *  @since 2019-05-28
 *
 *  execute all screenshot tests in terminal:
 *  ./gradlew clean cAT -Pandroid.testInstrumentationRunnerArguments.package=de.hajo.beermat.screenshots
 *  https://developer.android.com/reference/android/support/test/runner/AndroidJUnitRunner.html -> Running all tests in a java package
 *
 *  execute certain tests in terminal like this:
 *  ./gradlew clean cAT -Pandroid.testInstrumentationRunnerArguments.class=de.hajo.beermat.screenshots.MainActivityScreenshotEspTests.00
 *  https://github.com/googlesamples/android-testing-templates/blob/master/AndroidTestingBlueprint/README.md#custom-gradle-command-line-arguments
 *
 *  if you execute the tests via IDE without the screenshotRunner.sh or the task connectedDebugAndroidTest (cAT) the screenshots are saved on device,
 *  but no report is generated under app/build/reports/androidTests/connected
 */
@RunWith(AndroidJUnit4::class)
class MainActivityScreenshotEspTests : ScreenshotEspSetup(ActivityTestRule(MainActivity::class.java)) {

	@Test
	fun `00`() {
		Thread.sleep(500)
		onView(withId(R.id.button_add)).check(matches(isDisplayed()))
		onView(withId(R.id.button_reduce)).check(matches(isDisplayed()))
	}

	@Test
	fun `01`() {
		onView(withId(R.id.tv_beer)).check(matches(isDisplayed()))
		onView(withId(R.id.et_price)).perform(click()).perform(clearText()).perform(typeText("2.99"))
		onView(withId(R.id.button_add)).perform(doubleClick())
		val expectedTotalPrice = "8,97 €"
		onView(withId(R.id.tv_total_price_of_line)).check(matches(allOf(isDisplayed(), withText(expectedTotalPrice))))
	}

}