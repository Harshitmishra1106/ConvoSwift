package com.example.whatsappclone.ui

//import android.app.Fragment
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.CallAdapter
import com.example.whatsappclone.adapter.StatusAdapter
//import com.example.whatsappclone.adapter.ChatAdapter
import com.example.whatsappclone.databinding.FragmentCallBinding
import com.example.whatsappclone.databinding.FragmentStatusBinding
import com.example.whatsappclone.model.NotesModel
import com.example.whatsappclone.model.StatusModel
//import com.example.whatsappclone.databinding.FragmentChatBinding
import com.example.whatsappclone.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallFragment : Fragment() {

    private lateinit var binding: FragmentCallBinding
    lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var pdfUri: Uri? = null
    private lateinit var s: String
    //private var k: Int = 0
    lateinit var userList: ArrayList<NotesModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        userList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database.reference.child("Notes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(NotesModel::class.java)
                        if(user!!.uid == FirebaseAuth.getInstance().uid){
                            makeDisable()
                        }
                        userList.add(user)
                    }

                    binding.callRecyclerView.adapter = CallAdapter(binding.root.context,userList)
                    checkEmptyState()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                }
            })

            binding.addNotes.setOnClickListener {
                if (binding.name.text.toString() == "") {
                    Toast.makeText(requireContext(), "Please name the book", Toast.LENGTH_SHORT)
                        .show()
                } else openFile()
            }
            binding.pdfUpload.setOnClickListener {
                pdfUri?.let { uri ->
                    uploadPdfToFirebase(uri)
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "Please select a PDF first",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun makeDisable() {
        binding.addNotes.isClickable = false
        binding.addNotes.isVisible = false
        binding.name.isClickable  = false
        binding.name.isVisible = false
        binding.pdfUpload.setOnClickListener{
            Toast.makeText(requireContext(), "Only 1 notes upload was allowed 😔", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPdfToFirebase(uri: Uri) {
        val reference = storage.reference.child("Notes").child(Date().time.toString())
        reference.putFile(uri).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }

    }

    private fun uploadInfo(uri: String){
        s=binding.name.text.toString()
        val user = NotesModel(auth.uid.toString(),s,uri,0)
        database.reference.child("Notes")
            .child((auth.uid.toString()))
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this.requireContext(), "Notes uploaded", Toast.LENGTH_SHORT).show()
                makeDisable()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            pdfUri = data.data
            Toast.makeText(requireContext(), "PDF selected: ${pdfUri?.lastPathSegment}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf" // Filter for PDF files
        startActivityForResult(intent, PICK_PDF_REQUEST)
    }

    private fun checkEmptyState() {
        if (userList.isEmpty()) {
            binding.callRecyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.callRecyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }

    companion object {
        private const val PICK_PDF_REQUEST = 1
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CallFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CallFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}