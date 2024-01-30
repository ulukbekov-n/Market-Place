package com.example.marketplace.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.newapptester1.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(onSendClick:(String)-> Unit){

    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog,null)

    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val inpEmail = view.findViewById<EditText>(R.id.resetPasswordEmail)
    val btnSend =view.findViewById<Button>(R.id.btnSendToEmail)
    val btnCancel = view.findViewById<Button>(R.id.btnCancel)

    btnSend.setOnClickListener{
        val email = inpEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }
    btnCancel.setOnClickListener{
        dialog.dismiss()
    }
}