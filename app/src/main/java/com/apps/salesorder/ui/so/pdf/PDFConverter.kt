package com.apps.salesorder.ui.so.pdf

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.driverasrikatara.ui.kendaraan.fragment.DetailPdfAdapter
import com.apps.salesorder.R
import com.apps.salesorder.data.model.SoDetail
import com.apps.salesorder.data.model.SoHeader
import com.apps.salesorder.helper.Utils
import java.io.File
import java.io.FileOutputStream

class PDFConverter {

    private fun createBitmapFromView(
        context: Context,
        view: View,
        soHeader: SoHeader,
        soDetail: ArrayList<SoDetail>,
        dataAdapter: DetailPdfAdapter,
        activity: Activity
    ): Bitmap {
        val compCode = view.findViewById<TextView>(R.id.company_code)
        val compName = view.findViewById<TextView>(R.id.company_name)
        val soNo = view.findViewById<TextView>(R.id.so_no)
        val status = view.findViewById<TextView>(R.id.status)
        val allQty = view.findViewById<TextView>(R.id.all_qty)
        val allSubtotal = view.findViewById<TextView>(R.id.all_subtotal)
        val allPPNAmount = view.findViewById<TextView>(R.id.all_ppn_amount)
        val subtotalEx = view.findViewById<TextView>(R.id.subtotal_ex)
        val taxableAmount = view.findViewById<TextView>(R.id.taxable_amount)
        val ppn = view.findViewById<TextView>(R.id.ppn)
        val currency = view.findViewById<TextView>(R.id.currency)
        val rate = view.findViewById<TextView>(R.id.rate)
        val localTotal = view.findViewById<TextView>(R.id.local_total)
        val total = view.findViewById<TextView>(R.id.total)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvData)
        compCode.text = soHeader.accNo
        compName.text = soHeader.companyName
        soNo.text = soHeader.soNo
        status.text = soHeader.status

        var qty = 0;
        var ppnAmount: Double = 0.0;
        soDetail.forEach {
            qty += it.qty!!
            ppnAmount += it.ppnAmount!!
        }


        allQty.text = qty.toString()
        allSubtotal.text = Utils.NUMBER.currencyFormat(soHeader.subTotal.toString())
        allPPNAmount.text = Utils.NUMBER.currencyFormat(ppnAmount.toString())
        subtotalEx.text = Utils.NUMBER.currencyFormat(soHeader.subTotal.toString())
        taxableAmount.text = Utils.NUMBER.currencyFormat(soHeader.taxableAmount.toString())
        ppn.text = Utils.NUMBER.currencyFormat(soHeader.ppn.toString())
        currency.text = soHeader.currencyCode.toString()
        rate.text = soHeader.rate.toString()
        localTotal.text = Utils.NUMBER.currencyFormat(soHeader.localTotal.toString())
        total.text = Utils.NUMBER.currencyFormat(soHeader.total.toString())

        recyclerView.adapter = dataAdapter
//        recyclerView.apply {
//            layoutManager = GridLayoutManager(context,1)
//            adapter = dataAdapter
//        }
        return createBitmap(context, view, activity)
    }

    private fun createBitmap(
        context: Context,
        view: View,
        activity: Activity,
    ): Bitmap {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return Bitmap.createScaledBitmap(bitmap, 595, 900, true)
    }

    private fun convertBitmapToPdf(bitmap: Bitmap, context: Context) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)
        val filePath = File(context.getExternalFilesDir(null), "bitmapPdf.pdf")
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
        renderPdf(context, filePath)
    }

    fun createPdf(
        context: Context,
        soHeader: SoHeader,
        soDetail: ArrayList<SoDetail>,
        activity: Activity
    ) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_pdf_page, null)

        val adapter = DetailPdfAdapter(soDetail,context)
        val bitmap = createBitmapFromView(context, view, soHeader, soDetail, adapter, activity)
        convertBitmapToPdf(bitmap, activity)
    }


    private fun renderPdf(context: Context, filePath: File) {
//        val uri = FileProvider.getUriForFile(
//            context,
//            context.applicationContext.packageName + ".provider",
//            filePath
//        )
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.setDataAndType(uri, "application/pdf")

        val intent = Intent(context, ViewPDFActivity::class.java)
        intent.putExtra("path", filePath.getAbsolutePath())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }
}