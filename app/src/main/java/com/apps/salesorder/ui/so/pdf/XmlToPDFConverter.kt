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
import android.util.Log
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
import com.gkemon.XMLtoPDF.model.SuccessResponse

import android.widget.Toast

import com.gkemon.XMLtoPDF.model.FailureResponse

import com.gkemon.XMLtoPDF.PdfGeneratorListener

import com.gkemon.XMLtoPDF.PdfGenerator




class XmlToPDFConverter {


    fun createPdf(
        context: Context,
        soHeader: SoHeader,
        soDetail: ArrayList<SoDetail>
    ) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_pdf_page, null)

        val dataAdapter = DetailPdfAdapter(soDetail,context)

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
        generatePdf(context, view)
    }

    fun generatePdf(context: Context, finalInvoiceViewToPrint: View) {
        PdfGenerator.getBuilder()
            .setContext(context)
            .fromViewSource()
            .fromView(finalInvoiceViewToPrint) /* "fromLayoutXML()" takes array of layout resources.
                 * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */
            .setDefaultPageSize(PdfGenerator.PageSize.A5) /* It takes default page size like A4,A5. You can also set custom page size in pixel
                 * by calling ".setCustomPageSize(int widthInPX, int heightInPX)" here. */
            .setFileName("so-invoice") /* It is file name */
            .setFolderName("so-invoice-folder/") /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
                 * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
                 * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
            //.openPDFafterGeneration(true) /* It true then the generated pdf will be shown after generated. */
            .build(object : PdfGeneratorListener() {
                override fun onFailure(failureResponse: FailureResponse) {
                    super.onFailure(failureResponse)
                    Log.d("INVOICE", "onFailure: " + failureResponse.errorMessage)
                    /* If pdf is not generated by an error then you will findout the reason behind it
                         * from this FailureResponse. */
                    //Toast.makeText(MainActivity.this, "Failure : "+failureResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(
                        context,
                        "" + failureResponse.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun showLog(log: String) {
                    super.showLog(log)
                    Log.d("INVOICE", "log: $log")
                    /*It shows logs of events inside the pdf generation process*/
                }

                override fun onStartPDFGeneration() {}
                override fun onFinishPDFGeneration() {}
                override fun onSuccess(response: SuccessResponse) {
                    super.onSuccess(response)
                    /* If PDF is generated successfully then you will find SuccessResponse
                         * which holds the PdfDocument,File and path (where generated pdf is stored)*/
                    //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.d("INVOICE", "Success: " + response.path.toString())
                    renderPdf(context, response.path.toString())
                }
            })
    }


    private fun renderPdf(context: Context, filePath: String) {
        val intent = Intent(context, ViewPDFActivity::class.java)
        intent.putExtra("path", filePath)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }
}