package com.redmechax00.todolist.utils

import com.redmechax00.todolist.MainActivity

lateinit var APP_ACTIVITY: MainActivity

const val TODO_ITEMS_DATABASE = "database-todo-items"

const val APP_PREFERENCES = "database-todo-settings"
const val APP_PREFERENCES_VISIBILITY = "visibility"


const val TAG_ITEM_ID = "itemId"
const val ITEM_ID_NEW = "new"

const val ITEM_TYPE_BASE = 100
const val ITEM_TYPE_NEW = 101
const val ITEM_TYPE_DESC = 102
const val ITEM_TYPE_IMPORTANCE = 103
const val ITEM_TYPE_DEADLINE = 104
const val ITEM_TYPE_DELETE = 105

const val ITEM_POSITION_NEW = -1
const val ITEM_TIME_VALUE_NONE = -1L

const val ITEM_LEVEL_TOP = "top"
const val ITEM_LEVEL_MIDDLE = "middle"
const val ITEM_LEVEL_BOTTOM = "bottom"
const val ITEM_LEVEL_LONELY = "lonely"

const val ITEM_IMPORTANCE_NO = "no"
const val ITEM_IMPORTANCE_LOW = "low"
const val ITEM_IMPORTANCE_HIGH = "high"