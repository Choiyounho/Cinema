package com.soten.todo.di

import android.content.Context
import androidx.room.Room
import com.soten.todo.data.local.db.TodoDataBase
import com.soten.todo.data.repository.DefaultTodoRepository
import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.todo.*
import com.soten.todo.presentation.detail.DetailMode
import com.soten.todo.presentation.detail.DetailViewModel
import com.soten.todo.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // viewModel
    viewModel { ListViewModel(get(), get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        )
    }

    // UseCase
    factory { GetTodoListUseCase(get()) }
    factory { InsertTodoItemUseCase(get()) }
    factory { InsertTodoListUseCase(get()) }
    factory { UpdateTodoUseCase(get()) }
    factory { GetTodoItemUseCase(get()) }
    factory { DeleteTodoItemUseCase(get()) }
    factory { DeleteAllTodoItemUseCase(get()) }

    // Repository
    single<TodoRepository> { DefaultTodoRepository(get(), get()) }
    single { provideDb(androidContext()) }
    single { provideTodoDao(get()) }
}

internal fun provideDb(context: Context): TodoDataBase =
    Room.databaseBuilder(context, TodoDataBase::class.java, TodoDataBase.DB_NAME).build()

internal fun provideTodoDao(dataBase: TodoDataBase) = dataBase.toDoDao()