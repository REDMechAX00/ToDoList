package com.redmechax00.todolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.redmechax00.todolist.utils.*

@Entity
data class TodoItemData(
    @ColumnInfo(name = "item_type") val itemType: Int = ITEM_TYPE_BASE,
    @ColumnInfo(name = "item_position") val itemPosition: Int = ITEM_POSITION_NEW,
    @ColumnInfo(name = "item_level") val itemLevel: String = ITEM_LEVEL_MIDDLE,

    @PrimaryKey @ColumnInfo(name = "item_id") val itemId: String = "",
    @ColumnInfo(name = "item_text") val itemText: String = "",
    @ColumnInfo(name = "item_importance") val itemImportance: String = ITEM_IMPORTANCE_NO,
    @ColumnInfo(name = "item_deadline") val itemDeadline: Long = ITEM_TIME_VALUE_NONE,
    @ColumnInfo(name = "item_is_success") val itemIsSuccess: Boolean = false,
    @ColumnInfo(name = "item_date_created") val itemDateCreated: Long = ITEM_TIME_VALUE_NONE,
    @ColumnInfo(name = "item_date_changed") val itemDateChanged: Long = ITEM_TIME_VALUE_NONE
)
