package com.example.mytestapp.chat

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import com.example.mytestapp.R
import com.example.mytestapp.model.request.ReportData
import com.example.mytestapp.model.response.ReportResponse
import com.example.mytestapp.service.ReportService
import com.example.mytestapp.entitiy.KiriServicePool
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.content.Context
import android.view.View


class ReportUserActivity : Activity() {
    private lateinit var reportService: ReportService
    private lateinit var currentUserId: String
    private lateinit var targetUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        targetUserId = intent.getStringExtra("targetUserId") ?: "unknown"

        initializeComponents()
        confirmAction(targetUserId, "해당 사용자를 신고하시겠습니까?", this::showReportReasonDialog)
    }

    private fun initializeComponents() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        currentUserId = sharedPreferences.getString("UserID", "unknownUserId") ?: "unknownUserId"

        // KiriServicePool에서 chatService를 가져옴
        reportService = KiriServicePool.reportService
    }

    // 사용자에게 확인 메시지를 보여주는 다이얼로그 생성
    private fun confirmAction(targetId: String, message: String, onConfirm: (String) -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_chat_confirmation, null)
        val confirmButton = dialogView.findViewById<Button>(R.id.confirm_button)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
        val confirmationMessage = dialogView.findViewById<TextView>(R.id.confirmation_message)

        confirmationMessage.text = message

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        // 사용자가 확인 버튼 클릭시 onConfirm 콜백을 호출
        confirmButton.setOnClickListener {
            onConfirm(targetId)
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    // 신고 이유를 입력받는 다이얼로그 생성
    // 사용자가 제출 버튼을 클릭하면 신고 정보를 백엔드에 전송
    private fun showReportReasonDialog(reportedId: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_report_reason, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupReasons)
        val submitButton = dialogView.findViewById<Button>(R.id.buttonSubmit)
        val cancelButton = dialogView.findViewById<Button>(R.id.buttonCancel)
        val editTextOtherReason = dialogView.findViewById<EditText>(R.id.editTextOtherReason)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioButtonOther) {
                editTextOtherReason.visibility = View.VISIBLE
            } else {
                editTextOtherReason.visibility = View.GONE
            }
        }

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // 제출 버튼 클릭 (정보 수집, 백엔드에 전송)
        submitButton.setOnClickListener {
            val selectedOption = radioGroup.checkedRadioButtonId
            val reason = if (selectedOption == R.id.radioButtonOther) {
                editTextOtherReason.text.toString()
            } else {
                dialogView.findViewById<RadioButton>(selectedOption).text.toString()
            }
            reportUser(reportedId, reason)
            alertDialog.dismiss()
        }

        // 취소 버튼 클릭 시 다이어로그를 닫음
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun reportUser(reportedId: String, reason: String) {
        val reportData = ReportData(
            reporterId = currentUserId,
            reason = reason,
            reportedId = reportedId
        )
        reportService.reportUser(reportData).enqueue(object : Callback<ReportResponse> { // 변경
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                if (response.isSuccessful) {
                    val reportResponse = response.body()
                    if (reportResponse != null && reportResponse.success) {
                        showCompletionDialog("해당 사용자가 신고되었습니다.")
                    } else {
                        Toast.makeText(this@ReportUserActivity, "신고에 실패했습니다: ${reportResponse?.error}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ReportUserActivity, "신고에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                Toast.makeText(this@ReportUserActivity, "에러: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showCompletionDialog(message: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_chat_completion, null)
        val completionMessage = dialogView.findViewById<TextView>(R.id.completion_message)
        val closeButton = dialogView.findViewById<Button>(R.id.close_button)

        completionMessage.text = message

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        closeButton.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }

        alertDialog.show()
    }
}
