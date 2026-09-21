package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CommentDao
import com.example.data.local.dao.FriendDao
import com.example.data.local.dao.GroupDao
import com.example.data.local.dao.MessageDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.PageDao
import com.example.data.local.dao.PostDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.GroupEntity
import com.example.data.local.entity.MessageEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        CommentEntity::class,
        GroupEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        FriendEntity::class,
        PageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class KundeDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun groupDao(): GroupDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userDao(): UserDao
    abstract fun friendDao(): FriendDao
    abstract fun pageDao(): PageDao

    companion object {
        @Volatile
        private var INSTANCE: KundeDatabase? = null

        fun getDatabase(context: Context): KundeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KundeDatabase::class.java,
                    "kunde_connect_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
