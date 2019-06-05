import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.util.*

fun main() {
    val myWebServer = MockWebServer()
    val hostname = myWebServer.hostName
    val port = myWebServer.port
    val wsUrl = "ws://$hostname:$port/"
    println("$hostname : $port")

    myWebServer.enqueue(MockResponse().withWebSocketUpgrade(object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            println("Server onClosed")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            println("Server onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            println("Server onFailure")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            println("Server onMessage")
            println("Message from Client $text")
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            println("Server onOpen")
            val serverText = Scanner(System.`in`).next()
            webSocket.send(serverText)
        }
    }))

    val client = OkHttpClient.Builder().build()
    val request = Request.Builder()
        .url(wsUrl)
        .build()

    client.newWebSocket(request, object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            println("Client onClosed")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            println("Client onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            println("Client onFailure")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            println("Client onMessage")
            println("Message from Server $text")
            webSocket.close(1000, null)
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            println("Client onOpen")
            val clientText = Scanner(System.`in`).next()
            webSocket.send(clientText)
        }
    })
}