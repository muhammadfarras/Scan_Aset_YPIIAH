
package mfm.learn.scanasetsdit

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var textView: TextView
    lateinit var konekButton : Button
    lateinit var buttonScan : Button




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar2)
        textView = findViewById(R.id.textView)
        konekButton = findViewById(R.id.koneksi)
        buttonScan = findViewById(R.id.buttonScan)


        // check apakah terkoneksi ke internet local
        val getdata = getInternet (this@MainActivity,progressBar,textView,konekButton)
        getdata.execute()

        konekButton.setOnClickListener {
            val getdata = getInternet (this@MainActivity,progressBar,textView,konekButton)
            getdata.execute()
        }

        buttonScan.setOnClickListener {
            // Request Camera

            when {
                ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED  -> {

//                    Intent to scanActivity
                    val intent = Intent (this@MainActivity,ScanBarangActivity::class.java)
                    startActivity(intent)
                }

                else -> {
                    // Ask permission
                    val builder : AlertDialog.Builder? = this@MainActivity?.let {
                        AlertDialog.Builder (it)
                    }
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA),Izin.KAMERA)
                }
            }
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            Izin.KAMERA -> {
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(this@MainActivity,"Dapat izin",Toast.LENGTH_SHORT).show()
                }
                else {
                    val builder : AlertDialog.Builder? = this@MainActivity?.let {
                        AlertDialog.Builder (it)
                    }

                    builder?.setMessage("Untuk melakukan scan barcode applikasi ini membutuhkan akses kamera")
                    builder?.setTitle("Butuh Izin")
                    builder?.create()
                    builder?.show()
                }
            }
        }
    }

    private class getInternet (var context: Context,
                               var progressBar: ProgressBar,
                               var textView: TextView,
                               var button: Button) : AsyncTask<String, String, String>() {

        companion object {
            val BERHASIL  = "berhasil"
            val GAGAL = "gagal"
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
            textView.visibility = View.GONE
            button.visibility = View.GONE

        }


        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL ("http://192.168.1.202/aset/index.php").readText()
                return url
            }
            catch (e:FileNotFoundException){
                val url = "{\"status\":\"gagal\"}"
                return url
            }


        }

        override fun onPostExecute(result: String?) {
            val jsonObject = JSONObject (result)

            val value = jsonObject.get("status").toString()

            if (value == getInternet.BERHASIL){
                textView.setText("Status : $value")
                textView.visibility = View.VISIBLE

            }
            else {
                textView.setText("Status : $value")
                textView.visibility = View.VISIBLE
                button.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
        }

    }


}


