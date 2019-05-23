package de.hajo.beermat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author hansjoerg.keser
 * @since 07.12.18
 */
@Entity
data class User(
		@ColumnInfo(name = "email")
		val email: String,

		@ColumnInfo(name = "password")
		val password: String,

		@ColumnInfo(name = "firstname")
		val firstName: String = "",

		@ColumnInfo(name = "lastname")
		val lastName: String = "",

		@ColumnInfo(name = "has_premium_status")
		val hasPremiumStatus: Int = 0
) {
	@ColumnInfo(name = "uid")
	@PrimaryKey(autoGenerate = true)
	var uid: Int = 0
}