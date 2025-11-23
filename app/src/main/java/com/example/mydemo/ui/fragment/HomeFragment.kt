package com.example.mydemo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydemo.R
import com.example.mydemo.data.model.Note


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var NoteAdapter: NoteAdapter
    private val postList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSampleData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        NoteAdapter = NoteAdapter(postList)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2) // 两列网格布局
            adapter = NoteAdapter
        }
    }

    private fun initializeSampleData() {
        // 添加一些示例数据
        postList.add(Note(1, 1, "Beautiful Sunset", "Amazing sunset view from the beach", 25, 5))
        postList.add(Note(2, 2, "Delicious Food", "Tried this amazing pasta today", 42, 8))
        postList.add(Note(3, 3, "Mountain Hiking", "Great hiking experience in the mountains", 30, 3))
        postList.add(Note(4, 4, "City Lights", "Night view of the city skyline", 57, 12))
        postList.add(Note(5, 5, "Beach Day", "Perfect day at the beach with friends", 38, 6))
        postList.add(Note(6, 6, "Art Exhibition", "Visited the modern art museum today", 19, 2))
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

}