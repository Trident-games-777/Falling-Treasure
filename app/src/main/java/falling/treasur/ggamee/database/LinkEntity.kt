package falling.treasur.ggamee.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "full_link_table")
data class LinkEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val link: String,
    val flag: Boolean
)