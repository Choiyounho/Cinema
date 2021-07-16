package com.soten.todo.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.soten.todo.R
import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.databinding.ItemTodoBinding

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var todoList: List<TodoEntity> = listOf()
    private lateinit var todoItemClickListener: (TodoEntity) -> Unit
    private lateinit var todoCheckListener: (TodoEntity) -> Unit

    inner class ViewHolder(
        private val binding: ItemTodoBinding,
        val todoItemClickListener: (TodoEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: TodoEntity) = with(binding) {
            checkBox.text = data.title
            checkTodoComplete(data.hasCompleted)
        }

        fun bindViews(data: TodoEntity) {
            binding.checkBox.setOnClickListener {
                todoCheckListener(
                    data.copy(hasCompleted = binding.checkBox.isChecked)
                )
                checkTodoComplete(binding.checkBox.isChecked)
            }
            binding.root.setOnClickListener {
                todoItemClickListener(data)
            }
        }

        private fun checkTodoComplete(isChecked: Boolean) = with(binding) {
            checkBox.isChecked = isChecked
            container.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if (isChecked) {
                        R.color.gray_300
                    } else {
                        R.color.white
                    }
                )
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, todoItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(todoList[position])
        holder.bindViews(todoList[position])
    }

    override fun getItemCount(): Int = todoList.size

    fun setToDoList(toDoList: List<TodoEntity>, toDoItemClickListener: (TodoEntity) -> Unit, toDoCheckListener: (TodoEntity) -> Unit) {
        this.todoList = toDoList
        this.todoItemClickListener = toDoItemClickListener
        this.todoCheckListener = toDoCheckListener
        notifyDataSetChanged()
    }
}