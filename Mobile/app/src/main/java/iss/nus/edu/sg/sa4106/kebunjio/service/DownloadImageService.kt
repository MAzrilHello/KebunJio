package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

class DownloadImageService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action.equals("download_file") || intent.action.equals("download_file_id")) {

            val returnIntent = Intent()

            val url = intent.getStringExtra("url")
            Log.d("DownloadImageService","url is ${url}")

            val isId = intent.action.equals("download_file_id")
            Log.d("DownloadImageService","isId is ${isId}")

            val returnBitmap = intent.getBooleanExtra("returnBitmap",false)

            var id = -1
            if (isId) {
                id = intent.getIntExtra("id",-1)
                returnIntent.putExtra("id",id)

                returnIntent.setAction("download_completed_id")

                Log.d("DownloadImageService","id is ${id}")
            } else {
                returnIntent.setAction("download_completed")
            }

            if (url != null) {
                startImageDownload(url, returnIntent, returnBitmap)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun startImageDownload(imgURL: String, returnIntent: Intent, returnBitmap: Boolean) {
        Thread {
            if (downloadImage(imgURL, returnBitmap, returnIntent)) {
                Log.d("DownloadImageService","Sending broadcast")
                sendBroadcast(returnIntent)
            }
        }.start()
    }

    private fun downloadImage(imgURL: String, returnBitmap: Boolean, intent: Intent): Boolean {
        var conn : HttpURLConnection? = null

        try {

            conn = makeConnection(imgURL)

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("DownloadImageService","Connection made")
                if (returnBitmap) {
                    val bitmap = outputToBitmap(conn)
                    Log.d("DownloadImageService","Bitmap Downloaded")
                    val byteArrayOutputStream = ByteArrayOutputStream()

                    if(imgURL.contains(".png")){
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    }
                    else{
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    }
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    Log.d("DownloadImageService","Bitmap Byte Array Created")
                    intent.putExtra("bitmapByteArray",byteArray)
                    Log.d("DownloadImageService","Bitmap Byte Array Put Extra Successful")


                } else {
                    val ext = getFileExtension(imgURL)
                    val file = createDestFile(ext)
                    outputToFile(conn, file)
                    intent.putExtra("filename", file.absolutePath)
                }

            }

            return true
        } catch (e: Exception) {
            return false
        } finally {
            conn?.disconnect()
        }
    }

    private fun outputToBitmap(conn: HttpURLConnection): Bitmap {
        val inp = conn.inputStream
        val bitmap: Bitmap = BitmapFactory.decodeStream(inp)
        return bitmap
    }


    private fun outputToFile(conn: HttpURLConnection, file: File) {
        val inp = conn.inputStream
        val outp = FileOutputStream(file)

        // for storing incoming raw bytes from input-stream
        val buf = ByteArray(4096)

        // read in first 4096 bytes
        var bytesRead = inp.read(buf)

        while (bytesRead != -1) {   // -1 means no more bytes to read
            // write to file the number of bytes read
            outp.write(buf, 0, bytesRead)

            // read from input-stream again
            bytesRead = inp.read(buf)
        }

        inp.close()
        outp.close()
    }

    private fun makeConnection(imgURL: String): HttpURLConnection {
        var conn : HttpURLConnection? = null

        val url = URL(imgURL)

        conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET" // optional; default is GET

        return conn
    }

    public fun getFileExtension(str: String) : String {
        return str.substring(str.lastIndexOf("."))
    }

    private fun createDestFile(ext: String) : File {
        // using random string as filename to prevent filename clashes
        val filename = UUID.randomUUID().toString() + ext

        // storing under /files/PICTURES folder
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File(dir, filename)
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
}