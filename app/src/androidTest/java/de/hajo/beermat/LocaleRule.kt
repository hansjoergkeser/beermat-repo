package de.hajo.beermat

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.Locale

/**
 *  @author hansjoerg.keser
 *  @since 2019-05-26
 */
class LocaleRule(vararg locales: Locale) : TestRule {

	private val mLocales: Array<out Locale> = locales
	private var deviceLocale: Locale? = null

	/**
	 * Modifies the method-running [Statement] to implement this
	 * test-running rule.
	 *
	 * @param base The [Statement] to be modified
	 * @param description A [Description] of the test implemented in `base`
	 * @return a new statement, which may be the same as `base`,
	 * a wrapper around `base`, or a completely new Statement.
	 */
	override fun apply(base: Statement, description: Description): Statement {
		return object : Statement() {
			@Throws(Throwable::class)
			override fun evaluate() {
				try {
					deviceLocale = Locale.getDefault()
					for (locale in mLocales) {
						setLocale(locale)
						base.evaluate()
					}
				} finally {
					// reset emulator to default locale
					deviceLocale?.let {
						setLocale(it)
					}
				}
			}
		}
	}

	private fun setLocale(locale: Locale) {
		val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources
		Locale.setDefault(locale)
		val config = resources.configuration
		config.setLocale(locale)
		InstrumentationRegistry.getInstrumentation().targetContext.createConfigurationContext(config)
	}

}