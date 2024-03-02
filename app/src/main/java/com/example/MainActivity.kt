package com.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.example.databinding.ActivityMainBinding
import com.kdownloader.KDownloader
import com.kdownloader.Status
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    lateinit var dirPath: String
    private var downloadId1 = 0
    private var downloadId2 = 0
    private var downloadId3 = 0
    private var downloadId4 = 0
    private var downloadId5 = 0
    private var downloadId6 = 0

    private lateinit var kDownloader: KDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kDownloader = (applicationContext as MyApplication).kDownloader
        dirPath = Environment.getExternalStorageDirectory().path + "/Download"

        val request1 = kDownloader.newRequestBuilder(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", dirPath, "bunny.mp4",
        ).tag(TAG + "1").build()

        val request2 = kDownloader.newRequestBuilder(
            "https://ash-speed.hetzner.com/100MB.bin", dirPath, "100MB.bin",
        ).tag(TAG + "2").build()

        val request3 = kDownloader.newRequestBuilder(
            "https://filesamples.com/samples/document/pdf/sample3.pdf", dirPath, "docu.pdf",
        ).tag(TAG + "3").build()

        val request4 = kDownloader.newRequestBuilder(
            "https://media.giphy.com/media/Bk0CW5frw4qfS/giphy.gif", dirPath, "giphy.gif",
        ).tag(TAG + "4").build()

        val request5 = kDownloader.newRequestBuilder(
            "https://ash-speed.hetzner.com/1GB.bin", dirPath, "1GB.bin",
        ).tag(TAG + "5").build()

        val request6 = kDownloader.newRequestBuilder(
            "https://freetestdata.com/wp-content/uploads/2021/09/Free_Test_Data_1OMB_MP3.mp3", dirPath, "music.mp3",
        ).tag(TAG + "6").build()



        // REQUEST1

        binding.fileName1.text = "bunny.mp4"
        binding.startCancelButton1.setOnClickListener {
            if (binding.startCancelButton1.text.equals("Start")) {
                downloadId1 = kDownloader.enqueue(request1,
                    onStart = {
                        binding.status1.text = "Started"
                        binding.startCancelButton1.text = "Cancel"
                        binding.resumePauseButton1.isEnabled = true
                        binding.resumePauseButton1.visibility = View.VISIBLE
                        binding.resumePauseButton1.text = "Pause"
                    },
                    onProgress = {
                        binding.status1.text = "In Progress"
                        binding.progressBar1.progress = it
                        binding.progressText1.text = "$it%"
                    },
                    onProgressBytes = { currentBytes, totalBytes ->
                        val value = binding.progressText1.text.toString()
                        binding.progressText1.text = "$value ${getProgressDisplayLine(currentBytes, totalBytes)}"
                    },
                    onCompleted = {
                        binding.status1.text = "Completed"
                        binding.progressText3.text = "100%"
                        binding.startCancelButton1.visibility = View.GONE
                        binding.resumePauseButton1.visibility = View.GONE
                    },
                    onError = {
                        binding.status1.text = "Error : $it"
                        binding.resumePauseButton1.visibility = View.GONE
                        binding.progressBar1.progress = 0
                        binding.progressText1.text = "0%"
                    },
                    onPause = {
                        binding.status1.text = "Paused"
                    }
                )
            } else {
                kDownloader.cancel(downloadId1)
                binding.startCancelButton1.text = "Start"
            }
        }

        binding.resumePauseButton1.setOnClickListener{
            if (kDownloader.status(downloadId1) == Status.PAUSED) {
                binding.resumePauseButton1.text = "Pause"
                kDownloader.resume(downloadId1)
            } else {
                binding.resumePauseButton1.text = "Resume"
                kDownloader.pause(downloadId1)
            }
        }

        // REQUEST2

        binding.fileName2.text = "100MB.bin"
        binding.startCancelButton2.setOnClickListener {
            if (binding.startCancelButton2.text.equals("Start")) {
                downloadId2 = kDownloader.enqueue(request2,
                    onStart = {
                        binding.status2.text = "Started"
                        binding.startCancelButton2.text = "Cancel"
                        binding.resumePauseButton2.isEnabled = true
                        binding.resumePauseButton2.visibility = View.VISIBLE
                        binding.resumePauseButton2.text = "Pause"
                    },
                    onProgress = {
                        binding.status2.text = "In Progress"
                        binding.progressBar2.progress = it
                        binding.progressText2.text = "$it%"
                    },
                    onProgressBytes = { currentBytes, totalBytes ->
                        val value = binding.progressText2.text.toString()
                        binding.progressText2.text = "$value ${getProgressDisplayLine(currentBytes, totalBytes)}"
                    },
                    onCompleted = {
                        binding.status2.text = "Completed"
                        binding.progressText3.text = "100%"
                        binding.startCancelButton2.visibility = View.GONE
                        binding.resumePauseButton2.visibility = View.GONE
                    },
                    onError = {
                        binding.status2.text = "Error : $it"
                        binding.resumePauseButton2.visibility = View.GONE
                        binding.progressBar2.progress = 0
                        binding.progressText2.text = "0%"
                    },
                    onPause = {
                        binding.status2.text = "Paused"
                    }
                )
            } else {
                kDownloader.cancel(downloadId2)
                binding.startCancelButton2.text = "Start"
            }
        }

        binding.resumePauseButton2.setOnClickListener{
            if (kDownloader.status(downloadId2) == Status.PAUSED) {
                binding.resumePauseButton2.text = "Pause"
                kDownloader.resume(downloadId2)
            } else {
                binding.resumePauseButton2.text = "Resume"
                kDownloader.pause(downloadId2)
            }
        }


        // REQUEST3

        binding.fileName3.text = "docu.pdf"
        binding.startCancelButton3.setOnClickListener {
            if (binding.startCancelButton3.text.equals("Start")) {
                downloadId3 = kDownloader.enqueue(request3,
                    onStart = {
                        binding.status3.text = "Started"
                        binding.startCancelButton3.text = "Cancel"
                        binding.resumePauseButton3.isEnabled = true
                        binding.resumePauseButton3.visibility = View.VISIBLE
                    },
                    onPause = {
                        binding.status3.text = "Paused"
                    },
                    onProgress = {
                        binding.status3.text = "In Progress"
                        binding.progressBar3.progress = it
                        binding.progressText3.text = "$it%"
                    },
                    onProgressBytes = { currentBytes, totalBytes ->
                        val value = binding.progressText3.text.toString()
                        binding.progressText3.text = "$value ${getProgressDisplayLine(currentBytes, totalBytes)}"
                    },
                    onCompleted = {
                        binding.status3.text = "Completed"
                        binding.progressText3.text = "100%"
                        binding.startCancelButton3.visibility = View.GONE
                        binding.resumePauseButton3.visibility = View.GONE
                    },
                    onError = {
                        binding.status3.text = "Error : $it"
                        binding.resumePauseButton3.visibility = View.GONE
                        binding.progressBar3.progress = 0
                        binding.progressText3.text = "0%"
                    }
                )
            } else {
                kDownloader.cancel(downloadId3)
                binding.startCancelButton3.text = "Start"
            }
        }

        binding.resumePauseButton3.setOnClickListener{
            if (kDownloader.status(downloadId3) == Status.PAUSED) {
                binding.resumePauseButton3.text = "Pause"
                kDownloader.resume(downloadId3)
            } else {
                binding.resumePauseButton3.text = "Resume"
                kDownloader.pause(downloadId3)
            }
        }



        // REQUEST4


        binding.fileName4.text = "giphy.gif"
        binding.startCancelButton4.setOnClickListener {
            if (binding.startCancelButton4.text.equals("Start")) {
                downloadId4 = kDownloader.enqueue(request4,
                    onStart = {
                        binding.status4.text = "Started"
                        binding.startCancelButton4.text = "Cancel"
                        binding.resumePauseButton4.isEnabled = true
                        binding.resumePauseButton4.visibility = View.VISIBLE
                    },
                    onPause = {
                        binding.status4.text = "Paused"
                    },
                    onProgress = {
                        binding.status4.text = "In Progress"
                        binding.progressBar4.progress = it
                        binding.progressText4.text = "$it%"
                    },
                    onProgressBytes = { currentBytes, totalBytes ->
                        val value = binding.progressText4.text.toString()
                        binding.progressText4.text = "$value ${getProgressDisplayLine(currentBytes, totalBytes)}"
                    },
                    onCompleted = {
                        binding.status4.text = "Completed"
                        binding.progressText4.text = "100%"
                        binding.startCancelButton4.visibility = View.GONE
                        binding.resumePauseButton4.visibility = View.GONE
                    },
                    onError = {
                        binding.status4.text = "Error : $it"
                        binding.resumePauseButton4.visibility = View.GONE
                        binding.progressBar4.progress = 0
                        binding.progressText4.text = "0%"
                    }
                )
            } else {
                kDownloader.cancel(downloadId4)
                binding.startCancelButton4.text = "Start"
            }
        }

        binding.resumePauseButton4.setOnClickListener{
            if (kDownloader.status(downloadId4) == Status.PAUSED) {
                binding.resumePauseButton4.text = "Pause"
                kDownloader.resume(downloadId4)
            } else {
                binding.resumePauseButton4.text = "Resume"
                kDownloader.pause(downloadId4)
            }
        }



        // REQUEST 5

        binding.fileName5.text = "1GB.bin"
        binding.startCancelButton5.setOnClickListener {
            if (binding.startCancelButton5.text.equals("Start")) {
                downloadId5 = kDownloader.enqueue(request5,
                    onStart = {
                        binding.status5.text = "Started"
                        binding.startCancelButton5.text = "Cancel"
                        binding.resumePauseButton5.isEnabled = true
                        binding.resumePauseButton5.visibility = View.VISIBLE
                    },
                    onPause = {
                        binding.status5.text = "Paused"
                    },
                    onProgress = {
                        binding.status5.text = "In Progress"
                        binding.progressBar5.progress = it
                        binding.progressText5.text = "$it%"
                    },
                    onProgressBytes = { currentBytes, totalBytes ->
                        val value = binding.progressText5.text.toString()
                        binding.progressText5.text = "$value ${getProgressDisplayLine(currentBytes, totalBytes)}"
                    },
                    onCompleted = {
                        binding.status5.text = "Completed"
                        binding.progressText5.text = "100%"
                        binding.startCancelButton5.visibility = View.GONE
                        binding.resumePauseButton5.visibility = View.GONE
                    },
                    onError = {
                        binding.status5.text = "Error : $it"
                        binding.resumePauseButton5.visibility = View.GONE
                        binding.progressBar5.progress = 0
                        binding.progressText5.text = "0%"
                    }
                )
            } else {
                kDownloader.cancel(downloadId5)
                binding.startCancelButton5.text = "Start"
            }
        }

        binding.resumePauseButton5.setOnClickListener {
            if (kDownloader.status(downloadId5) == Status.PAUSED) {
                binding.resumePauseButton5.text = "Pause"
                kDownloader.resume(downloadId5)
            } else {
                binding.resumePauseButton5.text = "Resume"
                kDownloader.pause(downloadId5)
            }
        }




        // REQUEST6

        binding.fileName6.text = "music.mp3"
        binding.startCancelButton6.setOnClickListener {
            if (binding.startCancelButton6.text.equals("Start")) {
                downloadId6 = kDownloader.enqueue(request6,
                    onStart = {
                        binding.status6.text = "Started"
                        binding.startCancelButton6.text = "Cancel"
                        binding.resumePauseButton6.isEnabled = true
                        binding.resumePauseButton6.visibility = View.VISIBLE
                    },
                    onPause = {
                        binding.status6.text = "Paused"
                    },
                    onProgress = {
                        binding.status6.text = "In Progress"
                        binding.progressBar6.progress = it
                        binding.progressText6.text = "$it%"
                    },
                    onProgressBytes = { currentBytes, totalBytes ->
                        val value = binding.progressText6.text.toString()
                        binding.progressText6.text = "$value ${getProgressDisplayLine(currentBytes, totalBytes)}"
                    },
                    onCompleted = {
                        binding.status6.text = "Completed"
                        binding.progressText6.text = "100%"
                        binding.startCancelButton6.visibility = View.GONE
                        binding.resumePauseButton6.visibility = View.GONE
                    },
                    onError = {
                        binding.status6.text = "Error : $it"
                        binding.resumePauseButton6.visibility = View.GONE
                        binding.progressBar6.progress = 0
                        binding.progressText6.text = "0%"
                    }
                )
            } else {
                kDownloader.cancel(downloadId6)
                binding.startCancelButton6.text = "Start"
            }
        }

        binding.resumePauseButton6.setOnClickListener{
            if (kDownloader.status(downloadId6) == Status.PAUSED) {
                binding.resumePauseButton6.text = "Pause"
                kDownloader.resume(downloadId6)
            } else {
                binding.resumePauseButton6.text = "Resume"
                kDownloader.pause(downloadId6)
            }
        }

    }

    private fun getBytesToMBString(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%.2fMB", bytes / (1024.00 * 1024.00))
    }

    private fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String {
        return "${getBytesToMBString(currentBytes)} / ${getBytesToMBString(totalBytes)}"
    }
}