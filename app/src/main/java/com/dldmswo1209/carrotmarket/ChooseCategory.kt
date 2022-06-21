package com.dldmswo1209.carrotmarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dldmswo1209.carrotmarket.databinding.ActivityChooseCategoryBinding

class ChooseCategory : AppCompatActivity() {
    var mBinding: ActivityChooseCategoryBinding? = null
    val binding get() = mBinding!!
    val categoryList = mutableListOf<String>(
        "디지털기기",
        "생활가전" ,
        "가구,인테리어",
        "유아동",
        "유아도서",
        "생활,가공식품",
        "스포츠/레저",
        "여성잡화",
        "여성의류",
        "남성패션,잡화",
        "게임,취미",
        "뷰티,미용",
        "반려동물용품",
        "도서,티켓,음반",
        "식물",
        "기타 중고물품",
        "삽니다",
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChooseCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.previous.setOnClickListener {
            finish()
        }
        binding.categoryRecyclerview.adapter =
            CategoryAdapter(categoryList, LayoutInflater.from(this), this)

    }
}

class CategoryAdapter(
    val list: MutableList<String>,
    val inflater: LayoutInflater,
    val activity: Activity
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name : TextView

        init{
            name = itemView.findViewById(R.id.category_name)
            itemView.setOnClickListener {
                val category = list[adapterPosition]
                PostWriteActivity.activity.findViewById<TextView>(R.id.write_post_category)
                    .text = category

                activity.finish()


            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.category_item,parent,false))
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item
    }

    override fun getItemCount(): Int {
        return list.size
    }
}