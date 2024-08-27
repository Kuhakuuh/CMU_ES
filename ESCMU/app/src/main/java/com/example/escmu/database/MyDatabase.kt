package com.example.escmu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.escmu.database.dao.ExpenseDao
import com.example.escmu.database.dao.UserDao
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.User

@Database(
    entities = [User::class, Expense::class],
    version = 2,
    exportSchema = false
)



abstract class MyDatabase : RoomDatabase() {

    abstract val userDao: UserDao;
    abstract val expenseDao: ExpenseDao;


    private var isInitialized = false
    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "user_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}