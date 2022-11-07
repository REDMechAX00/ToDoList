package com.redmechax00.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.redmechax00.todolist.databinding.ActivityMainBinding
import com.redmechax00.todolist.ui.TodoItemsViewModel
import com.redmechax00.todolist.ui.main.MainFragment
import com.redmechax00.todolist.ui.tasks.TasksFragment
import com.redmechax00.todolist.utils.APP_ACTIVITY
import com.redmechax00.todolist.utils.replaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_ACTIVITY = this

        viewModel = ViewModelProvider(this)[TodoItemsViewModel()::class.java]

        restoreFragment()

    }

    private fun restoreFragment() {
        when (viewModel.getCurrentFragment()) {
            is TasksFragment -> {
                val itemId = viewModel.getEditedItem()?.itemId
                replaceFragment(this, TasksFragment(), itemId, false)
            }
            else -> {
                replaceFragment(this, MainFragment(), null, false)
            }
        }
    }

}