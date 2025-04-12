package com.example.chatlibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlibrary.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var webSocketManager: WebSocketManager
    private val messages = mutableListOf<String>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWebSocket()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter(messages)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupWebSocket() {
        webSocketManager = WebSocketManager { message ->
            runOnUiThread {
                messages.add("Server: $message")
                adapter.notifyItemInserted(messages.size - 1)
                binding.recyclerView.smoothScrollToPosition(messages.size - 1)
            }
        }
        webSocketManager.connect()
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty()) {
                messages.add("You: $message")
                adapter.notifyItemInserted(messages.size - 1)
                webSocketManager.sendMessage(message)
                binding.etMessage.text.clear()
            }
        }
    }

    override fun onDestroy() {
        webSocketManager.disconnect()
        super.onDestroy()
    }
}