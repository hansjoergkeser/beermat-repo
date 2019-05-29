package de.hajo.beermat.screenshots

import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import java.io.File

/**
 *  @author hansjoerg.keser
 *  @since 2019-05-24
 */
class ScreenCaptureProcessor(parentFolderPath: String) : BasicScreenCaptureProcessor() {

	init {
		this.mDefaultScreenshotPath = File(
				File(
						getExternalStoragePublicDirectory(DIRECTORY_PICTURES),
						"beermat-screenshots"
				).absolutePath,
				"espresso-screenshots/$parentFolderPath"
		)
	}

	override fun getFilename(prefix: String): String = prefix

}