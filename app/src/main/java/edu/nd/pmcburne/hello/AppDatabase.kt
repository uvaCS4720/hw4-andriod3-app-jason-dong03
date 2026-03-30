package edu.nd.pmcburne.hello
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocationEntity::class, LocationTagEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}