package br.edu.scl.ifsp.ads.sharedlist.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskDaoRoom : RoomDatabase() {
    companion object Constants {
        const val CONTACT_DATABASE_FILE = "contacts_room"
    }
    abstract fun getContactDao(): TaskDao
}