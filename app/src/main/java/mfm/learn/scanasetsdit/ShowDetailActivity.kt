package mfm.learn.scanasetsdit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class ShowDetailActivity : AppCompatActivity() {
    lateinit var imageLoad: GifImageView
    lateinit var detailTextView: TextView
    lateinit var imageDetailView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)
        imageLoad = findViewById(R.id.loadImage)
        detailTextView = findViewById(R.id.detailView)
        imageDetailView = findViewById(R.id.imageView2)

        val getDetailAset = getDetail (imageLoad,detailTextView,imageDetailView)
        getDetailAset.execute()
    }
}

class getDetail (var imageLoad:GifImageView , var detailView:TextView,var imageDetailView:ImageView) : AsyncTask <String,String,String>(){
    override fun onPreExecute() {
        super.onPreExecute()
        imageLoad.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: String?): String {
        val url = URL (Izin.LINK_DETAIL).readText()
        return url
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val jsonParse = JSONObject (result)
        val id = jsonParse.get("id")
        val keterangan = jsonParse.get("keterangan")
        val golongan = jsonParse.get("golongan")
        val kategori = jsonParse.get("kategori")
        val kelompok = jsonParse.get("kelompok")
        val subkelompok = jsonParse.get("sub_kelompok")
        val imageUrl = jsonParse.get("img")

        val resultDetail = "id\t: $id\n" +
                "Keterangan\t: $keterangan\n" +
                "Golongan\t: $golongan\n" +
                "Kategori\t: $kategori\n" +
                "Kelompok\t: $kelompok\n" +
                "Sub Kelompok\t: $subkelompok\n"
        imageLoad.visibility = View.GONE
        detailView.text = resultDetail

        val downloadImage = DownloadImage (imageUrl.toString(),imageDetailView)
        downloadImage.execute()
    }
}

class DownloadImage (val urlImage:String,var imageLoad:ImageView ) : AsyncTask <String,Void, Bitmap?>(){
    override fun doInBackground(vararg params: String?): Bitmap? {
        return try {
            val inputStream: InputStream = URL (urlImage).openStream()
            BitmapFactory.decodeStream(inputStream)
        }
        catch (e : Exception){
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result  != null ){
            imageLoad.setImageBitmap(result)
        }
    }
}