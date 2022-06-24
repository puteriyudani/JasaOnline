package com.puteriyudani.jasaonline.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.puteriyudani.jasaonline.R
import com.puteriyudani.jasaonline.helpers.Config
import com.puteriyudani.jasaonline.helpers.SessionHandler
import com.puteriyudani.jasaonline.models.DefaultResponse
import com.puteriyudani.jasaonline.models.User
import com.puteriyudani.jasaonline.services.JasaService
import com.puteriyudani.jasaonline.services.ServiceBuilder
import com.vincent.filepicker.Constant.*
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.activity_add_jasa.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Response
import java.io.File


class AddJasaActivity : AppCompatActivity() {
    // global variable untuk imagename.
    lateinit var imagename: MultipartBody.Part
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jasa)
        val session = SessionHandler(applicationContext)
        val user: User? = session.getUser()
        btnCari.setOnClickListener {
        // check permission untuk android M dan ke atas.
        // saat permission disetujui oleh user maka jalan script untuk intent ke pick image.
            if(EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val i = Intent(this, ImagePickActivity::class.java)
                i.putExtra(MAX_NUMBER,1)
                startActivityForResult(i, REQUEST_CODE_PICK_IMAGE)
            }else{
                // tampilkan permission request saat belum mendapat permission dari user
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        btnAddJasa.setOnClickListener {
            val namaJasa = etNamaJasa.text.toString()
            val deskripsiSingkat = etDeskripsiSingkat.text.toString()
            val uraianDeskripsi = etUraianDeskripsi.text.toString()
            val rating = tvRating.text.toString()
            val gambar = tvImage.text.toString()
            if(TextUtils.isEmpty(namaJasa)){
                etNamaJasa.setError("Nama jasa tidak boleh kosong!")
                etNamaJasa.requestFocus()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(deskripsiSingkat)){
                etDeskripsiSingkat.setError("Deskripsi singkat tidak boleh kosong!")
                etDeskripsiSingkat.requestFocus()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(uraianDeskripsi)){
                etUraianDeskripsi.setError("Uraian deskripsi tidak boleh kosong!")
                etUraianDeskripsi.requestFocus()
                return@setOnClickListener
            }
            if(gambar.equals("Pilih sebuah gambar!")){
                Toast.makeText(applicationContext, "Silahkan pilih satu gambar, klik button Cari disamping!", Toast.LENGTH_SHORT).show()
                btnCari.requestFocus()
                return@setOnClickListener
            }
            val loading = ProgressDialog(this)
            loading.setMessage("Menambahkan Jasa...")
            loading.show()
            // add another part within the multipart request
            val reqIdUser =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user?.id.toString())
            val reqNamaJasa =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), namaJasa)
            val reqDeskripsiSingkat =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), deskripsiSingkat)
            val reqUraianDeskripsi =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), uraianDeskripsi)
            val reqRating =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), rating)
            val reqGambar =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), gambar)
            val jasaService: JasaService =
                ServiceBuilder.buildService(JasaService::class.java)
            val requestCall: Call<DefaultResponse> = jasaService.addJasa(
                imagename, reqIdUser, reqNamaJasa, reqDeskripsiSingkat,
                reqUraianDeskripsi, reqRating, reqGambar
            )
            requestCall.enqueue(object :
                retrofit2.Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t:
                Throwable) {
                    loading.dismiss()
                    Toast.makeText(this@AddJasaActivity, "Error terjadi ketika sedang menambahkan jasa: " + t.toString(),
                    Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    loading.dismiss()
                    if(!response.body()?.error!!) {
                        Toast.makeText(this@AddJasaActivity,
                            response.body()?.message, Toast.LENGTH_LONG).show()
                        openMain()
                    }else{
                        Toast.makeText(this@AddJasaActivity, "Gagal menambahkan jasa: " + response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }
            });
        }
    }
    // override method onActivityResult untuk handling data dari pickImageActivity.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data:
    Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode ==
            Activity.RESULT_OK && data != null){
            // membuat variable yang menampung path dari picked image.
            val pickedImg =
                data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)?.get(0)?.path
            // membuat request body yang berisi file dari picked image.
            val requestBody =
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    File(pickedImg))
            // mengoper value dari requestbody sekaligus membuat form data untuk upload. dan juga mengambil nama dari picked image
            imagename = MultipartBody.Part.createFormData("file", File(pickedImg).name, requestBody)
            // mempilkan image yang akan diupload dengan glide ke imgUpload.
            Glide.with(this).load(pickedImg).into(imgJasa)
            tvImage.text = File(pickedImg).name
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
        intent.putExtra(Config.EXTRA_FRAGMENT_ID, R.id.nav_jasa_pengguna)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}