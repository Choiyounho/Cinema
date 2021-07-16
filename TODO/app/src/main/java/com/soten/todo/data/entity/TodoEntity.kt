package com.soten.todo.data.entity

data class TodoEntity(
    val id: Long = 0,
    val title: String,
    val description: String,
    val hasCompleted: Boolean = false
)
