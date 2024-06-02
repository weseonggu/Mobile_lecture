package ac.kr.hallym.smartportfoliio

import ac.kr.hallym.smartportfoliio.databinding.FragmentResumeBinding

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import java.lang.Exception


class ResumeFragment : Fragment() {

    private lateinit var binding: FragmentResumeBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var edu_adapter: MyAdapter
    private lateinit var rew_adapter: MyAdapter
    private lateinit var tec_adapter: MyAdapter
    private lateinit var car_adapter: MyAdapter

    private var education_contents:MutableList<String>?=null
    private var reward_contents:MutableList<String>?=null
    private var tech_contents:MutableList<String>?=null
    private var career_contents:MutableList<String>?=null
    private var gitaddr:String=""
    private var img:String=""

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //뷰바인딩
        binding = FragmentResumeBinding.inflate(inflater, container, false)
        //툴바
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)//프레그먼트에서 툴비를 보여주기위해서 사용함
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.logout)




        //엑티비티 돌아오고 나서 처리
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //dlfma
            it.data!!.getStringExtra("name")?.let{
                binding.name.text=it
            }
            //나이
            it.data!!.getStringExtra("age")?.let{
                binding.birthyear.text=it
            }
            //깃 주소
            it.data!!.getStringExtra("git")?.let{
                gitaddr=it
            }
            //intent로 받은 이미지 보여주기
            it.data!!.getStringExtra("img")?.let{
                if(it!=""){
                    img=it
                    lander(Uri.parse(it))
                }
            }
            it.data!!.getStringArrayListExtra("education")?.let{
                education_contents?.clear()
                for (i in 0..it.size-1){
                    education_contents?.add(it[i])
                }
                edu_adapter.notifyDataSetChanged()
            }
            it.data!!.getStringArrayListExtra("reward")?.let{
                reward_contents?.clear()
                for (i in 0..it.size-1){
                    reward_contents?.add(it[i])
                }
                rew_adapter.notifyDataSetChanged()
            }
            it.data!!.getStringArrayListExtra("tech")?.let{
                tech_contents?.clear()
                for (i in 0..it.size-1){
                    tech_contents?.add(it[i])
                }
                tec_adapter.notifyDataSetChanged()
            }
            it.data!!.getStringArrayListExtra("career")?.let{
                career_contents?.clear()
                for (i in 0..it.size-1){
                    career_contents?.add(it[i])
                }
                car_adapter.notifyDataSetChanged()
            }
        }

        //학력 콘테츠 베열
        education_contents=savedInstanceState?.let{
            it.getStringArrayList("education_contents")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }
        //수상 콘테츠 베열
        reward_contents=savedInstanceState?.let{
            it.getStringArrayList("reward_contents")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }
        //기술 콘테츠 베열
        tech_contents=savedInstanceState?.let{
            it.getStringArrayList("tech_contents")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }
        //경력 콘테츠 베열
        career_contents=savedInstanceState?.let{
            it.getStringArrayList("career_contents")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }


        //데이터베이스 에서 데이터 가져오기
        val db=DBHelper(requireContext()).readableDatabase
        val base=db.rawQuery("select * from BASE_TB",null)
        base.run {
            while (moveToNext()){
                binding.name.text=base.getString(1)
                binding.birthyear.text=base.getString(2)
                gitaddr=base.getString(3).toString()
                img=base.getString(4)
            }
        }
        base.close()
        //학력
        val edu_cursor = db.rawQuery("select * from RESUME_TB",null)
        edu_cursor.run{
            while(moveToNext()){
                education_contents?.add(edu_cursor.getString(1))
            }
        }
        edu_cursor.close()
        //수상
        val rew_cursor = db.rawQuery("select * from REWARD_TB",null)
        rew_cursor.run{
            while(moveToNext()){
                reward_contents?.add(rew_cursor.getString(1))
            }
        }
        rew_cursor.close()
        //사용기술
        val tec_cursor = db.rawQuery("select * from TECH_TB",null)
        tec_cursor.run{
            while (moveToNext()){
                tech_contents?.add(tec_cursor.getString(1))
            }
        }
        tec_cursor.close()
        //경력
        val car_cursor = db.rawQuery("select * from CAREER_TB",null)
        car_cursor.run{
            while(moveToNext()){
                career_contents?.add(car_cursor.getString(1))
            }
        }
        car_cursor.close()

        db.close()

        if(gitaddr==""){
            gitaddr="https://github.com/"
        }

        //깃 이미지 이벤트
        binding.github.setOnClickListener{

            when(getNetWorkStatusCheck(mainActivity)){
                true -> {
                    try{
                        val intent=Intent(Intent.ACTION_VIEW, Uri.parse("$gitaddr"))
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
        //데이터베이스 이미지 보여주기
        if(img !="") {
            lander(Uri.parse(img))
        }


        //학력 리사이클러 뷰
        val education_layoutManager= LinearLayoutManager(activity)
        binding.educationRecyclerView.layoutManager=education_layoutManager
        edu_adapter = MyAdapter( education_contents)
        binding.educationRecyclerView.adapter=edu_adapter
        binding.educationRecyclerView.addItemDecoration(
            Decoration(context))

        //수상 리사이클뷰
        val reward_layoutManager= LinearLayoutManager(activity)
        binding.rewardRecyclerView.layoutManager=reward_layoutManager
        rew_adapter = MyAdapter(reward_contents)
        binding.rewardRecyclerView.adapter=rew_adapter
        binding.rewardRecyclerView.addItemDecoration(
            Decoration(context))
        //기술 리사이클뷰
        val tech_layoutManager= LinearLayoutManager(activity)
        binding.usetechRecyclerView.layoutManager=tech_layoutManager
        tec_adapter = MyAdapter(tech_contents)
        binding.usetechRecyclerView.adapter=tec_adapter
        binding.usetechRecyclerView.addItemDecoration(
            Decoration(context))
        //경력 리사이클뷰
        val career_layoutManager= LinearLayoutManager(activity)
        binding.careerRecyclerView.layoutManager=career_layoutManager
        car_adapter = MyAdapter(career_contents)
        binding.careerRecyclerView.adapter=car_adapter
        binding.careerRecyclerView.addItemDecoration(
            Decoration(context))


        return binding.root
    }

    //툴바 메뉴 보여주는 부분
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.resume_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //툴바 이벤트 처리(이력서 편집 액티비티 이동)
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.resume_edit -> {
            val intent = Intent(context, ResumeEditActivity::class.java)
            intent.putExtra("name",binding.name.text)
            intent.putExtra("age",binding.birthyear.text)
            intent.putExtra("git",gitaddr)
            intent.putExtra("frag",img)
            intent.putStringArrayListExtra("edit_education",ArrayList(education_contents))
            intent.putStringArrayListExtra("edit_reward",ArrayList(reward_contents))
            intent.putStringArrayListExtra("edit_tech",ArrayList(tech_contents))
            intent.putStringArrayListExtra("edit_career",ArrayList(career_contents))

            resultLauncher.launch(intent)
            true
        }
        android.R.id.home ->{
            val intent=Intent(context,LoginActivity::class.java)
            ActivityCompat.finishAffinity(mainActivity)
            startActivity(intent)

            true
        }
        else -> {
            false
        }
    }


    private  fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHright:Int):Int{
        val options =BitmapFactory.Options()
        options.inJustDecodeBounds=true
        try{
            var inputStream=context?.contentResolver?.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null,options)
            inputStream!!.close()
            inputStream=null
        }catch (e:Exception){
            e.printStackTrace()
        }
        val (height: Int, width: Int)=options.run{outHeight to outWidth}
        var inSampleSize=1
        if(height> reqHright || width>reqWidth){
            val halfHeight: Int=height/2
            val halfWidth: Int=width/2
            while (halfHeight/inSampleSize>=reqHright &&
                halfWidth/inSampleSize>=reqWidth){
                inSampleSize*=2
            }
        }
        return  inSampleSize
    }
    private fun lander(img1:Uri){
        try{
            val calRatio= calculateInSampleSize(
                img1,
                resources.getDimensionPixelSize(R.dimen.imgWidth),
                resources.getDimensionPixelSize(R.dimen.imgHeight)
            )
            val option=BitmapFactory.Options()
            option.inSampleSize=calRatio
            val inputStream = context?.contentResolver?.openInputStream(img1)
            val bitmap = BitmapFactory.decodeStream(inputStream,null, option)!!
            bitmap?.let{
                binding.img.setImageBitmap(bitmap)
            } ?: let{
                Log.d("kkang","bitmap null")
            }
        }catch (e: Exception){
            Log.d("kkang","$e")
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
