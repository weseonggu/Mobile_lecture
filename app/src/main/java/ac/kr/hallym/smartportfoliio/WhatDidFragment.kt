package ac.kr.hallym.smartportfoliio

import ac.kr.hallym.smartportfoliio.databinding.ActivityMainBinding
import ac.kr.hallym.smartportfoliio.databinding.DialogInputBinding
import ac.kr.hallym.smartportfoliio.databinding.FragmentResumeBinding
import ac.kr.hallym.smartportfoliio.databinding.FragmentWhatDidBinding
import android.app.ProgressDialog.show
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.ByteArrayOutputStream
import java.lang.Exception


class WhatDidFragment : Fragment() {

    private lateinit var binding: FragmentWhatDidBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var adapter: ActivityAdaptet

    var didTitle:MutableList<String>?=null
    var gitAddr:MutableList<String>?=null

    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentWhatDidBinding .inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)//프레그먼트에서 툴비를 보여주기위해서 사용함
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.logout)



        //intent사후처리
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it.data!!.getStringExtra("didTitle")?.let{
                didTitle!!.add(it)
                adapter.notifyDataSetChanged()
            }
            it.data!!.getStringExtra("didGit")?.let{
                gitAddr!!.add(it)
            }
        }


        didTitle=savedInstanceState?.let{
            it.getStringArrayList("didTitle")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }
        gitAddr=savedInstanceState?.let{
            it.getStringArrayList("gitAddr")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }
        //활동 추가
        binding.mainFad.setOnClickListener{
            val intent:Intent=Intent(context,DidEditActivity::class.java)
            resultLauncher.launch(intent)
        }
        //데이터베이스에서 데이터 가져오기
        val db=DBHelper(requireContext()).readableDatabase
        val did=db.rawQuery("select * from DID_TB",null)
        did.run{
            while (moveToNext()){
                didTitle?.add(did.getString(1))
                gitAddr?.add(did.getString(2))
            }
        }
        did.close()
        db.close()



        var item_backgraound= mutableListOf<Int>(R.drawable.itemone, R.drawable.itemtwo, R.drawable.itemthree, R.drawable.itemfour)
        val layoutManager= LinearLayoutManager(activity)
        binding.didRecyclerView.layoutManager=layoutManager
        adapter = ActivityAdaptet( didTitle, item_backgraound)
        binding.didRecyclerView.adapter=adapter
        binding.didRecyclerView.addItemDecoration(MyDecoration(context))


        //리사이클뷰 클릭 이벤트
        adapter.setItemClickListener(object : ActivityAdaptet.OnItemClickListener{
            override fun onClick(position: Int) {
                when(getNetWorkStatusCheck(mainActivity)){
                    true -> {
                        try{
                            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("${gitAddr?.get(position)}"))
                            startActivity(intent)
                        }catch (e: Exception){
                            Toast.makeText(mainActivity,"없는 주소입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    false -> {
                        Toast.makeText(mainActivity,"인터넷 연결이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        return binding.root
    }
    //툴바 보여주기
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.did_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    //툴바 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home ->{
            val intent:Intent=Intent(context,LoginActivity::class.java)
            ActivityCompat.finishAffinity(mainActivity)
            startActivity(intent)

            true
        }
        //아이템 삭제
        R.id.did_edit ->{
            //다이얼로그
            val dialogBinding= DialogInputBinding.inflate(layoutInflater)
            val dialogBuilder= AlertDialog.Builder(mainActivity).run {
                setTitle("지울 아이템위치를 적으시오")
                setView(dialogBinding.root)
                show()
            }
            dialogBinding.del.setOnClickListener {
                if(dialogBinding.dialogEdit.text.toString()==""){
                    dialogBuilder.dismiss()
                }
                else{
                    val position=dialogBinding.dialogEdit.text.toString().toInt()

                    delitem(adapter,dialogBuilder,position)
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }

            true
        }
        else -> {
            false
        }
    }

    // 아이템 삭제 함수
    private fun delitem(adapter:ActivityAdaptet, builder:AlertDialog,position:Int){
        if(didTitle?.size?:0 < position){
            builder.dismiss()
            Toast.makeText(mainActivity,"잘못입력하셨습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            val delData=didTitle?.get(position-1)
            val deladdr=gitAddr?.get(position-1)
            didTitle?.removeAt(position-1)
            gitAddr?.removeAt(position-1)
            val db=DBHelper(mainActivity).writableDatabase
            Log.d("kkang","지울 데이터 $delData, $deladdr")
            db.execSQL("delete from DID_TB")
            for(i in 0..(didTitle?.size ?:0)-1){
                val contentValues= ContentValues()
                contentValues.put("didTitle",didTitle?.get(i))
                contentValues.put("didAddr",gitAddr?.get(i))
                db.insert("DID_TB",null,contentValues)
            }
            db.close()
            adapter.notifyItemRemoved(position-1)
            builder.dismiss()
        }
    }


    //네트워크 연결 상태 확인
    private fun getNetWorkStatusCheck(context : Context)  : Boolean {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkStatus : NetworkInfo? = connectManager.activeNetworkInfo
        val connectCheck : Boolean = networkStatus?.isConnectedOrConnecting == true
        return connectCheck
    }

    }