package com.redmechax00.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoItemData::class], version = 1, exportSchema = false)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun getItemDao(): ItemDao
}