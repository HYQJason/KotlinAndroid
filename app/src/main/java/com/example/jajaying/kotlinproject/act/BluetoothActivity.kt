package com.example.jajaying.kotlinproject.act

import android.bluetooth.*

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import com.example.jajaying.kotlinproject.R
import java.util.ArrayList
import com.tbruyelle.rxpermissions2.RxPermissions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import com.example.jajaying.kotlinproject.runtimepermissions.PermissionsManager
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.jajaying.kotlinproject.R.id.bluetoothRecycleView
import com.example.jajaying.kotlinproject.adapter.TestBluetoothAdapter
import com.example.jajaying.kotlinproject.bean.BluetoothDeviceBean
import kotlinx.android.synthetic.main.activity_bluetooth.*
import java.lang.ref.WeakReference

/**
 *
 * 蓝牙4.0于2010年发布，相对于上个版本3.0，它的特点是更省电、成本低 延迟低等特点，
 * 现在最新的蓝牙协议是2013年底发布的蓝牙4.1，蓝牙4.1在4.0 基础上进行升级，使得可穿戴设备的批量数据传输速度更高。
 * Android是从4.3 才开始提供BLE API,这也就限定了BLE的应用只能运行在Android 4.3及其以上 的系统。
 * 在Android平台上的蓝牙4.0主要有两种工作模式:经典蓝牙(classic bluetooth) 、低功耗蓝牙(bluetooth low energy,缩写为BLE)
 */
class BluetoothActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    private var mBluetoothAdapter: BluetoothAdapter? = null


    private var mbluetoothManager: BluetoothManager? = null

    private var mbluetoothscan: BluetoothLeScanner? = null

    private var REQUEST_ENABLE_BT: Int = 1999

    private var resultslist = ArrayList<BluetoothDevice>()

    private var mReceiver: BlueToothStateReceiver? = null


    private var mList = ArrayList<String>()

    /**
     * getBondedDevices()获得
    已经配对的蓝牙设备列表
     * */

    private val bluetoothLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    private val bluetoothAdapter: TestBluetoothAdapter by lazy {
        TestBluetoothAdapter(mList)
    }

    private val rxPermissions: RxPermissions by lazy {

        RxPermissions(this)
    }

    val handler = WeakReferenceHandler(this)

    class WeakReferenceHandler(activity: BluetoothActivity) : Handler() {
        private val reference: WeakReference<BluetoothActivity>


        init {
            reference = WeakReference<BluetoothActivity>(activity)
        }

        override fun handleMessage(msg: Message) {
            Log.d("aaa", msg.toString())
            val bluetoothActivity = reference.get() as BluetoothActivity
            when (msg.what) {
                1 -> {
                    if (bluetoothActivity != null) {
                        val device: String = msg.obj as String
                        if (device != null) {
                            if (!bluetoothActivity.mList.contains(device)) {
                                bluetoothActivity.mList.add(device)
                                bluetoothActivity.bluetoothAdapter.notifyDataSetChanged()
                            }
                        }

                    }
                }
                3 -> {
                    bluetoothActivity.mBluetoothAdapter!!.cancelDiscovery()
                }
                else -> {

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        bluetoothRecycleView.run {
            bluetoothRecycleView.layoutManager = bluetoothLayoutManager
            adapter = bluetoothAdapter
        }
        bluetoothAdapter.run {
            bluetoothAdapter.bindToRecyclerView(bluetoothRecycleView)
            bluetoothAdapter.setOnItemClickListener(itemClickListener)
        }
        mbluetoothManager = this.getSystemService(android.content.Context.BLUETOOTH_SERVICE) as BluetoothManager?

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            mbluetoothManager?.let {
                mBluetoothAdapter = it.adapter
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mbluetoothManager?.let {
                mBluetoothAdapter = it.adapter
            }

            mBluetoothAdapter?.let {
                if (!it.isEnabled) {
                    it.disable()
                }


                mbluetoothscan = it.bluetoothLeScanner
            }

        } else {
            Log.d(TAG, "00000000000000")
        }


    }

    override fun onStop() {
        super.onStop()
        mBluetoothAdapter?.let {
            if (it.isDiscovering()) {
                it.cancelDiscovery()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    if (isregisterRec){
        unregisterReceiver(mReceiver)
    }

    }
    var isregisterRec=false
    private fun registerRec() {
        isregisterRec=true
        mReceiver = BlueToothStateReceiver()
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)//搜多到蓝牙
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)//搜索结束
        registerReceiver(mReceiver, filter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "requestCode==" + requestCode.toString())
        Log.d(TAG, "resultCode==" + resultCode.toString())
    }

    @RequiresApi(Build.VERSION_CODES.ECLAIR)
     fun openbluetooth(view: View) {
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        }
    }

    fun endbluetooth(view: View){
        if (mBluetoothAdapter!!.isEnabled){
            mBluetoothAdapter!!.disable()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun stopscan(view: View) {
        Log.d(TAG, "STOP====")
        mbluetoothscan!!.stopScan(scanCallback)

        /*  mBluetoothAdapter?.let {
              if ( it.isDiscovering()){
                  it.cancelDiscovery()
              }
          }*/

    }
var newgatt :BluetoothGatt?=null
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    internal var itemClickListener: BaseQuickAdapter.OnItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
     var   mac = resultslist.get(position).address
      var  mDevice = mBluetoothAdapter!!.getRemoteDevice(mac)

        newgatt =  mDevice.connectGatt(this, false, connectcallback)


          //  resultslist.get(position).createRfcommSocketToServiceRecord(resultslist.get(position).uuids.)



    }
    internal var connectcallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.d(TAG, "onConnectionStateChange")
            Log.d(TAG,"status======"+status)
            Log.d(TAG,"newState======"+newState)
            if (newState==BluetoothProfile.STATE_CONNECTED){
                newgatt!!.discoverServices()
            }


        }
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Log.d(TAG, "onServicesDiscovered")
            if (status==BluetoothGatt.GATT_SUCCESS){
            var  gattServiceList: List<BluetoothGattService>  = newgatt!!.services
                for (gattService in gattServiceList) {
                 var  gattCharacteristicList:  List<BluetoothGattCharacteristic> =   gattService.characteristics
                    for (characteristic in gattCharacteristicList) {
                    var  charpro=   characteristic.properties
                        if (BluetoothGattCharacteristic.PROPERTY_READ>0){

                        }
                    }
                }
            }
        }
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            Log.d(TAG, "onCharacteristicChanged")
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            Log.d(TAG, "onCharacteristicRead")

        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(TAG, "onCharacteristicWrite")
        }



        override fun onPhyRead(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyRead(gatt, txPhy, rxPhy, status)
            Log.d(TAG, "onPhyRead")
        }

        override fun onPhyUpdate(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
            Log.d(TAG, "onPhyUpdate")
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
            Log.d(TAG, "onReliableWriteCompleted")
        }



    }
    internal var scanCallback: ScanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            Log.d(TAG, "onBatchScanResults")


        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(TAG, "onScanFailed===" + errorCode)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d(TAG, "onScanResult//" + "callbackType=========" + callbackType + "//ScanResult==" + result.toString())
            val message = handler.obtainMessage()
            message.what = 1
            if (result!!.device.name != null) {
                message.obj = result.device.name
                val device = mBluetoothAdapter!!.getRemoteDevice(result!!.device.address)
                if (!resultslist.contains(device)) {
                    resultslist.add(device)
                    Log.d(TAG,"add")
                }
                handler.sendMessage(message)
            }
        }
    }


    // mBluetoothAdapter.startLeScan()//扫描BLE蓝牙设备,对于4.3以上的系统，直接调用
