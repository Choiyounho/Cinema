package com.soten.todo.di

import com.soten.todo.data.repository.TestTodoRepository
import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.todo.*
import com.soten.todo.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    // viewModel
    viewModel { ListViewModel(get(), get(), get()) }

    // UseCase
    factory { GetTodoListUseCase(get()) }
    factory { InsertTodoListUseCase(get()) }
    factory { UpdateTodoUseCase(get()) }
    factory { GetTodoItemUseCase(get()) }
    factory { DeleteAllTodoItemUseCase(get()) }

    // Repository
    single<TodoRepository> { TestTodoRepository() }
}