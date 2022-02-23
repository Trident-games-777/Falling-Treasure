package falling.treasur.ggamee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import falling.treasur.ggamee.util.Constants.Companion.DB_NAME

@Database(entities = [LinkEntity::class], version = 1)
abstract class LinkDb : RoomDatabase() {
    abstract fun linkDao(): LinkDao

    companion object {
        @Volatile
        private var instance: LinkDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LinkDb::class.java,
                DB_NAME
            ).build()
    }


}