package com.example.escmu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.escmu.database.dao.ExpenseDao
import com.example.escmu.database.dao.GroupDao
import com.example.escmu.database.dao.UserDao
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import com.example.escmu.database.models.User

@Database(
    entities = [User::class, Expense::class, Group::class],
    version =18,
    exportSchema = false
)



abstract class MyDatabase : RoomDatabase() {

    abstract val userDao: UserDao;
    abstract val expenseDao: ExpenseDao;
    abstract val groupDao:GroupDao;


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
                        "expense_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}