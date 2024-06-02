package ac.kr.hallym.smartportfoliio

import ac.kr.hallym.smartportfoliio.databinding.DidItemRecyclerviewBinding
import ac.kr.hallym.smartportfoliio.databinding.ItemRecyclerviewBinding
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class MyViewHolder(val binding: DidItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class ActivityAdaptet(val contents: MutableList<String>?,val backgraound_img:MutableList<Int>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var binding:DidItemRecyclerviewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(DidItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        binding=(holder as MyViewHolder).binding
        Log.d("kkang","$contents")
        binding.itemData.setMovementMethod(ScrollingMovementMethod())
        binding.itemData.text=contents!![position]
        binding.imgId.setImageResource(R.drawable.ring)
        binding.backImg.setImageResource(backgraound_img[position%4])
        holder.binding.backImg.setOnClickListener {
            Log.d("kkang","아댑터 이벤트 발생: $position")
            itemClickListener.onClick(position)
        }
        holder.binding.itemData.setOnClickListener {
            Log.d("kkang","아댑터 이벤트 발생: $position")
            itemClickListener.onClick(position)
        }
    }
    //  리스너 인터페이스
    interface OnItemClickListener {
        fun onClick( position: Int)
    }
    //  외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    //  setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int{
        return contents?.size ?: 0
    }
}

class MyDecoration(val contents: Context?): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(10,0,10,0)
        view.setBackgroundColor(Color.argb(0,0,0,0))
        ViewCompat.setElevation(view,20.0f)
    }
}



