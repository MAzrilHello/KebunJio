package iss.nus.edu.sg.sa4106.kebunjio.features.addplant

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityAddPlantBinding
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.TimeClassHandler
import iss.nus.edu.sg.sa4106.kebunjio.service.mlModel.MlModelService
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL


class AddPlantActivity : AppCompatActivity() {

    // for the UI
    private var updatePlantId: String? = null
    private var updateUserId: String? = null

    private var _binding: ActivityAddPlantBinding? = null
    private val binding get() = _binding!!
    private lateinit var nameEditText: EditText

    private lateinit var speciesSpinner: Spinner
    private var speciesSpinnerIdxToId = mutableListOf<String>()

    private lateinit var selectImageBtn: Button
    private lateinit var addPlantBtn: Button
    private lateinit var showChosenImg: ImageView
    private lateinit var predictSpeciesText: TextView
    private lateinit var predictSpeciesBtn: Button

    lateinit var plantDateTimeHandler: TimeClassHandler
    lateinit var plantDateTimeText: TextView
    lateinit var changePlantDateBtn: Button
    lateinit var changePlantTimeBtn: Button

    lateinit var harvestDateTimeHandler: TimeClassHandler
    lateinit var harvestDateTimeText: TextView
    lateinit var changeHarvestDateBtn: Button
    lateinit var changeHarvestTimeBtn: Button

    lateinit var plantHealthText: EditText
    lateinit var diseaseText: EditText
    lateinit var harvestedSpinner: Spinner

    private var dummyData: DummyData = DummyData()

    private var harvestSpinnerOptions = mutableListOf("Harvested","Not Harvested")

    // services
    //variables for service, set bound to false
    private var svc: MlModelService? = null
    private var isBound: Boolean? = false



