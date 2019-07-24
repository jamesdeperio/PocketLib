/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.base

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jdp.pocketlib.ext.VERBOSE

/**
 * Created by jamesdeperio on 2/13/2019
 *  jamesdeperio.github.com.codepocket.base
 */

abstract class NFCActivity : AppCompatActivity(),
        BaseContract.Common {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private val writeText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        onInitialization(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onViewDidLoad(savedInstanceState)
    }

    fun isNFCEnabled(): Boolean = nfcAdapter != null

    fun enableNFC() {
        if (nfcAdapter != null) nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) nfcAdapter!!.disableForegroundDispatch(this )
    }


    override fun onNewIntent(intent: Intent) {
        if (writeText == null)
            readTagFromIntent(intent)
    }


    @SuppressLint("MissingPermission")
    private fun readTagFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == action) {
            val rawMsgss= intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) as NdefMessage?
            val myTag:Tag?= intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag?
            if (myTag != null) {
                val sb = StringBuilder()
                for (b in myTag.id) {
                    sb.append(String.format("%02X", b))
                }
                try {
                    myTag.techList.forEach { Log.e("NFC","TAG: $it") }
                    Log.e("NFC","TAG: $sb")
                    val  ndef:Ndef?= Ndef.get(myTag)
                    if (ndef!=null ){
                        VERBOSE("MSG: ${ndef.ndefMessage}")
                        VERBOSE("ISWRITABLE: ${Ndef.get(myTag).isWritable}")
                    }
                    onTagReadListener(sb.toString())
                } catch (e:Exception){
                    e.printStackTrace()
                }

                if (rawMsgss != null) {
                    val sb1 = StringBuilder()
                    for ( x in 0 until rawMsgss.records.size)
                        sb1.append(String.format("%02X",  rawMsgss.records[x].payload))
                    VERBOSE("MESSAGE: $sb1")
                }
            }
        }
        else   VERBOSE("MESSAGE: ERROR ON NFC READ")
    }

    abstract fun onTagReadListener(tag: String)


}
