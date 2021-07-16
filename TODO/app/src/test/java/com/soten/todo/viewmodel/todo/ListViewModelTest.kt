package com.soten.todo.viewmodel.todo

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.domain.todo.InsertTodoListUseCase
import com.soten.todo.presentation.list.ListViewModel
import com.soten.todo.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.inject

/**
 * [ListViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test item update
 * 4. test item delete all
 *
 */

@ExperimentalCoroutinesApi
internal class ListViewModelTest: ViewModelTest() {

    private val viewModel: ListViewModel by inject()

    private val insertTodoListUseCase: InsertTodoListUseCase by inject()

    private val mockList = (0..9).map {
        TodoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "des $it",
            hasCompleted = false
        )
    }

    /**
     * 필요한 UseCase
     * 1. InsertTodoListUseCase
     * 2. GetTodoItemUseCase
     */

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {
        insertTodoListUseCase(mockList)
    }

    // Test : 입력된 데이터를 불러와서 검증한다.
    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                mockList
            )
        )
    }
}