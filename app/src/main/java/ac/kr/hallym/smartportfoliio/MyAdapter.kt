package ac.kr.hallym.smartportfoliio


import ac.kr.hallym.smartportfoliio.databinding.ItemRecyclerviewBinding
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class ResumViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter (val contents: MutableList<String>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ResumViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as ResumViewHolder).binding
        binding.itemData.text=contents!![position]
    }

    override fun getItemCount(): Int {
        return contents?.size ?: 0
    }
}
class Decoration(val contents: Context?): RecyclerView.ItemDecoration(){
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
