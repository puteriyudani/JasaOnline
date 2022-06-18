package com.puteriyudani.jasaonline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.puteriyudani.jasaonline.R
import com.puteriyudani.jasaonline.helpers.Config
import com.puteriyudani.jasaonline.helpers.SessionHandler
import com.puteriyudani.jasaonline.models.DefaultResponse
import com.puteriyudani.jasaonline.models.User
import com.puteriyudani.jasaonline.services.ServiceBuilder
import com.puteriyudani.jasaonline.services.UserService
import kotlinx.android.synthetic.main.activity_edit_profile.*
import retrofit2.Call
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val session = SessionHandler(applicationContext)
        val user = session.getUser()
        if(user != null) {
            etNama.setText(user.nama)
            etTanggalLahir.setText(user.tanggalLahir)
            if(user.jenisKelamin.equals("Pria"))
                spJenisKelamin.setSelection(1)
            else
                spJenisKelamin.setSelection(2)
            etNomorHP.setText(user.nomorHP)
            etAlamat.setText(user.alamat)
            etEmail.setText(user.email)
        }
        btnSubmit.setOnClickListener {
            val id = user?.id!!
            val nama = etNama.text.toString()
            val tanggalLahir = etTanggalLahir.text.toString()
            val jenisKelamin = spJenisKelamin.selectedItem.toString()
            val nomorHP = etNomorHP.text.toString()
            val alamat = etAlamat.text.toString()
            val email = etEmail.text.toString()
            var password = etPassword.text.toString()
            val konfirmasiPassword = etKonfirmasiPassword.text.toString()
            if(TextUtils.isEmpty(nama)){
                etNama.error = "Nama tidak boleh kosong!"
                etNama.requestFocus()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(tanggalLahir)){
                etTanggalLahir.error = "Tanggal lahir tidak boleh kosong!"
                etTanggalLahir.requestFocus()
                return@setOnClickListener
            }
            if(jenisKelamin.equals("Jenis Kelamin")){
                Toast.makeText(applicationContext, "Silahkan pilih jenis kelamin!", Toast.LENGTH_SHORT).show()
                spJenisKelamin.requestFocus()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(nomorHP)){
                etNomorHP.error = "Nomor HP tidak boleh kosong!"
                etNomorHP.requestFocus()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(alamat)){
                etAlamat.error = "Alamat tidak boleh kosong!"
                etAlamat.requestFocus()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(email)){
                etEmail.error = "Email tidak boleh kosong!"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password) &&
                TextUtils.isEmpty(konfirmasiPassword)) {
                password = ""
            } else {
                if (TextUtils.isEmpty(password)) {
                    etPassword.error = "Password tidak boleh kosong!"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(konfirmasiPassword)) {
                    etKonfirmasiPassword.setError("Konfirmasi password tidak boleh kosong!")
                            etKonfirmasiPassword.requestFocus()
                        return@setOnClickListener
                }
                if (!password.equals(konfirmasiPassword)) {
                    etKonfirmasiPassword.setError("Password dan konfirmasi password tidak sama!")
                            etKonfirmasiPassword.requestFocus()
                        return@setOnClickListener
                }
            }
            val updatedUser = User(id, nama, tanggalLahir, jenisKelamin,
                nomorHP, alamat, email, password);
            val userService: UserService =
                ServiceBuilder.buildService(UserService::class.java)
            val requestCall: Call<DefaultResponse> =
                userService.updateUser(updatedUser)
            requestCall.enqueue(object :
                retrofit2.Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t:
                Throwable) {
                    Toast.makeText(this@EditProfileActivity, "Error terjadi ketika sedang mengubah data user: " + t.toString(),
                    Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(!response.body()?.error!!) {
                        val defaultResponse: DefaultResponse =
                            response.body()!!
                        defaultResponse.let {
                            session.updateUser(updatedUser)
                            Toast.makeText(this@EditProfileActivity,
                                defaultResponse.message, Toast.LENGTH_LONG).show()
                            openMain()
                        }
                    }else{
                        Toast.makeText(this@EditProfileActivity, "Gagal mengubah user: " + response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
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
        intent.putExtra(Config.EXTRA_FRAGMENT_ID, R.id.nav_profile)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}