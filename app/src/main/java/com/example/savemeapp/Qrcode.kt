package com.example.savemeapp


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.databinding.DataBindingUtil
import com.example.savemeapp.databinding.FragmentQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentIntegrator
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.AccessController.checkPermission

/**
 * A simple [Fragment] subclass.
 */

private const val PERMISSION_REQUEST = 10

class Qrcode : Fragment() {

    private lateinit var binding: FragmentQrcodeBinding
    lateinit var generateStr: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_qrcode,container,false)
        return binding.root
    }

    lateinit var QRCodeImage : ImageView // the qrcode in our xml
    private var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) //for permission
    var encodedString : String = "No Information yet" // this will hold the information stored
    lateinit var buttonToScan : Button // the button to start scanning



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myhelper = DatabaseHandler(view.context)
        val dbwrite = myhelper.writableDatabase
        val dbread = myhelper.readableDatabase
        QRCodeImage = view.findViewById(R.id.qrcode)

        //generateQRCODE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (true) { //as of right now we assume that permission is true , we dont ask.
                generateQRCode()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }else{
            generateQRCode()
        }

        //To read qrCode
        buttonToScan= view.findViewById(R.id.scannerButton)

        buttonToScan.setOnClickListener {

            val scanner = IntentIntegrator(activity)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // restricting to only scanning qr codes
            scanner.setBeepEnabled(false) // set a beep after scanning
            scanner.initiateScan()
        }


    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK) {
//            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//            if (result != null) {
//                if (result.contents == null) {
//                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(getContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
//                    readedString=result.contents
//                }
//            } else {
//                super.onActivityResult(requestCode, resultCode, data)
//            }
//        }
//    }

    fun generateQRCode(){
        //will create barcde by generating a bitmap
      //  Toast.makeText(getContext(),"In function generateQRcode",Toast.LENGTH_LONG).show()
        val bitmap = encodeAsBitmap(encodedString,300,300) // encode a s bitmap it encodes the message as qr code
//        createImageFile(bitmap) // will create an image file out of barcode generated
        QRCodeImage.setImageBitmap(bitmap)

    }

    //this function is in case we need an image file of the bar code
//    fun createImageFile(bitmapScaled : Bitmap?){
//        val bytes = ByteArrayOutputStream()
//       bitmapScaled?.compress(Bitmap.CompressFormat.PNG, 40, bytes)
//       val filepath = "/Users/nicolasojeda/Desktop/SaveMeApp/SaveMeApp/app/src/main/res/drawable/CodeAndroid.png"
//        val f = File(filepath)
//        f.createNewFile()
//     //   val fo = FileOutputStream(f)
////        fo.write(bytes.toByteArray())
////        fo.close()
//    }

    //this returns a bitmap wich we can attack later to our imageView

    fun encodeAsBitmap(str: String, WIDTH: Int, HEIGHT: Int): Bitmap? {
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null)
        } catch (iae: IllegalArgumentException) {
            return null
        }
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result.get(x, y)) -0x1000000 else -0x1
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
//        createImageFile(bitmap)
        return bitmap
    }

    //the permission function

//    fun checkPermission(context: Context, permissionArray: Array<String>): Boolean {
//        var allSuccess = true
//        for (i in permissionArray.indices){
//            if(false)
//                allSuccess = false
//        }
//        return allSuccess
//    }

    //more permission altough is not really used.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST){
            var allSuccess = true
            for(i in permissions.indices){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allSuccess = false
                    var requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if(requestAgain){
                        Toast.makeText(context,"Permission denied", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if(allSuccess)
                generateQRCode()
        }
    }

}
