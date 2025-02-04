package iss.nus.edu.sg.sa4106.kebunjio.features.planthealthcheck

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityPlantHealthCheckBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ViewPlantDetailsActivity
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantApiService
import iss.nus.edu.sg.sa4106.kebunjio.service.mlModel.MlModelDiagnoseService
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class PlantHealthCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlantHealthCheckBinding
    //declare filename format
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val outputDirectory: File by lazy {
        retrieveOutputDirectory()
    }
    // Declare imageCapture as a class-level variable
    private var imageCapture: ImageCapture? = null
    private val apiService = PlantApiService
    private lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlantHealthCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        initButtons()
        startCamera()

        // Initialize the ActivityResultLauncher
        // () is deprecated, and the recommended way to handle activity results in Android now
        // is using the Activity Result API
        // Declare the activity result launcher for picking images from the gallery.
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Handle the result and implement the callback to receive the selected image URI.
                // Convert URI to file and pass to diagnosePlant()
                val file = getFileFromUri(it)
                if (file != null) {
                    diagnosePlant(file)
                } else {
                    showToast("Failed to get the image file.")
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun initButtons() {

        binding.captureButton.setOnClickListener {
            capturePhoto()
        }

        binding.galleryButton.setOnClickListener {
            openGallery()
        }

        binding.viewPlantsButton.setOnClickListener {
            // The below scripts are opening a new ViewPlantDetailsActivity, not going back
            // to the previous view.
            //val intent = Intent(this, ViewPlantDetailsActivity::class.java)
            //startActivity(intent)
            // 'finish' is used to close the activity
            finish()
        }
    }

//    private fun showFragment() {
//            binding.previewView.visibility = View.GONE
//            binding.captureButton.visibility = View.GONE
//            binding.galleryButton.visibility = View.GONE
//            binding.viewPlantsButton.visibility = View.GONE
//            binding.fragmentContainer.visibility = View.VISIBLE
//
//
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, PlantListFragment())
//                .addToBackStack(null)
//                .commit()
//
////            getUserPlants()
//
//        }

    private fun startCamera() {
        Log.d("PlantHealthCheckActivity", "startCamera called")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Ensure at least one camera is available
            if (cameraProvider.availableCameraInfos.isEmpty()) {
                Log.e("CameraX", "No available cameras found")
                return@addListener
            }

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
        }
            // Build the ImageCapture use case
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.previewView.display.rotation)
                .build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                // Ensure imageCapture is bound
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    //Logic for Capturing a new plant photo
    private fun capturePhoto() {
        if (imageCapture == null) {
            Log.e("PlantHealthCheckActivity", "ImageCapture is not initialized. Restarting camera.")
            showToast("Camera is not ready. Restarting...")
            startCamera() // Retry camera setup
            return
        }

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError (e: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${e.message}", e)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("PlantHealthCheckActivity", "Photo saved at: $savedUri")
//                    previewCapturedImage(savedUri)
                    diagnosePlant(photoFile)

                }
            }
        )
    }

    private fun previewCapturedImage(uri: Uri) {
        // Display captured image in ImageView or navigate to the next screen
    }

    override fun onResume() {
        super.onResume()

        // Restart the camera if the preview is visible
        if (binding.previewView.visibility == View.VISIBLE) {
            Log.d("PlantHealthCheckActivity", "Resuming activity and restarting camera")
            startCamera() // Restart camera
        }
    }

    //Logic for Open Gallery
    //Launch the gallery picker to open the gallery.
    private fun openGallery() {
        getContent.launch("image/*")
    }

    /* Deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            val selectedImageUri: Uri = data.data ?: return

            // Convert URI to file
            val file = getFileFromUri(selectedImageUri)

            if (file != null) {
                // Diagnose the selected plant
                diagnosePlant(file)
            } else {
                showToast("Failed to get the image file.")
            }
        }
    }
    */

    // Function to get a file from the URI
    private fun getFileFromUri(uri: Uri): File? {
        val file = File(cacheDir, "plant_image.jpg")  // Temporary location for the selected image
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return file
    }

    //Logic for viewing user's own plant
//    private fun getUserPlants() {
//        // Get list of plants
//        val plantList = PlantApiService.getPlants()
//        if (plantList.isNotEmpty()) {
//            updateRecyclerView(plantList)
//        } else {
//            showToast("No plants found")
//        }
//    }

//    private fun updateRecyclerView(plants: List<Plant>) {
//        val adapter = PlantsAdapter(plants)
//        binding.recyclerView.adapter = adapter
//        adapter.notifyDataSetChanged()
//    }


    private fun retrieveOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    //Permissions for Camera
    private fun checkPermissions() {
        val requiredPermissions = arrayOf(android.Manifest.permission.CAMERA)
        if (requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun diagnosePlant(imageFile: File) {
        Thread {
            val diagnosis = MlModelDiagnoseService().diagnosePlant(imageFile)
            runOnUiThread {
                if (diagnosis != null) {
                    showToast("Diagnosis: $diagnosis")
                } else {
                    showToast("Failed to diagnose plant")
                }
            }
        }.start()
    }
    //Diagnose plant using ML service
//    private fun diagnosePlant(imageFile: File) {
//        Thread {
//            try {
//                val urlString = "http://10.0.2.2:8080/api/healthcheck"
//                val boundary = "Boundary-${System.currentTimeMillis()}"
//                val lineEnd = "\r\n"
//                val twoHyphens = "--"
//
//                val url = URL(urlString)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.requestMethod = "POST"
//                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
//                connection.doOutput = true
//                connection.doInput = true
//                connection.useCaches = false
//
//                DataOutputStream(connection.outputStream).use { outputStream ->
//                    outputStream.writeBytes("$twoHyphens$boundary$lineEnd")
//                    outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${imageFile.name}\"$lineEnd")
//                    outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
//                    outputStream.writeBytes(lineEnd)
//                    outputStream.write(imageFile.readBytes())
//                    outputStream.writeBytes(lineEnd)
//                    outputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
//                    outputStream.flush()
//                }
//
//                val responseCode = connection.responseCode
//                val responseMessage = if (responseCode == HttpURLConnection.HTTP_OK) {
//                    connection.inputStream.bufferedReader().use { it.readText() }
//                } else {
//                    connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error: No response"
//                }
//
//                Log.d("PlantHealthCheckActivity", "Response Code: $responseCode")
//                Log.d("PlantHealthCheckActivity", "Response Message: $responseMessage")
//
//                runOnUiThread {
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        val responseObject = JSONObject(responseMessage)
//                        val plantHealth = responseObject.optString("plantHealth", "Unknown")
//                        showToast("Diagnosis: $plantHealth")
//                    } else {
//                        showToast("Failed to diagnose plant")
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                runOnUiThread {
//                    showToast("Error connecting to server")
//                }
//            }
//        }.start()
//    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
    //Need to understand this
    companion object {
        private const val REQUEST_GALLERY_IMAGE = 1
        private const val PERMISSIONS_REQUEST_CODE = 101
    }

}