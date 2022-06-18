package com.puteriyudani.jasaonline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.puteriyudani.jasaonline.R
import com.puteriyudani.jasaonline.helpers.Config
import com.puteriyudani.jasaonline.models.Jasa
import kotlinx.android.synthetic.main.activity_detail_jasa.*

class DetailJasaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_jasa)

        val receiveJasa = intent.getSerializableExtra(Config.EXTRA_JASA) as? Jasa
        if(receiveJasa != null){
            tvNamaJasa.setText(receiveJasa.namaJasa)
            tvPenyedia.setText(receiveJasa.namaPenyedia)
            tvKontak.setText(receiveJasa.nomorHP)
            tvDeskripsiSingkat.setText(receiveJasa.deskripsiSingkat)
            tvUraianDeskripsi.setText(receiveJasa.uraianDeskripsi)
            tvRating.text = receiveJasa.rating.toString()
            Glide.with(this)
                .load(Config.IMAGE_URL + receiveJasa.gambar)
                .into(imgJasa)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        openMain()
        return true
    }
    override fun onBackPressed() {
        moveTaskToBack(true)
        openMain()
    }
    private fun openMain(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(Config.EXTRA_FRAGMENT_ID, R.id.nav_beranda)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}