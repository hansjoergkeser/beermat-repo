package de.hajo.beermat.screenshots

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.screenshot.Screenshot
import de.hajo.beermat.LoginActivity
import de.hajo.beermat.R
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestName
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.Locale

/**
 *  @author hansjoerg.keser
 *  @since 2019-05-28
 *
 *  execute certain tests in terminal like this:
 *  ./gradlew clean cAT -Pandroid.testInstrumentationRunnerArguments.class=de.hajo.beermat.screenshots.LoginActivityScreenshotEspTests.02
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityScreenshotEspTests {

	private val localeRule = LocaleRule(Locale("de"))
	private val activityRule = ActivityTestRule(LoginActivity::class.java)
	private val grantPermissionRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
	private val testName = TestName()

	@get:Rule
	val mRuleChain: RuleChain = RuleChain.outerRule(localeRule)
			.around(grantPermissionRule)
			.around(activityRule)
			.around(testName)

	@After
	fun createDirAndMakeScreenshot() {
		// using test orchestrator to reset the app
		// https://developer.android.com/training/testing/junit-runner#using-android-test-orchestrator

		var deviceType = "unknown"
		val windowManager =
				InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val displayDimensions = Point()
		windowManager.defaultDisplay.getRealSize(displayDimensions)

		when ("" + displayDimensions.y + "x" + displayDimensions.x) {
			"1920x1080" -> deviceType = "phone"
			"1200x1920" -> deviceType = "tablet-7"
			"1536x2048" -> deviceType = "tablet-9"
		}

		val currentLocale =
				InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration.locales[0]
		val parentFolderPath = "$deviceType/$currentLocale"
		val currentScreenShotName = testName.methodName

		takeScreenshot(parentFolderPath = parentFolderPath, screenShotName = currentScreenShotName)
		Timber.i("Screenshot: [$currentScreenShotName] taken and saved in directory: [$parentFolderPath] on device external storage.")
	}

	@Test
	fun `02`() {
		Thread.sleep(1000)
		onView(withId(R.id.email)).check(matches(isDisplayed()))
		onView(withId(R.id.password)).check(matches(isDisplayed()))
		onView(withId(R.id.email_sign_in_button)).check(matches(isDisplayed()))
	}

	private fun takeScreenshot(parentFolderPath: String = "", screenShotName: String) {
		Timber.i("Taking screenshot of '$screenShotName'")
		val screenCapture = Screenshot.capture()
		val processors = setOf(ScreenCaptureProcessor(parentFolderPath))

		screenCapture.apply {
			name = screenShotName
			process(processors)
		}
	}

}