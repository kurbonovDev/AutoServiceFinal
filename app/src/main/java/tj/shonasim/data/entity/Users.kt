package tj.shonasim.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["phone"], unique = true)])
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = -1,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "login")
    val login: String = "",

    @ColumnInfo(name = "password")
    val password: String = "",

    @ColumnInfo(name = "role", defaultValue = "client")
    val role: String = "",
)