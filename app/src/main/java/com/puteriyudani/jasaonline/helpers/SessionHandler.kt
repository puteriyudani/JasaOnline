package com.puteriyudani.jasaonline.helpers

import android.content.Context
import android.content.SharedPreferences
import com.puteriyudani.jasaonline.models.User
import java.util.*

class SessionHandler {
    private val PREF_NAME = "UserSession"
    private val KEY_ID = "id"
    private val KEY_NAMA = "nama"
    private val KEY_TANGGAL_LAHIR = "tanggal_lahir"
    private val KEY_JENIS_KELAMIN = "jenis_kelamin"
    private val KEY_NOMOR_HP = "nomor_hp"
    private val KEY_ALAMAT = "alamat"
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private val KEY_EXPIRES = "expires";
    private val KEY_EMPTY = ""
    private var mContext: Context? = null
    private var mEditor: SharedPreferences.Editor? = null
    private var mPreferences: SharedPreferences? = null
    constructor(mContext:Context){
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        this.mEditor = mPreferences?.edit();
    }
    fun saveUser(user: User) {
        mEditor!!.putInt(KEY_ID, user.id)
        mEditor!!.putString(KEY_NAMA, user.nama)
        mEditor!!.putString(KEY_TANGGAL_LAHIR, user.tanggalLahir)
        mEditor!!.putString(KEY_JENIS_KELAMIN, user.jenisKelamin)
        mEditor!!.putString(KEY_NOMOR_HP, user.nomorHP)
        mEditor!!.putString(KEY_ALAMAT, user.alamat)
        mEditor!!.putString(KEY_EMAIL, user.email)
        mEditor!!.putString(KEY_PASSWORD, user.password)
        val date = Date()
        //Set user session for next 7 days --> 7 * 24 * 60 * 60 * 1000
        val millis: Long = date.getTime() + 60 * 60 * 1000 // 1 Jam
        mEditor!!.putLong(KEY_EXPIRES, millis)
        mEditor!!.commit()
    }
    fun updateUser(user: User) {
        mEditor!!.putString(KEY_NAMA, user.nama)
        mEditor!!.putString(KEY_TANGGAL_LAHIR, user.tanggalLahir)
        mEditor!!.putString(KEY_JENIS_KELAMIN, user.jenisKelamin)
        mEditor!!.putString(KEY_NOMOR_HP, user.nomorHP)
        mEditor!!.putString(KEY_ALAMAT, user.alamat)
        mEditor!!.putString(KEY_EMAIL, user.email)
        if(!user.password.equals(""))
            mEditor!!.putString(KEY_PASSWORD, user.password)
        mEditor!!.commit()
    }
    fun isLoggedIn(): Boolean {
        val currentDate = Date()
        val millis = mPreferences!!.getLong(KEY_EXPIRES, 0)
        /* If shared preferences does not have a value
        then user is not logged in
        */
        if (millis == 0L) {
            return false
        }
        val expiryDate = Date(millis)
        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate)
    }
    fun getExpireTime(): String{
        val millis = mPreferences!!.getLong(KEY_EXPIRES, 0)
        val expiryDate = Date(millis)
        return expiryDate.toString()
    }
    fun getUserId(): Int{
        val user_id = mPreferences!!.getInt(KEY_ID, 0)
        return user_id
    }
    fun getUser(): User? {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null
        }
        val user = User()
        user.id = mPreferences!!.getInt(KEY_ID, 0)
        user.nama = mPreferences!!.getString(KEY_NAMA, KEY_EMPTY)
        user.tanggalLahir = mPreferences!!.getString(KEY_TANGGAL_LAHIR,
                KEY_EMPTY)
        user.jenisKelamin = mPreferences!!.getString(KEY_JENIS_KELAMIN,
                KEY_EMPTY)
        user.nomorHP = mPreferences!!.getString(KEY_NOMOR_HP, KEY_EMPTY)
        user.alamat = mPreferences!!.getString(KEY_ALAMAT, KEY_EMPTY)
        user.email = mPreferences!!.getString(KEY_EMAIL, KEY_EMPTY)
        user.password = mPreferences!!.getString(KEY_PASSWORD, KEY_EMPTY)
        return user
    }
    fun removeUser() {
        mEditor!!.clear()
        mEditor!!.commit()
    }
}