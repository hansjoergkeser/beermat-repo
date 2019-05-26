package de.hajo.beermat

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import de.hajo.beermat.database.BeerDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.io.IOException

/**
 * @author hansjoerg.keser
 * @since 10.12.18
 */
@RunWith(AndroidJUnit4::class)
class PremiumUserMigrationTest {

	private val TEST_DB = "migration-test-database.db"
	private lateinit var db: SupportSQLiteDatabase

	private val MIGRATION_1_2 = object : Migration(1, 2) {
		override fun migrate(database: SupportSQLiteDatabase) {
			Timber.d("Executing room database migration from version 1 to 2: ALTER TABLE User ADD COLUMN has_premium_status INTEGER NOT NULL DEFAULT 0")
			database.execSQL("ALTER TABLE User ADD COLUMN has_premium_status INTEGER NOT NULL DEFAULT 0")
		}
	}

	@JvmField
	@Rule
	val helper: MigrationTestHelper = MigrationTestHelper(
			InstrumentationRegistry.getInstrumentation(),
			BeerDatabase::class.java.canonicalName,
			FrameworkSQLiteOpenHelperFactory()
	)

	@After
	fun closeAndRemoveDb() {
		db.close()
		getApplicationContext<Context>().deleteDatabase(TEST_DB)
	}

	@Test
	@Throws(IOException::class)
	fun migrate1To2() {
		db = helper.createDatabase(TEST_DB, 1).apply {
			// db has schema version 1. insert some data using SQL queries.
			// You cannot use DAO classes because they expect the latest schema.
			execSQL("INSERT INTO User VALUES (nullif(?, 0),'test@gmail.com','SuperSafePassword','Ich','HabsDrauf')")
			execSQL("INSERT INTO User VALUES (nullif(?, 0),'totalfalscheemailohneaddzeichen.com','Mega_Seltsames_Passwort#äö+üp-.,12334567890','IchFrageMich','WasHierPassierenWird')")

			// Prepare for the next version.
			close()
		}

		// Re-open the database with version 2 and provide
		// MIGRATION_1_2 as the migration process.
		db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

		// MigrationTestHelper automatically verifies the schema changes,
		// but you need to validate that the data was migrated properly.
		assertTrue(db.query("SELECT * FROM User").toString().isNotEmpty())
		assertEquals(2, db.query("SELECT * FROM User").count)

		val newPremiumUserValues = ContentValues(6)
		newPremiumUserValues.putNull("uid")
		newPremiumUserValues.put("email", "premium_user@gmail.com")
		newPremiumUserValues.put("password", "SuperBadPassword")
		newPremiumUserValues.put("firstname", "Ich")
		newPremiumUserValues.put("lastname", "HabsImmerNochDrauf")
		newPremiumUserValues.put("has_premium_status", 1)

		db.insert("User", SQLiteDatabase.CONFLICT_FAIL, newPremiumUserValues)
		// does not work, does not throw an exception, no idea why
//        db.query("INSERT INTO User VALUES (nullif(?, 0),'premium_user@gmail.com','SuperBadPassword','Ich','HabsImmerNochDrauf','1')")
		assertEquals(3, db.query("SELECT * FROM User").count)
	}

}