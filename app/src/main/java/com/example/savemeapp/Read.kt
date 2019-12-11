package com.example.savemeapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.savemeapp.databinding.FragmentReadBinding

/**
 * A simple [Fragment] subclass.
 */
class Read : Fragment() {

    private lateinit var binding: FragmentReadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_read,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myHelper = DatabaseHandler(view.context)
        val db = myHelper.readableDatabase

        if(arguments?.getString("data")!=null){

            val str = arguments?.getString("data")

            binding.nameDisplay.setText(str)


//            val QrCodeFragment = Qrcode()
//            val args = Bundle()
//            args.putString("data", str)

//            QrCodeFragment.arguments = args


       }

        binding.goUpdate.setOnClickListener { view.findNavController().navigate(R.id.action_read_to_update) }
        binding.goQr.setOnClickListener { view.findNavController().navigate(R.id.action_read_to_qrcode) }
    }

}
