package com.puteriyudani.jasaonline.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.puteriyudani.jasaonline.R
import com.puteriyudani.jasaonline.activities.EditProfileActivity
import com.puteriyudani.jasaonline.helpers.SessionHandler
import com.puteriyudani.jasaonline.models.User
import kotlinx.android.synthetic.main.fragment_profile.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container,
            false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionHandler(requireContext())
        val user: User? = session.getUser()
        val titikDua = ": "
        if(user != null) {
            view.tvNama.text = titikDua + user.nama
            view.tvTanggalLahir.text = titikDua + user.tanggalLahir
            view.tvJenisKelamin.text = titikDua + user.jenisKelamin
            view.tvNomorHP.text = titikDua + user.nomorHP
            view.tvAlamat.text = titikDua + user.alamat
            view.tvEmail.text = titikDua + user.email
            view.tvWaktuSesi.text = titikDua + session.getExpireTime()
        }

        view.btnEditProfil.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}