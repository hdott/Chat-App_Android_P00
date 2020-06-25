import java.net.InetAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.http.HttpClient
import java.net.http.WebSocket



fun main(args: Array<String>) {
    val client = Socket("127.0.0.1", 3000)

    client.outputStream.write("Hello".toByteArray())
    client.close()
}

//data class MyModel(
//    val title: String
//)
//    fun toString() : String {
//
//
//}
