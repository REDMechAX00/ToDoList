package com.redmechax00.todolist.data

import androidx.room.*

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item: TodoItemData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: TodoItemData)

    @Update
    fun update(item: TodoItemData)

    @Query("DELETE FROM TodoItemData")
    fun deleteAll()

    @Delete
    fun delete(item: TodoItemData)

    @Query("SELECT * FROM TodoItemData")
    fun getAllItems(): List<TodoItemData>

    // Получение итема по его идентификатору
    @Query("SELECT * FROM TodoItemData WHERE item_id=(:id)")
    fun getItemById(id: String): TodoItemData
}