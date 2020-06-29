package mfm.learn.scanasetsdit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception
import java.net.URL


class ScanBarangActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    lateinit var scannerView:ZXingScannerView
    lateinit var starterIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barang)

        starterIntent = intent
        scannerView = ZXingScannerView (this@ScanBarangActivity)
        setContentView(scannerView)


    }

    override fun handleResult(rawResult: Result?) {
        val result = "${Izin.LINK_ISTHEREARE}?id=${rawResult.toString()}"
        Izin.LINK_DETAIL = "${Izin.LINK}detail.php?id=${rawResult.toString()}"
        val getStatusAsync = getStatus (this@ScanBarangActivity,starterIntent,result,this@ScanBarangActivity)
        getStatusAsync.execute()
    }
    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler (this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.startCamera()
    }

    override fun onStop() {
        super.onStop()
        scannerView.stopCamera()
    }


}

class getStatus (var context:Context, var starterIntent:Intent , var urlParam:String ,var acvtivity:Activity): AsyncTask <String, String, String> (){
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: String?): String {


        try {
            val url = URL (urlParam).readText()
            return url
        }
        catch (e: FileNotFoundException){
            val url = "{\"status\":\"gagal\"}"
            return url
        }

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        // parse json
        val parsJson = JSONObject (result)
        var result = parsJson.get("status")

        if (result == Izin.SUKSES){
            Toast.makeText(context,"Berhasil intent ke activity lain",Toast.LENGTH_LONG).show()
            val intent = Intent(context,ShowDetailActivity::class.java)
            context.startActivity(intent)
        }
        else {
            Toast.makeText(context,"Gagal Scan Ulang",Toast.LENGTH_LONG).show()
            acvtivity.finish()
            context.startActivity(Intent(starterIntent))

        }
    }
}



