package com.example.mydemo.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mydemo.R
import com.example.mydemo.data.AppDatabase
import com.example.mydemo.data.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val noteList = mutableListOf<Note>()
    private lateinit var database: AppDatabase
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        // 初始化SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        // 初始化RecyclerView
        setupRecyclerView()
        // 初始化SwipeRefreshLayout
        setupSwipeRefresh()
        if (noteList.isEmpty()) {
            loadNotes(false)
        }
        return view
    }

    /**
     * 初始化RecyclerView，设置布局管理器和适配器
     */
    private fun setupRecyclerView() {

        noteAdapter = NoteAdapter(noteList)
        val gridLayoutManager = GridLayoutManager(context,2)

        recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = noteAdapter

            /**
             * 监听滚动事件，实现加载更多功能
             */
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = gridLayoutManager.childCount
                    val totalItemCount = gridLayoutManager.itemCount
                    val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()


                    if (!isLoading // 确保不在加载中
                        && visibleItemCount > 0 && // 确保有可见项
                        visibleItemCount + firstVisibleItemPosition >= totalItemCount  // 确保滚动到了最后一项
                        ) { // 确保是向下滚动
                        loadNotes(true)
                    }
                }
            })

        }
    }

        /**
         * 初始化SwipeRefreshLayout，设置刷新监听器
         */
    private fun setupSwipeRefresh(){
        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 0
            noteList.clear()
            noteAdapter.notifyDataSetChanged()
            loadNotes(false)
        }
    }


    /**
     * 加载笔记数据，支持刷新和加载更多两种模式
     * @param isLoadMore true表示加载更多，false表示刷新数据
     */
    private fun loadNotes(isLoadMore: Boolean) {
        if (isLoading) return

        isLoading = true
        if (!isLoadMore) {
            // 如果不是加载更多，则显示刷新动画
            swipeRefreshLayout.isRefreshing = true
        }
        // 使用协程替代Thread
        lifecycleScope.launch {
            try {// 模拟延迟
                delay(1500)
                val newNotes = if (isLoadMore) {
                    currentPage++
                    // 从数据库中查询id大于等于当前最大id的note
                    database.noteDao().getNotesAfterId(noteList.lastOrNull()?.noteId ?: 0)
                } else {
                    currentPage = 0
                    // 从数据库中查询前20条note
                    database.noteDao().getNext20Notes()
                }

                // 更新UI
                if (isLoadMore) {
                    val startPosition = noteList.size
                    noteList.addAll(newNotes)
                    noteAdapter.notifyItemRangeInserted(startPosition, newNotes.size)
                } else {
                    noteList.clear()
                    noteList.addAll(newNotes)
                    noteAdapter.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
                swipeRefreshLayout.isRefreshing = false
            }
        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

}