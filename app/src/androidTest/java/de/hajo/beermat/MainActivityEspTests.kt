package de.hajo.beermat

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.util.Log
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
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.screenshot.Screenshot
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import java.util.Locale


/**
 * @author hansjoerg.keser
 * @since 26.01.19
 */
@RunWith(AndroidJUnit4::class)
class MainActivityEspTests {

	private val localeRule = LocaleRule(Locale("en"))
	private val activityRule = ActivityTestRule(MainActivity::class.java)
	private val grantPermissionRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

	@get:Rule
	val mRuleChain: RuleChain = RuleChain.outerRule(localeRule)
			.around(grantPermissionRule)
			.around(activityRule)

	@After
	fun makeScreenshotAfterwards() {
		val parentFolderPath = "espresso-screenshots/"
		takeScreenshot(parentFolderPath = parentFolderPath, screenShotName = "test-screenshot")
	}

	@Test
	fun assertButtons() {
		onView(withId(R.id.button_add)).check(matches(isDisplayed()))
		onView(withId(R.id.button_reduce)).check(matches(isDisplayed()))
	}

	@Test
	fun assertTotalPriceOfThreeBeers() {
		// TODO: reset app before start
		onView(withId(R.id.tv_beer)).check(matches(isDisplayed()))
		onView(withId(R.id.et_price)).perform(click()).perform(clearText()).perform(typeText("2.99"))
		onView(withId(R.id.button_add)).perform(doubleClick())
		val expectedTotalPrice = "8,97 €"
		onView(withId(R.id.tv_total_price_of_line)).check(matches(allOf(isDisplayed(), withText(expectedTotalPrice))))
	}

	private fun takeScreenshot(parentFolderPath: String = "", screenShotName: String) {
		Log.d("Screenshots", "Taking screenshot of '$screenShotName'")
		val screenCapture = Screenshot.capture()
		val processors = setOf(ScreenCaptureProcessor(parentFolderPath))

		screenCapture.apply {
			name = screenShotName
			process(processors)
		}
		Log.d("Espresso-Screenshots", "Screenshot: [$screenShotName] taken and saved in directory: [$parentFolderPath]")
	}

}