//在5.0以上的系统中是使用BluetoothLeScanner的startScan(ScanCallbackcallback),回调也是ScanCallback了
    internal var callback: BluetoothAdapter.LeScanCallback = BluetoothAdapter.LeScanCallback { bluetoothDevice, i, bytes ->
        Log.d(TAG, "扫描回调====" + bluetoothDevice.name)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun scan(view: View) {
        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return
        }

        if (PermissionsManager.getInstance().hasPermission(this, "android.permission.BLUETOOTH")) {
            Log.d(TAG, "有权限开始扫描" + mBluetoothAdapter)
//StartLeScan低功耗蓝牙
            mbluetoothscan!!.startScan(scanCallback)

            handler.postDelayed(Runnable {
                mbluetoothscan!!.stopScan(scanCallback)
                registerRec()
                //传统蓝牙
                mBluetoothAdapter!!.startDiscovery()
                handler.sendEmptyMessageDelayed(3, 10000)
            }, 10000)

        }
    }

    /**  Kotlin中的内部类与嵌套类的定义，跟Java并没有什么不同，区别在于在没有任何修
    饰的情况下，定义在另一个类内部的类将被默认为嵌套类，不持有外部类的引用，如果要将
    它声明为一个内部类，则需要加上inner修饰符，代码如下：*/
    inner class BlueToothStateReceiver : BroadcastReceiver() {


        override fun onReceive(context: Context, intent: Intent) {

            val action = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val message = handler.obtainMessage()
                    message.what = 1
                    if (device.name != null) {
                        message.obj = device.name
                        handler.sendMessage(message)
                        if (!resultslist.contains(device)) {
                            resultslist.add(device)
                            Log.d(TAG,"Receiver------------add")
                        }
                    }


                    Log.d("BlueToothStateReceiver", "找到设备=======" + device)

                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> Log.d("BlueToothStateReceiver", "搜索结束")
            }
        }
    }

}
