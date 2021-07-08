package com.soten.githubrepository

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.soten.githubrepository.data.db.DBProvider
import com.soten.githubrepository.data.entity.GithubRepositoryEntity
import com.soten.githubrepository.databinding.ActivityRepositoryBinding
import com.soten.githubrepository.extensions.loadCenterInside
import com.soten.githubrepository.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RepositoryActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    companion object {
        const val REPOSITORY_OWNER_KEY = "repositoryOwner"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }

    private lateinit var binding: ActivityRepositoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repositoryOwner = intent.getStringExtra(REPOSITORY_OWNER_KEY) ?: run {
            toast("Repository Owner 이름이 없습니다.")
            finish()
            return
        }
        val repositoryName = intent.getStringExtra(REPOSITORY_NAME_KEY) ?: run {
            toast("Repository 이름이 없습니다.")
            finish()
            return
        }

        launch {
            loadRepository(repositoryOwner, repositoryName)?.let {
                setData(it)
            } ?: run {
                toast("Repository 정보가 없습니다.")
                finish()
            }
        }

        showLoading(true)
    }

    private suspend fun loadRepository(repositoryOwner: String, repositoryName: String): GithubRepositoryEntity? =
        withContext(coroutineContext) {
            var repository: GithubRepositoryEntity? = null
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.githubApiService.getRepository(
                    ownerLogin = repositoryOwner,
                    repoName = repositoryName
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    withContext(Dispatchers.Main) {
                        body?.let { repo ->
                            repository = repo
                        }
                    }
                }
            }
            repository
        }

    private fun setData(githubRepositoryEntity: GithubRepositoryEntity) = with(binding) {
        Log.e("data", githubRepositoryEntity.toString())
        showLoading(false)
        ownerProfileImageView.loadCenterInside(githubRepositoryEntity.owner.avatarUrl, 42f)
        ownerNameAndRepoNameTextView.text= "${githubRepositoryEntity.owner.login}/${githubRepositoryEntity.name}"
        stargazersCountText.text = githubRepositoryEntity.stargazersCount.toString()
        githubRepositoryEntity.language?.let { language ->
            languageText.isGone = false
            languageText.text = language
        } ?: run {
            languageText.isGone = true
            languageText.text = ""
        }
        descriptionTextView.text = githubRepositoryEntity.description
        updateTimeTextView.text = githubRepositoryEntity.updatedAt

        setLikeState(githubRepositoryEntity)
    }

    private fun setLikeState(githubRepositoryEntity: GithubRepositoryEntity) = launch {
        withContext(Dispatchers.IO) {
            val repository = DBProvider.provideDB(this@RepositoryActivity).searchHistoryDao().getRepository(githubRepositoryEntity.fullName)
            val isLike = repository != null
            withContext(Dispatchers.Main) {
                setLikeImage(isLike)
                binding.likeButton.setOnClickListener {
                    Log.e("testT", "클릭")
                    likeRepository(githubRepositoryEntity, isLike)
                }
            }
        }
    }

    private fun setLikeImage(isLike: Boolean) {
        binding.likeButton.setImageDrawable(
            ContextCompat.getDrawable(this@RepositoryActivity,
            if (isLike) {
                R.drawable.ic_like
            } else {
                R.drawable.ic_dislike
            }
        ))
    }

    private fun likeRepository(githubRepositoryEntity: GithubRepositoryEntity, isLike: Boolean) = launch {
        withContext(Dispatchers.IO) {
            val dao = DBProvider.provideDB(this@RepositoryActivity).searchHistoryDao()
            if (isLike) {
                dao.remove(githubRepositoryEntity.fullName)
            } else {
                dao.insert(githubRepositoryEntity)
            }
            withContext(Dispatchers.Main) {
                setLikeImage(isLike.not())
            }
        }
    }

    private fun showLoading(isShown: Boolean) = with(binding) {
        progressBar.isGone = isShown.not()
    }

    private fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}