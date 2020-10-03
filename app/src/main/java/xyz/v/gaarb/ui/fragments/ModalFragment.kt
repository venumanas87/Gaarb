package xyz.v.gaarb.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import xyz.v.gaarb.R


class ModalFragment : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.bottom_sheet_payment, null)
        dialog.setContentView(contentView)
    }
}