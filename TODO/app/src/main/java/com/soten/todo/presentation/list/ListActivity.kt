package com.soten.todo.presentation.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.soten.todo.R
import com.soten.todo.databinding.ActivityListBinding
import com.soten.todo.presentation.BaseActivity
import com.soten.todo.presentation.view.TodoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _binding: ActivityListBinding? = null
    private val binding get() = _binding!!

    private val adapter = TodoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override val viewModel: ListViewModel by viewModel()

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        addToDoButton.setOnClickListener {
//            startActivityForResult(
//                DetailActivity.getIntent(this@ListActivity, DetailMode.WRITE),
//                DetailActivity.FETCH_REQUEST_CODE
//            )
        }
    }

    override fun observeData() {
        viewModel.todoListLiveData.observe(this) {
            when (it) {
                is TodoListState.UnInitialized -> {
                    initViews(binding)
                }
                is TodoListState.Loading -> {
                    handleLoadingState()
                }
                is TodoListState.Success -> {
                    handleSuccessState(it)
                }
                is TodoListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun handleErrorState() {
        Toast.makeText(this, "오류 입니다", Toast.LENGTH_SHORT).show()
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: TodoListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.todoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.todoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setToDoList(
                state.todoList,
                toDoItemClickListener = {
//                    startActivityForResult(
//                        DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
//                        DetailActivity.FETCH_REQUEST_CODE
//                    )
                }, toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == DetailActivity.FETCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            viewModel.fetchData()
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                viewModel.deleteAll()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }
}