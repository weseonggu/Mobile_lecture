package ac.kr.hallym.smartportfoliio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ac.kr.hallym.smartportfoliio.databinding.ActivityResumeEditBinding
import ac.kr.hallym.smartportfoliio.databinding.DialogBaseBinding
import ac.kr.hallym.smartportfoliio.databinding.DialogInputBinding
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import java.lang.Exception
import kotlin.collections.ArrayList


class ResumeEditActivity : AppCompatActivity() {

    private lateinit var binding:ActivityResumeEditBinding
    private lateinit var edu_adapter: MyAdapter
    private lateinit var rew_adapter: MyAdapter
    private lateinit var tec_adapter: MyAdapter
    private lateinit var car_adapter: MyAdapter
    //콘텐츠 데이터
    var education_contents:MutableList<String>?=null
    var reward_contents:MutableList<String>?=null
    var tech_contents:MutableList<String>?=null
    var career_contents:MutableList<String>?=null
    lateinit var gitaddr:String
    var img:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResumeEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)



        // 콘테츠 추가
        val requestLauncher: ActivityResultLauncher<Intent> =  registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            //edit activity에서 데이터 받기
            it.data!!.getStringExtra("result")?.let{
                education_contents?.add(it)
                edu_adapter.notifyDataSetChanged()
            }
            it.data!!.getStringExtra("reward")?.let{
                reward_contents?.add(it)
                rew_adapter.notifyDataSetChanged()
            }
            it.data!!.getStringExtra("tech")?.let{
                tech_contents?.add(it)
                tec_adapter.notifyDataSetChanged()
            }
            it.data!!.getStringExtra("career")?.let{
                career_contents?.add(it)
                car_adapter.notifyDataSetChanged()
            }
        }

        //갤러리 요청 런치
        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            img=it.data!!.data!!.toString()
            try{
                lander(it.data!!.data!!)
            }catch (e: Exception){
                Log.d("kkang","bitmap null")
            }
        }
        //갤러리
        binding.myImage.setOnClickListener {

                val intent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type="image/*"
                requestGalleryLauncher.launch(intent)

        }

        //이름 다이얼로그
        binding.nameEdit.setOnClickListener {
            val dialogBinding=DialogBaseBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
                setTitle("이름을 적으시오")
                setView(dialogBinding.root)
                show()
            }
            dialogBinding.del.setOnClickListener {
                if(dialogBinding.dialogEdit.text.toString()==""){
                    dialogBuilder.dismiss()
                }
                else{
                    binding.nameEdit.text=dialogBinding.dialogEdit.text.toString()
                    dialogBuilder.dismiss()
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
        //나이 다이얼로그
        binding.ageEdit.setOnClickListener {
            val dialogBinding=DialogBaseBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
                setTitle("나이를 적으시오")
                setView(dialogBinding.root)
                show()
            }
            dialogBinding.del.setOnClickListener {
                if(dialogBinding.dialogEdit.text.toString()==""){
                    dialogBuilder.dismiss()
                }
                else{
                    binding.ageEdit.text=dialogBinding.dialogEdit.text.toString()
                    dialogBuilder.dismiss()
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
        //깃 다이얼로그
        binding.github.setOnClickListener {
            val dialogBinding=DialogBaseBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
                setTitle("깃 주소를 적으시오")
                setView(dialogBinding.root)
                show()
            }
            dialogBinding.del.setOnClickListener {
                if(dialogBinding.dialogEdit.text.toString()==""){
                    dialogBuilder.dismiss()
                }
                else{
                    gitaddr=dialogBinding.dialogEdit.text.toString()
                    dialogBuilder.dismiss()
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }

        //학력 추가버튼 이벤트
        binding.educationAddButton.setOnClickListener {
            addclicked("result",requestLauncher,"학력")
        }
        //학력 삭제버튼 이벤트 다이얼로그
        binding.educationDelButton.setOnClickListener {
            val dialogBinding=DialogInputBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
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
                    delitem(education_contents,edu_adapter,dialogBuilder,position)
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
        //수상 추가버튼 이벤트
        binding.rewardAddButton.setOnClickListener {
            addclicked("reward",requestLauncher,"수상")
        }
        //수상 삭제버튼 이벤트
        binding.rewardDelButton.setOnClickListener {
            val dialogBinding=DialogInputBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
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
                    delitem(reward_contents,rew_adapter,dialogBuilder,position)
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
        //기술 추가버튼 이벤트
        binding.usetechAddButton.setOnClickListener {
            addclicked("tech",requestLauncher,"사용기술")
        }
        //기술 삭제버튼 이벤트
        binding.techDelButton.setOnClickListener {
            val dialogBinding=DialogInputBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
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
                    delitem(tech_contents,tec_adapter,dialogBuilder,position)
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
        //경력 추가버튼 이벤트
        binding.careerAddButton.setOnClickListener {
            addclicked("career",requestLauncher,"경력")
        }
        //경력 삭제버튼 이벤트
        binding.careerDelButton.setOnClickListener {
            val dialogBinding=DialogInputBinding.inflate(layoutInflater)
            val dialogBuilder=AlertDialog.Builder(this).run {
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
                    delitem(career_contents,car_adapter,dialogBuilder,position)
                }
            }
            dialogBinding.no.setOnClickListener {
                dialogBuilder.dismiss()
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
        //이력서 프레그먼트에서 원래 데이터 받아옴
        binding.nameEdit.text=intent.getStringExtra("name")
        binding.ageEdit.text=intent.getStringExtra("age")
        gitaddr= intent.getStringExtra("git").toString()
        img=intent.getStringExtra("frag").toString()
        //이미지 보여주기
        if(img!=""){
            lander(Uri.parse(img))
        }

        val edu_data:MutableList<String>?=intent.getStringArrayListExtra("edit_education")
        education_contents=edu_data

        val rew_data:MutableList<String>?=intent.getStringArrayListExtra("edit_reward")
        reward_contents=rew_data

        val tec_data:MutableList<String>?=intent.getStringArrayListExtra("edit_tech")
        tech_contents=tec_data

        val car_data:MutableList<String>?=intent.getStringArrayListExtra("edit_career")
        career_contents=car_data


        //학력 리사이클뷰
        val education_layoutManager= LinearLayoutManager(this)
        binding.educationRecyclerView.layoutManager=education_layoutManager
        edu_adapter = MyAdapter(education_contents)
        binding.educationRecyclerView.adapter=edu_adapter
        binding.educationRecyclerView.addItemDecoration(
            Decoration(this))

        //수상 리사이클뷰
        val reward_layoutManager= LinearLayoutManager(this)
        binding.rewardRecyclerView.layoutManager=reward_layoutManager
        rew_adapter = MyAdapter(reward_contents)
        binding.rewardRecyclerView.adapter=rew_adapter
        binding.rewardRecyclerView.addItemDecoration(
            Decoration(this))
        //기술 리사이클뷰
        val tech_layoutManager= LinearLayoutManager(this)
        binding.usetechRecyclerView.layoutManager=tech_layoutManager
        tec_adapter = MyAdapter(tech_contents)
        binding.usetechRecyclerView.adapter=tec_adapter
        binding.usetechRecyclerView.addItemDecoration(
            Decoration(this))
        //경력 리사이클뷰
        val career_layoutManager= LinearLayoutManager(this)
        binding.careerRecyclerView.layoutManager=career_layoutManager
        car_adapter = MyAdapter(career_contents)
        binding.careerRecyclerView.adapter=car_adapter
        binding.careerRecyclerView.addItemDecoration(
            Decoration(this))

    }





    //툴바
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.resume_save,menu)
        return super.onCreateOptionsMenu(menu)
    }
    //툴바 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.resume_save -> {
            //데이터베이스 저장
            val db = DBHelper(this).writableDatabase

            db.execSQL("delete from RESUME_TB")
            db.execSQL("delete from REWARD_TB")
            db.execSQL("delete from TECH_TB")
            db.execSQL("delete from CAREER_TB")
            db.execSQL("delete from BASE_TB")

            //학력
            for(i in 0..(education_contents?.size ?:0)-1){
                val contentValues=ContentValues()
                contentValues.put("education",education_contents?.get(i))
                db.insert("RESUME_TB",null,contentValues)

            }
            //수상
            for(i in 0..(reward_contents?.size ?:0)-1){
                val contentValues=ContentValues()
                contentValues.put("reward",reward_contents?.get(i))
                db.insert("REWARD_TB",null,contentValues)

            }
            //사용기술
            for(i in 0..(tech_contents?.size ?:0)-1){
                val contentValues=ContentValues()
                contentValues.put("tech",tech_contents?.get(i))
                db.insert("TECH_TB",null,contentValues)

            }
            //경력
            for(i in 0..(career_contents?.size ?:0)-1){
                val contentValues=ContentValues()
                contentValues.put("career",career_contents?.get(i))
                db.insert("CAREER_TB",null,contentValues)

            }
            //이름 나이 깃 이미지
            val base=ContentValues()
            base.put("name",binding.nameEdit.text.toString())
            base.put("age",binding.ageEdit.text.toString())
            base.put("git",gitaddr)
            base.put("img",img)
            db.insert("BASE_TB",null,base)

            db.close()

            //저장누르면 resumefragmet로 변경사항 전송
            intent.putExtra("name",binding.nameEdit.text)
            intent.putExtra("age",binding.ageEdit.text)
            intent.putExtra("git",gitaddr)
            intent.putExtra("img",img)
            intent.putStringArrayListExtra("education",ArrayList(education_contents))
            intent.putStringArrayListExtra("reward",ArrayList(reward_contents))
            intent.putStringArrayListExtra("tech",ArrayList(tech_contents))
            intent.putStringArrayListExtra("career",ArrayList(career_contents))

            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else -> true
    }


    // 아이템 삭제 함수
    private fun delitem(contentsList:MutableList<String>?,adapter:MyAdapter, builder:AlertDialog,position:Int){
        if(contentsList?.size?:0 < position){
            builder.dismiss()
            Toast.makeText(this,"잘못입력하셨습니다.",Toast.LENGTH_SHORT).show()
        }
        else{
            contentsList?.removeAt(position-1)
            adapter.notifyItemRemoved(position-1)
            builder.dismiss()
        }
    }


    // 추가 버튼 함수
    private fun addclicked(id:String, request:ActivityResultLauncher<Intent>,data:String){
        val intent=Intent(this,EditActivity::class.java)
        intent.putExtra("data","${data} 내용을 입력하시오.")
        intent.putExtra("id",id)
        request.launch(intent)
    }


    //백버튼 막기
    override fun onBackPressed() {
//        super.onBackPressed()
        Toast.makeText(this,"뒤로가기는 위 저장을 이용해주세요",Toast.LENGTH_SHORT).show()
    }



    private  fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHright:Int):Int{
        val options =BitmapFactory.Options()
        options.inJustDecodeBounds=true
        try{
            var inputStream=contentResolver.openInputStream(fileUri)
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
            val inputStream = contentResolver?.openInputStream(img1)
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
}
