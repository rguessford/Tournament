package com.pyreon.network.server

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

/**
 * Run a Test Server instance?
 */

fun main(args: Array<String>) {
    val server = Server()
    server.start()
}

class Server(port: Int = 9500) {

    data class Client(val socket:Socket, val writer:PrintWriter, val reader:BufferedReader, var state:ConnectionState = ConnectionState.NEW){
        enum class ConnectionState{
            NEW, WAITING, MSG_RECIEVED, DISCONNECTED
        }
    }

    private val clients = ArrayList<Client>()
    private val connectionListener = ConnectionListener(port, ::registerConnection)
    private val isRunning:Boolean = true
    private var connectionListenerThread = Thread(connectionListener)

    fun start() {
        connectionListenerThread.start()
        while(isRunning){
            clients.map(::processMessage)
            clients.filter{x -> x.state == Client.ConnectionState.DISCONNECTED}.map( ::disconnectClient )
            clients.removeIf{x -> x.state == Client.ConnectionState.DISCONNECTED}
        }

    }

    private fun disconnectClient(client:Client){
        println("Disconnecting client $client")
        client.writer.println("Bye")
        client.writer.close()
        client.reader.close()
        client.socket.close()
    }
    private fun processMessage(client:Client){
        if (client.reader.ready()){
            val message = client.reader.readLine()
            println("received message: $message")
            if(message == "disconnect"){
                client.state = Client.ConnectionState.DISCONNECTED
            }
        }
    }

    private fun registerConnection(socket: Socket){
        val newClient = Client(socket,
                PrintWriter(socket.getOutputStream()),
                BufferedReader(InputStreamReader(socket.getInputStream()))
        )
        clients.add(newClient)
        return
    }

    class ConnectionListener(private val port: Int, val registerConnectionCallback: (socket: Socket) -> Unit) : Runnable {

        override fun run() {
            println("start listening on port $port")
            val serverSocket = ServerSocket(port)
            while(true) {
                val socket = serverSocket.accept()
                registerConnectionCallback(socket)
            }
        }

    }
}

