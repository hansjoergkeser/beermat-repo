package de.hajo.beermat.database

import androidx.room.*
import de.hajo.beermat.model.User

/**
 * @author hansjoerg.keser
 * @since 23.11.18
 */
@Dao
interface UserDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(vararg users: User)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertUser(user: User)

	@Query("SELECT * FROM user")
	fun getAll(): List<User>

	@Query("SELECT * FROM user WHERE lastName LIKE :lastName")
	fun findByLastName(lastName: String): List<User>

	@Query("SELECT * FROM user WHERE firstName LIKE :firstName AND lastName LIKE :lastName")
	fun findByName(firstName: String, lastName: String): User

	@Query("SELECT COUNT(*) FROM user")
	fun countUsers(): Int

	@Query("SELECT has_premium_status FROM user WHERE firstName LIKE :firstName AND lastName LIKE :lastName")
	fun getPremiumStatus(firstName: String, lastName: String): Int

	@Query("DELETE FROM user")
	fun deleteAll()

	@Delete
	fun deleteUser(user: User)
}