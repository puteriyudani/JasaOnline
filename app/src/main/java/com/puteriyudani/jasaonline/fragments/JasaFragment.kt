package com.puteriyudani.jasaonline.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.puteriyudani.jasaonline.R
import com.puteriyudani.jasaonline.activities.AddJasaActivity
import com.puteriyudani.jasaonline.activities.EditJasaActivity
import com.puteriyudani.jasaonline.adapters.JasaAdapter
import com.puteriyudani.jasaonline.helpers.Config
import com.puteriyudani.jasaonline.helpers.SessionHandler
import com.puteriyudani.jasaonline.models.Jasa
import com.puteriyudani.jasaonline.models.JasaResponse
import com.puteriyudani.jasaonline.services.JasaService
import com.puteriyudani.jasaonline.services.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_jasa.view.*
import retrofit2.Call
import retrofit2.Response

class JasaFragment : Fragment() {
    lateinit var rvData: RecyclerView
    lateinit var session : SessionHandler
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = SessionHandler(requireContext())
        val rootView = inflater.inflate(R.layout.fragment_jasa, container,
            false)
        rvData = rootView.findViewById(R.id.rvData)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rvData.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        // memberikan listener onClick pada fabMain, saat diclick maka akan pindah ke activity Upload.
        view.fabAddJasa.setOnClickListener {
            val intent = Intent(context, AddJasaActivity::class.java)
            startActivity(intent)
        }
    }
    // override method dari onresume
    override fun onResume() {
        super.onResume()
        loadService()
    }
    private fun loadService() {
        val loading = ProgressDialog(context)
        loading.setMessage("Meload Jasa Pengguna...")
        loading.show()
        val jasaService: JasaService =
            ServiceBuilder.buildService(JasaService::class.java)
        val requestCall: Call<JasaResponse> =
            jasaService.getJasaUser(session.getUserId())
        requestCall.enqueue(object : retrofit2.Callback<JasaResponse>{
            override fun onFailure(call: Call<JasaResponse>, t: Throwable)
            {
                loading.dismiss()
                Toast.makeText(context, "Error terjadi ketika sedang mengambil data jasa: " + t.toString(), Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<JasaResponse>,
                response: Response<JasaResponse>
            ) {
                loading.dismiss()
                if(!response.body()?.error!!) {
                    val serveicesResponse: JasaResponse? = response.body()
                    serveicesResponse?.let {
                        val daftarJasa: List<Jasa> =
                            serveicesResponse.data
                        val jasaAdapter = JasaAdapter(daftarJasa) {
                                service ->
                            //Toast.makeText(context, "service clicked ${service.namaJasa}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context,
                                EditJasaActivity::class.java)
                            intent.putExtra(Config.EXTRA_JASA, service)
                            startActivity(intent)
                        }
                        jasaAdapter.notifyDataSetChanged()
                        rvData.adapter = jasaAdapter
                    }
                }else{
                    Toast.makeText(context, "Gagal menampilkan data jasa:" + response.body()?.message, Toast.LENGTH_LONG).show()
                }
            }
        });
    }
}