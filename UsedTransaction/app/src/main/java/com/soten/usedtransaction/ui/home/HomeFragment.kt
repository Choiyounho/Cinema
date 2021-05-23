package com.soten.usedtransaction.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soten.usedtransaction.DBKey.Companion.CHILD_CHAT
import com.soten.usedtransaction.DBKey.Companion.DB_ARTICLES
import com.soten.usedtransaction.DBKey.Companion.DB_USERS
import com.soten.usedtransaction.R
import com.soten.usedtransaction.databinding.FragmentHomeBinding
import com.soten.usedtransaction.ui.chatlist.ChatListItem

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleDb: DatabaseReference
    private lateinit var userDb: DatabaseReference

    private var binding: FragmentHomeBinding? = null

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleList.clear()
        articleDb = Firebase.database.reference.child(DB_ARTICLES)
        userDb = Firebase.database.reference.child(DB_USERS)
        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (auth.currentUser != null) {
                // 로그인 상태
                if (auth.currentUser?.uid != articleModel.sellerId) {
                    val chatRoom = ChatListItem(
                        buyerId = auth.currentUser?.uid.orEmpty(),
                        sellerId = articleModel.sellerId,
                        itemTitle = articleModel.title,
                        key = System.currentTimeMillis()
                    )

                    userDb.child(auth.currentUser!!.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDb.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요", Snackbar.LENGTH_SHORT).show()


                } else {
                    // 내가 올린 아이템
                    Snackbar.make(view, "내가 올린 아이템입니다.", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                // 로그인 안 한 상태
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_SHORT).show()
            }

        })

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            context?.let {
                auth.currentUser?.let { _ ->
                    val intent = Intent(Intent(it, AddArticleActivity::class.java))
                    startActivity(intent)
                } ?: (Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_SHORT).show())
            }
        }


        articleDb.addChildEventListener(listener)
    }

    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        articleDb.removeEventListener(listener)
    }

}