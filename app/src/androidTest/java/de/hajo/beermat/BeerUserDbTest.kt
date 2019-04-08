package de.hajo.beermat

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import de.hajo.beermat.database.BeerDatabase
import de.hajo.beermat.database.UserDao
import de.hajo.beermat.model.User
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * @author hansjoerg.keser
 * @since 26.11.18
 */
class Beermat {

    private lateinit var userDao: UserDao
    private lateinit var db: BeerDatabase

    @Before
    fun createDb() {
		val context = getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BeerDatabase::class.java)
            .build()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun writeUserAndReadInList() {
        val user = User("Hajooo", "Cheddar")
        userDao.insertUser(user)
        val byName = userDao.findByName("Hajooo", "Cheddar")
        assertEquals(user.email, byName.email)
    }

    @Test
    fun deleteUser() {
        val user = User("hajo@cheese.de", "Gouda")
        userDao.insertUser(user)
        userDao.deleteUser(user)
        val byName = userDao.findByName("Hajö", "Gouda")
        assertEquals("User has not been deleted.", null, byName)
    }

}