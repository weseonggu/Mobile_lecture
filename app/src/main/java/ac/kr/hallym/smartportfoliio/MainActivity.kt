package ac.kr.hallym.smartportfoliio

import ac.kr.hallym.smartportfoliio.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainActivity : AppCompatActivity() {
    var initTime=0L//backbutton 입력시간
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //프레그먼트
        binding.viewpager.adapter=MyFragmentPagerViewAdapter(this)
    }

    //backbutton 실행시 앱 종료 설정
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode === KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-initTime>3000){
                Toast.makeText(this,"종료하려면 한 번 더 누루세요!!",
                    Toast.LENGTH_SHORT).show()
                initTime=System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
    //뷰페이저
    class MyFragmentPagerViewAdapter(activity: FragmentActivity):FragmentStateAdapter(activity){
        val fragments:List<Fragment>
        init {
            fragments = listOf(ResumeFragment(), WhatDidFragment())
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment =fragments[position]
}