package com.apps.salesorder.ui.so.header

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.apps.salesorder.R
import com.apps.salesorder.data.db.SoDB
import com.apps.salesorder.data.db.dao.DebtorDao
import com.apps.salesorder.data.db.dao.SoHeaderDao
import com.apps.salesorder.data.model.Branch
import com.apps.salesorder.data.model.Debtor
import com.apps.salesorder.data.model.SoHeader
import com.apps.salesorder.databinding.ActivitySoHeaderBinding
import com.apps.salesorder.helper.Utils
import com.apps.salesorder.ui.kendaraan.fragment.CariBranchFragment
import com.apps.salesorder.ui.kendaraan.fragment.CariCompCodeFragment
import com.apps.salesorder.ui.so.detail.AddDetailActivity
import com.apps.salesorder.ui.so.detail.OrderDetailActivity
import com.google.android.material.datepicker.MaterialDatePicker
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_list_so.*
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SoHeaderActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySoHeaderBinding.inflate(layoutInflater) }
    private var date_today: String = ""
    private var accNo: String = ""
    var soNo: String = ""
    var delivery1: String = ""
    var delivery2: String = ""
    var delivery3: String = ""
    var delivery4: String = ""
    var branch: String = ""
    var date: String = ""
    var salesagent: String = ""
    var refdoc: String = ""
    var cal = Calendar.getInstance()
    private lateinit var database: SoDB
    private lateinit var SoHeaderDao: SoHeaderDao
    private lateinit var DebtorDao: DebtorDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setviewModel()
        setupListener()

    }

    private fun setviewModel() {

        database = SoDB.getDatabase(this)
        SoHeaderDao = database.getSoHeader()
        DebtorDao = database.getDebtorDao()

        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date_today = current.format(formatter)
        binding.tanggal.setText(date_today)
        setTanggal()


    }

    private fun setupListener() {
        binding.companyCode.setOnClickListener {
            showDialogCariCompCode()
        }
        binding.tabAddress.setOnClickListener {
            activeTab("address")
        }
        binding.tabDelivery.setOnClickListener {
            activeTab("delivery")
        }
        binding.branch.setOnClickListener {
            showDialogCariBranch()
        }
        binding.simpan.setOnClickListener {

            val d = Utils.DATE.convertDate(binding.tanggal.text.toString()).toString();
            val count = SoHeaderDao.getCount() + 1
            soNo = "SO/"+ d +"/"+ count
            val companyName = DebtorDao.getCompName(accNo)
            date = binding.tanggal.text.toString()
            delivery1 = binding.delivery1.text.toString()
            delivery2 = binding.delivery2.text.toString()
            delivery3 = binding.delivery3.text.toString()
            delivery4 = binding.delivery4.text.toString()
            refdoc = binding.refDocNo.text.toString()

            if(branch.isEmpty()){
                Toasty.warning(this, "Branch tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if(refdoc.isEmpty()){
                Toasty.warning(this, "Ref Doc No tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                SoHeaderDao.insert(
                    SoHeader(
                        soNo = soNo,
                        accNo = accNo,
                        companyName = companyName,
                        delivery1 = delivery1,
                        delivery2 = delivery2,
                        delivery3 = delivery3,
                        delivery4 = delivery4,
                        branch = branch,
                        salesAgent = salesagent,
                        refDoc = refdoc,
                        date = date,
                        status = "draft"
                    )
                )
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra("so_no", soNo)
                intent.putExtra("acc_no", accNo)
                startActivity(intent)
            }



        }
    }

    private fun showDialogCariBranch() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val newFragment = CariBranchFragment()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        val mBundle = Bundle()
        mBundle.putString("accno", accNo)
        newFragment.arguments = mBundle

        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()

    }

    private fun activeTab(tab: String){
        if (tab.equals("address")){
            binding.tabAddress.setTextColor(Color.RED)
            binding.layoutTabAddress.visibility = View.VISIBLE

            binding.tabDelivery.setTextColor(Color.BLACK)
            binding.layoutTabDelivery.visibility = View.GONE

        } else {
            binding.tabDelivery.setTextColor(Color.RED)
            binding.layoutTabDelivery.visibility = View.VISIBLE

            binding.tabAddress.setTextColor(Color.BLACK)
            binding.layoutTabAddress.visibility = View.GONE
        }
    }

    private fun showDialogCariCompCode() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val newFragment = CariCompCodeFragment()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        val mBundle = Bundle()
        mBundle.putString("salesagent","KOSONG")
        newFragment.arguments = mBundle

        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()

    }

    fun setTanggal(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat1 = "yyyy-MM-dd" // mention the format you need
                val sdf1 = SimpleDateFormat(myFormat1, Locale.US)
                binding.tanggal.setText( sdf1.format(cal.getTime()) )
                //var tglSewa = sdf1.format(cal.getTime()).toString()

            }
        }
        binding.tanggal.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@SoHeaderActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    fun setDataFromDialogCariCompCode(result: Debtor){
        Timber.e("[DATA] ${result.accNo}")
        Timber.e("[DATA] ${result.companyName}")
        accNo = result.accNo.toString()
        salesagent = result.salesAgent.toString()
        binding.companyCode.setText(result.accNo)
        binding.companyName.setText(result.companyName)
        binding.address1.setText(result.address1)
        binding.address2.setText(result.address2)
        binding.address3.setText(result.address3)
        binding.address4.setText(result.address4)
        binding.delivery1.setText(result.deliverAddr1)
        binding.delivery2.setText(result.deliverAddr2)
        binding.delivery3.setText(result.deliverAddr3)
        binding.delivery4.setText(result.deliverAddr4)
        binding.creditTerm.setText(result.displayTerm)
        binding.salesagent.setText(result.salesAgent)
    }

    fun setDataFromDialogCariBranch(result: Branch){
        binding.branch.setText(result.branchName)
        branch = result.branchCode
    }

}