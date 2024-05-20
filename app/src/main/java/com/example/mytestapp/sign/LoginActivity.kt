package com.example.mytestapp.sign

import CriminalServicePool      //구현 필요
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mytestapp.MainActivity
import com.example.mytestapp.databinding.ActivityLoginBinding
import com.example.mytestapp.model.request.loginrequest     //구현 필요
import com.example.mytestapp.model.response.loginresponse   //구현 필요
import retrofit2.Call       //구현 필요
import retrofit2.Callback       //구현 필요
import retrofit2.Response       //구현 필요


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val LoginService = CriminalServicePool.loginService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val ID = binding.loginID
        val Password = binding.loginPassword

        binding.signupBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            if (ID.text.isNotEmpty() && Password.text.isNotEmpty()) {
                LoginService.signin(
                    loginrequest(
                        ID.text.toString(),
                        Password.text.toString()
                    )
                ).enqueue(object : Callback<loginresponse> {
                    override fun onResponse(
                        call: Call<loginresponse>,
                        response: Response<loginresponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                            val userid = response.body()?.user_id

                            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userid", userid)
                            editor.apply()

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Log.d("ffffff", response.body()?.success.toString())
                            Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<loginresponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버통신 실패", Toast.LENGTH_SHORT).show()
                    }

                })
            } else {
                if (ID.text.isEmpty()) {
                    Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}