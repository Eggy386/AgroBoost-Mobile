package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.ambient.AmbientModeSupport.AmbientCallback
import com.bharathvishal.messagecommunicationusingwearabledatalayer.databinding.ActivityMainBinding
import com.google.android.gms.wearable.*
import com.google.gson.Gson
import java.nio.charset.StandardCharsets

//import com.bharathvishal.messagecommunicationusingwearabledatalayer.data.Riego
//import com.bharathvishal.messagecommunicationusingwearabledatalayer.data.Recordatorio

class MainActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider,
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {
    private var activityContext: Context? = null

    private lateinit var binding: ActivityMainBinding

    private val TAG_MESSAGE_RECEIVED = "receive1"
    private val APP_OPEN_WEARABLE_PAYLOAD_PATH = "/APP_OPEN_WEARABLE_PAYLOAD"
    private val CULTIVO_PAYLOAD_PATH = "/cultivo-payload"
    private val RECORDATORIO_PAYLOAD_PATH = "/recordatorio-payload"
    private val RIEGO_PAYLOAD_PATH = "/riego-payload"

    private var mobileDeviceConnected: Boolean = false


    // Payload string items
    private val wearableAppCheckPayloadReturnACK = "AppOpenWearableACK"

    private val MESSAGE_ITEM_RECEIVED_PATH: String = "/message-item-received"


    private var messageEvent: MessageEvent? = null
    private var mobileNodeUri: String? = null

    private lateinit var ambientController: AmbientModeSupport.AmbientController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activityContext = this

        // Enables Always-on
        ambientController = AmbientModeSupport.attach(this)

        /*On click listener for sendmessage button
        binding.sendmessageButton.setOnClickListener {
            if (mobileDeviceConnected) {
                if (binding.messagecontentEditText.text!!.isNotEmpty()) {

                    val nodeId: String = messageEvent?.sourceNodeId!!
                    // Set the data of the message to be the bytes of the Uri.
                    val payload: ByteArray =
                        binding.messagecontentEditText.text.toString().toByteArray()

                    // Send the rpc
                    // Instantiates clients without member variables, as clients are inexpensive to
                    // create. (They are cached and shared between GoogleApi instances.)
                    val sendMessageTask =
                        Wearable.getMessageClient(activityContext!!)
                            .sendMessage(nodeId, MESSAGE_ITEM_RECEIVED_PATH, payload)

                    binding.deviceconnectionStatusTv.visibility = View.GONE

                    sendMessageTask.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("send1", "Message sent successfully")
                            val sbTemp = StringBuilder()
                            sbTemp.append("\n")
                            sbTemp.append(binding.messagecontentEditText.text.toString())
                            sbTemp.append(" (Sent to mobile)")
                            Log.d("receive1", " $sbTemp")
                            binding.messagelogTextView.append(sbTemp)

                            binding.scrollviewTextMessageLog.requestFocus()
                            binding.scrollviewTextMessageLog.post {
                                binding.scrollviewTextMessageLog.fullScroll(ScrollView.FOCUS_DOWN)
                            }
                        } else {
                            Log.d("send1", "Message failed.")
                        }
                    }
                } else {
                    Toast.makeText(
                        activityContext,
                        "Message content is empty. Please enter some message and proceed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }*/
    }

    override fun onDataChanged(p0: DataEventBuffer) {
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
    }


    @SuppressLint("SetTextI18n")
    override fun onMessageReceived(p0: MessageEvent) {
        try {
            Log.d(TAG_MESSAGE_RECEIVED, "onMessageReceived event received")
            val message = String(p0.data, StandardCharsets.UTF_8)
            val messageEventPath: String = p0.path

            Log.d(TAG_MESSAGE_RECEIVED, "onMessageReceived() A message from mobile was received:" +
                    " ${p0.requestId} ${messageEventPath} ${message}")

            when (messageEventPath) {
                APP_OPEN_WEARABLE_PAYLOAD_PATH -> {
                    // Handle connection acknowledgement
                    val nodeId = p0.sourceNodeId
                    val returnPayloadAck = wearableAppCheckPayloadReturnACK
                    val payload: ByteArray = returnPayloadAck.toByteArray()

                    // Send an acknowledgment back to the mobile device
                    val sendMessageTask = Wearable.getMessageClient(activityContext!!)
                        .sendMessage(nodeId, APP_OPEN_WEARABLE_PAYLOAD_PATH, payload)

                    sendMessageTask.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d(TAG_MESSAGE_RECEIVED, "Acknowledgement message sent successfully")
                            mobileDeviceConnected = true
                        } else {
                            Log.d(TAG_MESSAGE_RECEIVED, "Failed to send acknowledgement message")
                        }
                    }
                }
                CULTIVO_PAYLOAD_PATH -> {
                    // Handle received cultivo data
                    val gson = Gson()
                    val cultivo = gson.fromJson(message, Cultivo::class.java)
                    Log.d("CultivoMessageListener", "Received Cultivo data: $cultivo")
                    // Here you can update the UI or process the data as needed
                }
                RECORDATORIO_PAYLOAD_PATH -> {
                    val gson = Gson()
                    val recordatorio = gson.fromJson(message, RecordatorioData::class.java)
                    Log.d("RecordatorioMessageListener", "Received Recordatorio data: $recordatorio")
                    DataRepository.recordatorioList.add(recordatorio)
                }
                RIEGO_PAYLOAD_PATH -> {
                    val gson = Gson()
                    val riego = gson.fromJson(message, RiegoData::class.java)
                    Log.d("RiegoMessageListener", "Received Riego data: $riego")
                    DataRepository.riegoList.add(riego)

                    val intent = Intent(this, Inicio::class.java)
                    Log.v("Intent", "Iniciar vista")
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Log.d(TAG_MESSAGE_RECEIVED, "Unknown path: $messageEventPath")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG_MESSAGE_RECEIVED, "Exception in onMessageReceived: ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            Wearable.getDataClient(activityContext!!).removeListener(this)
            Wearable.getMessageClient(activityContext!!).removeListener(this)
            Wearable.getCapabilityClient(activityContext!!).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            Wearable.getDataClient(activityContext!!).addListener(this)
            Wearable.getMessageClient(activityContext!!).addListener(this)
            Wearable.getCapabilityClient(activityContext!!)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAmbientCallback(): AmbientCallback = MyAmbientCallback()

    private inner class MyAmbientCallback : AmbientCallback() {
        override fun onEnterAmbient(ambientDetails: Bundle) {
            super.onEnterAmbient(ambientDetails)
        }

        override fun onUpdateAmbient() {
            super.onUpdateAmbient()
        }

        override fun onExitAmbient() {
            super.onExitAmbient()
        }
    }

}