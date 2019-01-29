package jdp.pocketlib.widget
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.TabHost
import jdp.pocketlib.R
import java.util.*




class DateRangePickerFragmentDialog : DialogFragment(), View.OnClickListener {

    private var onDateRangeSelectedListener: OnDateRangeSelectedListener? = null

    private var tabHost: TabHost? = null
    private var startDatePicker: DatePicker? = null
    private var endDatePicker: DatePicker? = null
    private var butSetDateRange: Button? = null
    fun initialize(callback: OnDateRangeSelectedListener) {
        onDateRangeSelectedListener = callback
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.dialog_datepicker, container, false)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        tabHost = root.findViewById(R.id.tabHost)
        butSetDateRange = root.findViewById(R.id.but_set_time_range)
        startDatePicker = root.findViewById(R.id.start_date_picker)
        endDatePicker = root.findViewById(R.id.end_date_picker)
        butSetDateRange!!.setOnClickListener(this)
        tabHost!!.findViewById<View>(R.id.tabHost)
        tabHost!!.setup()
        val startDatePage = tabHost!!.newTabSpec("start")
        startDatePage.setContent(R.id.start_date_group)
        startDatePage.setIndicator("From")

        val endDatePage = tabHost!!.newTabSpec("end")
        endDatePage.setContent(R.id.end_date_group)
        endDatePage.setIndicator("TO")

        val current= Calendar.getInstance()
        startDatePicker!!.updateDate(current.get(Calendar.YEAR),0,1)
        endDatePicker!!.updateDate(current.get(Calendar.YEAR),11,1)
        tabHost!!.addTab(startDatePage)
        tabHost!!.addTab(endDatePage)
        return root

    }

    override fun onStart() {
        super.onStart()
        if (dialog == null)
            return
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }


    fun setOnDateRangeSelectedListener(callback: OnDateRangeSelectedListener) {
        this.onDateRangeSelectedListener = callback
    }

    override fun onClick(v: View) {
        val c1 = Calendar.getInstance()
        c1.set(startDatePicker!!.year, startDatePicker!!.month, startDatePicker!!.dayOfMonth)
        val c2 = Calendar.getInstance()
        c2.set(endDatePicker!!.year, endDatePicker!!.month, endDatePicker!!.dayOfMonth)
        if (c1.timeInMillis < c2.timeInMillis) {
            dismiss()
            onDateRangeSelectedListener!!.onDateRangeSelected(c1, c2)
        } else onDateRangeSelectedListener!!.onInvalidDateRange()
    }

    interface OnDateRangeSelectedListener {
        fun onDateRangeSelected(start: Calendar, end: Calendar)
        fun onInvalidDateRange()
    }

    companion object {
        fun newInstance(callback: OnDateRangeSelectedListener): DateRangePickerFragmentDialog {
            val dateRangePickerFragment = DateRangePickerFragmentDialog()
            dateRangePickerFragment.initialize(callback)
            return dateRangePickerFragment
        }
    }

}