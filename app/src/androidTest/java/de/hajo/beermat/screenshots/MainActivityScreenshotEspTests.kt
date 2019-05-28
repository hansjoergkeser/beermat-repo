package de.hajo.beermat.screenshots

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.screenshot.Screenshot
import de.hajo.beermat.MainActivity
import de.hajo.beermat.R
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.*

/**
 *  @author hansjoerg.keser
 *  @since 2019-05-28
 *
 * execute certain tests in terminal:
 * ./gradlew cAT -Pandroid.testInstrumentationRunnerArguments.class=de.hajo.beermat.MainActivityEspTests
 * https://github.com/googlesamples/android-testing-templates/blob/master/AndroidTestingBlueprint/README.md#custom-gradle-command-line-arguments
 */
@RunWith(AndroidJUnit4::class)
class MainActivityScreenshotEspTests {

    // TODO: add locales
    private val localeRule = LocaleRule(Locale("de"))
    private val activityRule = ActivityTestRule(MainActivity::class.java)
    private val grantPermissionRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val mRuleChain: RuleChain = RuleChain.outerRule(localeRule)
        .around(grantPermissionRule)
        .around(activityRule)

    @After
    fun makeScreenshotAndResetApp() {
        // using test orchestrator to reset the app
        // https://developer.android.com/training/testing/junit-runner#using-android-test-orchestrator

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
        onView(withId(R.id.tv_beer)).check(matches(isDisplayed()))
        onView(withId(R.id.et_price)).perform(click()).perform(clearText()).perform(typeText("2.99"))
        onView(withId(R.id.button_add)).perform(doubleClick())
        val expectedTotalPrice = "8,97 €"
        onView(withId(R.id.tv_total_price_of_line)).check(matches(allOf(isDisplayed(), withText(expectedTotalPrice))))
    }

    private fun takeScreenshot(parentFolderPath: String = "", screenShotName: String) {
        Timber.i("Taking screenshot of '$screenShotName'")
        val screenCapture = Screenshot.capture()
        val processors = setOf(ScreenCaptureProcessor(parentFolderPath))

        screenCapture.apply {
            name = screenShotName
            process(processors)
        }
        Timber.i("Screenshot: [$screenShotName] taken and saved in directory: [$parentFolderPath]")
    }

}