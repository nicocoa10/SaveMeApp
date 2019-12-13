package com.example.savemeapp


import android.content.ContentValues
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.savemeapp.DataModel.User
import com.example.savemeapp.DataModel.UserContract
import com.example.savemeapp.databinding.FragmentUpdateBinding



/**
 * A simple [Fragment] subclass.
 */
class Update : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    val userEn = UserContract.UserEntry
    var maxId: Int = 0
    var currentId : Int = 1
    var stringToEncode: String= android.os.Build.MODEL
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_update,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myHelper = DatabaseHandler(view.context)

        val dbwrite = myHelper.writableDatabase
        val dbread = myHelper.readableDatabase

        maxId = DatabaseUtils.queryNumEntries(dbread, userEn.TABLE_NAME).toInt()

        binding.insert.setOnClickListener {
            if(binding.Name.text.toString().isNotBlank() && binding.Phone.toString().isNotBlank()){

                if(binding.Name.text.isNotEmpty() && binding.Phone.text.isNotEmpty()){
                    val user = User(binding.Name.text.toString(), binding.Phone.text.toString())

                    val values = ContentValues().apply{
                        put(userEn.COL_NAME, user.name)
                        put(userEn.COL_PHONE, user.phone)
                    }
                    val newRowId = dbwrite?.insert(userEn.TABLE_NAME, null, values)
                    if(newRowId == (-1).toLong()){
                        binding.notifySuccess.text = "Failed to insert"
                    }else{
                        binding.notifySuccess.text = "New row of ID: $newRowId added."
                        maxId += 1
                    }
                }else{
                    binding.notifySuccess.text = "Fill in both fields"
                }
            }

        }

        //when done button is pressed then we go to read page.
        //string is generated here
        binding.done.setOnClickListener {

            val curs = dbread.rawQuery(
                "Select * from ${userEn.TABLE_NAME} " , null
            )
            if(curs.moveToFirst()){
                do{
                    stringToEncode = " $stringToEncode-" + "${curs.getString(0)}-" + "${curs.getString(1)}-" +" ${curs.getString(2)}-"
                }while(curs.moveToNext())
            }

            curs.close()
//            Toast.makeText(getContext(), stringToEncode, Toast.LENGTH_LONG).show() // for testing purposes

            val readFragment = Read()
            val args = Bundle()
            args.putString("data", stringToEncode)

            readFragment.arguments = args


            view.findNavController().navigate(R.id.action_update_to_read)
        }

        binding.read.setOnClickListener {

            // this get the database for us to read from

            val rowId: Long? = binding.getRow.text.toString().toLongOrNull()

            if(rowId == null){
                binding.notifySuccess.text = "No ID Inserted"
            }else if(rowId > maxId || rowId < (1).toLong()){
                binding.notifySuccess.text = "ID does not exist"
            }else{
                currentId = rowId.toInt()
                val curs = dbread.rawQuery(
                    "Select * from ${userEn.TABLE_NAME} " , null
                )
                var str = ""
                if(curs.moveToFirst()){
                    do{
                        str = "ID: ${curs.getString(0)}\nName: ${curs.getString(1)}\nPhone: ${curs.getString(2)}"
                    }while(curs.moveToNext())
                }

                curs.close()

                binding.reading.text = str

            }
        }

        binding.delete.setOnClickListener {

            dbwrite.execSQL("DROP TABLE IF EXISTS ${userEn.TABLE_NAME}")
            val table = "CREATE TABLE ${userEn.TABLE_NAME} (" +
                    "${userEn.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${userEn.COL_NAME} TEXT," +
                    "${userEn.COL_PHONE} VARCHAR(15))"
            dbwrite.execSQL(table)
            maxId = 0
        }

        binding.nextRow.setOnClickListener {
            if(currentId < maxId){
                currentId += 1
                val curs = dbread.rawQuery(
                    "Select * from ${userEn.TABLE_NAME} where id = $currentId", null
                )
                var str = ""
                if(curs.moveToFirst()){
                    do{
                        str = "ID: ${curs.getString(0)}\nName: ${curs.getString(1)}\nPhone: ${curs.getString(2)}"
                    }while(curs.moveToNext())
                }

                curs.close()
                binding.reading.text = str
            }else{
                binding.notifySuccess.text = "At max ID"
            }

        }
        binding.previousRow.setOnClickListener{
            if(currentId > 1){
                currentId -= 1
                val curs = dbread.rawQuery(
                    "Select * from ${userEn.TABLE_NAME} where id = $currentId", null
                )
                var str = ""
                if(curs.moveToFirst()){
                    do{
                        str = "ID: ${curs.getString(0)}\nName: ${curs.getString(1)}\nPhone: ${curs.getString(2)}"
                    }while(curs.moveToNext())
                }

                curs.close()
                binding.reading.text = str
            }else{
                binding.notifySuccess.text = "At lowest ID"
            }
        }
    }
}
