package ac.kr.hallym.smartportfoliio

import ac.kr.hallym.smartportfoliio.databinding.ActivityDidEditBinding
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class DidEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityDidEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDidEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.resume_save,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.resume_save -> {
            val didTitle=binding.didTitle.text.toString()
            val didGit=binding.didGit.text.toString()


            if (didTitle!="" && didGit!="" && didTitle.length<=18) {
                //데이터베이스 저장
                val db=DBHelper(this).writableDatabase
                val contentValues= ContentValues()
                contentValues.put("didTitle",didTitle)
                contentValues.put("didAddr",didGit)
                db.insert("DID_TB",null,contentValues)
                //입력정보 whatdidfragment로 전송
                intent.putExtra("didTitle", didTitle)
                intent.putExtra("didGit", didGit)
                setResult(Activity.RESULT_OK, intent)
                db.close()
                finish()
            }
            else{
                if (didTitle.length>18){
                    Toast.makeText(this,"18글자 및으로 입력 바랍니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "전부 입력해야 합니다!!", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        else -> true
    }
    override fun onBackPressed() {
//        super.onBackPressed()
        Toast.makeText(this,"뒤로가기는 위 저장을 이용해주세요", Toast.LENGTH_SHORT).show()
    }

}