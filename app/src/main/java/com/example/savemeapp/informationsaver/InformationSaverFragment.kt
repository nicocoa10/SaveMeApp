package com.example.savemeapp.informationsaver


import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController


import com.example.savemeapp.R

class InformationSaverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.information_saver_fragment, container, false)
    }


    lateinit var nextFragmentButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        nextFragmentButton = view.findViewById(R.id.button)





    }


}