    //set up service to connection
    val conn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            svc = (p1 as MlModelService.LocalBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
            svc = null
        }

    }

    //create BroadcastReceiver object
    private val recv = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent){

            //get intent action
            val action = intent.action


            if(action.equals("Predict Species")){
                // need to assign the species index to the dropdown
                runOnUiThread {
                }

            } else if (action.equals("Species List")) {
                // assign the species list as dropdown values
            }
        }
    }

    // for choosing an image
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try{
            showChosenImg.setImageURI(galleryUri)
            predictSpeciesText.text = "Species: ___"
            //binding.image.setImageURI(galleryUri)
        }catch(e:Exception){
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // add binding
        _binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameEditText = binding.nameEditText
        speciesSpinner = binding.speciesSpinner
        addPlantBtn = binding.addPlantBtn
        selectImageBtn = binding.selectImageBtn
        showChosenImg = binding.showChosenImg
        predictSpeciesText = binding.predictSpeciesText
        predictSpeciesBtn = binding.predictSpeciesBtn

        plantDateTimeText = binding.plantDateTimeText
        changePlantDateBtn = binding.changePlantDateBtn
        changePlantTimeBtn = binding.changePlantTimeBtn
        plantDateTimeHandler = TimeClassHandler(plantDateTimeText,changePlantDateBtn,changePlantTimeBtn,this)

        harvestDateTimeText = binding.harvestDateTimeText
        changeHarvestDateBtn = binding.changeHarvestDateBtn
        changeHarvestTimeBtn = binding.changeHarvestTimeBtn
        harvestDateTimeHandler = TimeClassHandler(harvestDateTimeText,changeHarvestDateBtn,changeHarvestTimeBtn,this)

        plantHealthText = binding.plantHealthText
        diseaseText = binding.diseaseText
        harvestedSpinner = binding.harvestedSpinner

        setupSpeciesSpinner()

        // set harvest spinner options
        val harvestSpinAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
                                                                                android.R.layout.simple_spinner_item,
                                                                                harvestSpinnerOptions)
        harvestSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        harvestedSpinner.adapter = harvestSpinAdapter

        // for choosing an image to show
        selectImageBtn.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // predict species
        predictSpeciesBtn.setOnClickListener {
            checkAndPredictChosenImage()
        }

        addPlantBtn.setOnClickListener {
            addNewPlant()
        }

        bindNeededService()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // if updating
        updateUserId = intent.getStringExtra("userId")
        Log.d("AddPlantActivity","userId: ${updateUserId}")
        if (intent.getBooleanExtra("update",false)) {
            Log.d("AddPlantActivity","We are in update mode")
            binding.titlePart.text = "Update Plant"
            binding.addPlantBtn.text = "Update Plant"
            val plantId = intent.getStringExtra("plantId")
            Log.d("AddPlantActivity","plantId: ${plantId}")
            if (plantId != null) {
                val chosenPlant = dummyData.getPlantById(plantId)
                if (chosenPlant != null) {
                    setData(chosenPlant)
                }
            }

        }
    }


    fun setupSpeciesSpinner() {
        speciesSpinnerIdxToId.clear()
        val spinnerOptions = mutableListOf<String>()
        for (i in 0..dummyData.SpeciesDummy.size-1) {
            speciesSpinnerIdxToId.add(dummyData.SpeciesDummy[i].id)
            spinnerOptions.add(dummyData.SpeciesDummy[i].name)
        }
        // set the spinner options
        val spinAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item,
            spinnerOptions)

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        speciesSpinner.adapter = spinAdapter
    }


    public fun bindNeededService() {
        // bind service
        val intent = Intent(this@AddPlantActivity, MlModelService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    public fun setData(plant: Plant) {
        updatePlantId = plant.id
        Log.d("AddPlantActivity","set ediblePlantSpeciesId: ${plant.ediblePlantSpeciesId}")
        speciesSpinner.setSelection(speciesSpinnerIdxToId.indexOf(plant.ediblePlantSpeciesId))
        updateUserId = plant.userId
        nameEditText.setText(plant.name)
        diseaseText.setText(plant.disease)
        plantDateTimeText.text = plant.plantedDate
        harvestDateTimeText.text = plant.harvestStartDate
        plantHealthText.setText(plant.plantHealth)
        if (plant.harvested) {
            harvestedSpinner.setSelection(harvestSpinnerOptions.indexOf("Harvested"))
        } else {
            harvestedSpinner.setSelection(harvestSpinnerOptions.indexOf("Not Harvested"))
        }


    }

    private fun addNewPlant() {
        var plantId: String = "" // Must assign a proper id later
        if (updatePlantId != null) {
            plantId = updatePlantId!!
        }
        val ediblePlantSpeciesId = harvestSpinnerOptions[speciesSpinner.selectedItemPosition] // must assign a proper id later
        var userId = updateUserId // must assign a proper id later
        if (userId==null){
            userId = ""
        }
        val name = nameEditText.text.toString()
        val disease = diseaseText.text.toString()
        val plantedDate = plantDateTimeText.text.toString()
        val harvestStartDate = harvestDateTimeText.text.toString()
        val plantHealth = plantHealthText.text.toString()
        val harvested = harvestedSpinner.selectedItem.toString() == "Harvested"
        val newPlant = Plant(plantId, ediblePlantSpeciesId, userId, name,disease,plantedDate,harvestStartDate,plantHealth,harvested)
        // TODO: add the new plant
    }

    //function for subscribe to broadcast
    protected fun subscribeToActions(){
        val filter = IntentFilter()
        filter.addAction("Predict Species")
        filter.addAction("Species List")
        registerReceiver(recv, filter, RECEIVER_EXPORTED)
    }

    //function for unsubscribe to broadcast
    protected fun unsubscribeToActions(){
        unregisterReceiver(recv)
    }


    fun checkAndPredictChosenImage() {
        // Check if there is an image in the ImageView
        val drawable = showChosenImg.drawable
        if (drawable == null || drawable !is BitmapDrawable) {
            Log.d("checkAndPredictChosenImage","No image in showChosenImg. Doing nothing.")
            return
        }

        // Convert the image to a Bitmap
        val bitmap = (drawable as BitmapDrawable).bitmap

        // Convert Bitmap to ByteArray
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream) // Compress to JPEG format
        val imageData = byteArrayOutputStream.toByteArray()
        predictSpeciesText.text = "Species: AWAITING PREDICTION"
        // in thread as android does not allow api requests in main thread
        Thread{predictImage(imageData)}.start()
    }


    fun predictImage(imageData: ByteArray) {
        val boundary = "Boundary-${System.currentTimeMillis()}" // Unique boundary for multipart form
        val lineEnd = "\r\n"
        val twoHyphens = "--"
        val use_as_ip = "10.0.2.2"
        //val use_as_ip = "127.0.0.1"
        //val use_as_ip = "192.168.1.3"
        //val use_as_ip = "192.168.1.254"
        val flaskUrl = "http://$use_as_ip:5000/predictSpecies"
        Log.d("predictImage","flaskUrl: $flaskUrl")

        try {
            val url = URL(flaskUrl)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            // Set headers
            Log.d("predictImage","Set headers")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            // Create output stream
            Log.d("predictImage","Create output stream")
            val outputStream = DataOutputStream(connection.outputStream)

            // Write the image file part
            Log.d("predictImage","Write the image file part")
            outputStream.writeBytes("$twoHyphens$boundary$lineEnd")
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"$lineEnd")
            outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)

            // Write image data
            Log.d("predictImage","Write image data")
            outputStream.write(imageData)
            outputStream.writeBytes(lineEnd)

            // Write the end of the multipart request
            Log.d("predictImage","Write the end of the multipart request")
            outputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
            outputStream.flush()
            outputStream.close()

            // Get response
            Log.d("predictImage","Get response")
            val responseCode = connection.responseCode
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            val responseObject = JSONObject(responseMessage)
            val prediction = responseObject.getString("prediction")

            runOnUiThread {
                val resultText = "Species: $prediction"
                predictSpeciesText.text = resultText
            }

            Log.d("predictImage","Response Code: $responseCode")
            Log.d("predictImage","Response Message: $responseMessage")

            connection.disconnect()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("predictImage","Failed to send image to Flask API.")
            Log.d("predictImage",e.stackTraceToString())
            runOnUiThread {
                predictSpeciesText.text = "Species: ERROR"
            }
        }
    }
}