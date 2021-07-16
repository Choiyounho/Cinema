package com.soten.todo.di

import com.soten.todo.data.repository.TestTodoRepository
import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.todo.GetTodoListUseCase
import com.soten.todo.domain.todo.InsertTodoListUseCase
import com.soten.todo.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    // viewModel
    viewModel { ListViewModel(get()) }

    // UseCase
    factory { GetTodoListUseCase(get()) }
    factory { InsertTodoListUseCase(get()) }

    // Repository
    single<TodoRepository> { TestTodoRepository() }
}