package ac.kr.hallym.smartportfoliio

import ac.kr.hallym.smartportfoliio.databinding.ActivityEditBinding
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class EditActivity : AppCompatActivity() {
    lateinit var id:String
    lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val data=intent.getStringExtra("data")
        val sendid=intent.getStringExtra("id")

        binding.contents.text=data
        id= sendid.toString()


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.resume_save,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.resume_save -> {
            val intent = intent.putExtra(id,binding.edit.text.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else -> true
    }
    override fun onBackPressed() {
//        super.onBackPressed()
        Toast.makeText(this,"뒤로가기는 위 저장을 이용해주세요", Toast.LENGTH_SHORT).show()
    }
}