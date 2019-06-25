package de.hajo.beermat.screenshots

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.screenshot.Screenshot
import org.junit.After
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.rules.TestName
import timber.log.Timber
import java.util.Locale

/**
 *  @author hansjoerg.keser
 *  @since 2019-06-06
 */
abstract class ScreenshotEspSetup<T : Activity>(activityRule: ActivityTestRule<T>) {

	private val localeRule = LocaleRule(Locale.GERMANY, Locale.UK)
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

		val currentLanguage = localeRule.getLanguageOfCurrentLocale()
		val parentFolderPath = "$deviceType/$currentLanguage"
		val currentScreenShotName = testName.methodName

		takeScreenshot(parentFolderPath = parentFolderPath, screenShotName = currentScreenShotName)
		Timber.i("Screenshot: [$currentScreenShotName] taken and saved in directory: [$parentFolderPath] on device external storage.")
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