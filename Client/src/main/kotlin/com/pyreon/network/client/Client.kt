package com.pyreon.network.client
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main(){
    println("hello")
    val client = Client("localhost")
    client.connect()
    client.close()
}

class Client (host:String, port: Int = 9500){
    private val socket:Socket = Socket(host, port)
    private val inBufferedReader: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
    private val outPrintWriter: PrintWriter = PrintWriter(socket.getOutputStream(), true)

    fun connect(){
        outPrintWriter.println("fuck you server")
    }
    fun close(){
        outPrintWriter.println("disconnect\n")
        println(inBufferedReader.readLine())
        outPrintWriter.close()
        inBufferedReader.close()
        socket.close()
    }
}