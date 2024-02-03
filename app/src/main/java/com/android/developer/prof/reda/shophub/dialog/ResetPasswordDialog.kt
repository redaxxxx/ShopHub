package com.android.developer.prof.reda.shophub.dialog

import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.ResetPasswordDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String)-> Unit
){
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.etResetEmail)
    val sendBtn = view.findViewById<Button>(R.id.sendBtn)
    val cancelBtn = view.findViewById<Button>(R.id.cancelBtn)

    sendBtn.setOnClickListener{
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    cancelBtn.setOnClickListener{
        dialog.dismiss()
    }
}