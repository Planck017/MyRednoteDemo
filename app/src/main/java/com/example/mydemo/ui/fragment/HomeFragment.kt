package com.example.mydemo.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mydemo.R
import com.example.mydemo.data.AppDatabase
import com.example.mydemo.data.api.NoteService
import com.example.mydemo.data.model.Note
import com.example.mydemo.repository.NoteRepository
import com.example.mydemo.view.NoteViewModel
import com.example.mydemo.view.NoteViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val noteList = mutableListOf<Note>()
    private lateinit var database: AppDatabase
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var noteViewModel: NoteViewModel

    private var isLoading = false

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
        // 初始化ViewModel
        val noteService = Retrofit.Builder()
            .baseUrl("http://172.21.96.1:8080/") // 替换为你的API基础URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteService::class.java)

        val noteRepository = NoteRepository(noteService)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(noteRepository))[NoteViewModel::class.java]
        // 初始化RecyclerView
        setupRecyclerView()
        // 初始化SwipeRefreshLayout
        setupSwipeRefresh()
        // 初始化数据观察者（只初始化一次）
        setupDataObserver()

        if (noteList.isEmpty()) {
            loadNotes(false)
        }
        return view
    }


    /**
     * 设置数据观察者，只在初始化时调用一次
     */
    private fun setupDataObserver() {
        noteViewModel.notes.observe(viewLifecycleOwner) { newNotes ->
            // 确保数据不为空
            if (newNotes != null) {
                noteList.clear()
                noteList.addAll(newNotes)
                noteAdapter.notifyDataSetChanged()
            }
        }
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
                        && dy > 0 // 确保是向下滚动
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
        // isLoading 确保不会重复加载
        if (isLoading) return

        isLoading = true

        // isLoadMore 为 false 显示刷新动画
        if (!isLoadMore) {
            swipeRefreshLayout.isRefreshing = true
        }


        // 使用协程替代Thread
        lifecycleScope.launch {
            try {// 模拟延迟
                delay(1500)
                // isLoadMore 为 true 加载更多数据
                if (isLoadMore){
                    if (noteList.isNotEmpty()){
                        val id = noteViewModel.getLastNoteId() ?: 0
                        Log.d("HomeFragment", "当前最后一项id为$id")
                        val nextNotes = noteViewModel.loadNextNotes(id)
                        if (nextNotes.isEmpty()){
                            Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show()
                        } else {
                            // 有更多数据，添加到列表末尾
                            val startPosition = noteList.size
                            noteList.addAll(nextNotes)
                            // viewmodel
                            noteViewModel.addNote(nextNotes)
                            // 通知适配器有新数据插入
                            noteAdapter.notifyItemRangeInserted(startPosition, nextNotes.size)
                        }
                    }
                }
                // isLoadMore 为 false 刷新数据
                else{
                    // 刷新数据，先清空旧数据
                    noteList.clear()
                    val notes = noteViewModel.loadNotes()
                    noteViewModel.clearNotes()
                    noteViewModel.addNote(notes)
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