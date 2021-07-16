package com.soten.todo.viewmodel.todo

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.domain.todo.InsertTodoItemUseCase
import com.soten.todo.presentation.detail.DetailMode
import com.soten.todo.presentation.detail.DetailViewModel
import com.soten.todo.presentation.detail.TodoDetailState
import com.soten.todo.presentation.list.ListViewModel
import com.soten.todo.presentation.list.TodoListState
import com.soten.todo.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트 하기위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test delete todo
 * 4. test update todo
 *
 */
@ExperimentalCoroutinesApi
internal class DetailViewModelTest : ViewModelTest() {

    private val id = 1L

    private val detailViewModel: DetailViewModel by inject { parametersOf(DetailMode.DETAIL, id) }
    private val listViewModel: ListViewModel by inject()
    private val insertTodoItemUseCase: InsertTodoItemUseCase by inject()

    private val todo = TodoEntity(
        id = id,
        title = "title 1",
        description = "description 1",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {
        insertTodoItemUseCase(todo)
    }

    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Success(todo)
            )
        )
    }

    @Test
    fun `test delete todo`() = runBlockingTest {
        val detailTestObserver = detailViewModel.todoDetailLiveData.test()
        detailViewModel.deleteTodo()

        detailTestObserver.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.todoListLiveData.test()
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                TodoListState.UnInitialized,
                TodoListState.Loading,
                TodoListState.Success(listOf())
            )
        )
    }

    @Test
    fun `test update todo`() = runBlockingTest {
        val testObserver = detailViewModel.todoDetailLiveData.test()

        val updateTitle = "title 1 update"
        val updateDescription = "title 1 description"

        val updateTodo = todo.copy(
            title = updateTitle,
            description = updateDescription
        )

        detailViewModel.writeTodo(updateTodo.title, updateTodo.description)

        testObserver.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Success(updateTodo)
            )
        )
    }
}