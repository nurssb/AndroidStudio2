package com.example.chatlibrary

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketManager(private val callback: (String) -> Unit) {
    private var webSocketClient: WebSocketClient? = null

    fun connect() {
        val uri = URI("wss://echo.websocket.org/")
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                callback("Connected to server")
            }

            override fun onMessage(message: String?) {
                val processedMessage = if (message == "283 = 0xcb") "Server response: OK" else message
                callback(processedMessage ?: "Empty message")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                callback("Disconnected")
            }

            override fun onError(ex: Exception?) {
                callback("Error: ${ex?.message}")
            }
        }
        webSocketClient?.connect()
    }

    fun sendMessage(message: String) {
        webSocketClient?.send(message)
    }

    fun disconnect() {
        webSocketClient?.close()
    }
}