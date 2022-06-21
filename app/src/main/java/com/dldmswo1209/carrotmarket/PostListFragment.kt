package com.dldmswo1209.carrotmarket

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.carrotmarket.databinding.FragmentPostListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PostListFragment : Fragment() {
    var mBinding: FragmentPostListBinding? = null
    val binding get() = mBinding!!
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    val postList = mutableListOf<Post>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        mBinding = FragmentPostListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnWrite.setOnClickListener {
            startActivity(Intent(view.context, PostWriteActivity::class.java))
        }
        // db 에서 리스트 가져와야 함 
        db.collection("post")
            .get()
            .addOnSuccessListener {
                it.forEach {
                    val image_1 = it["image_1"].toString().toUri()
                    val image_2 = it["image_2"].toString().toUri()
                    val title = it["title"].toString()
                    val category = it["category"].toString()
                    val price = it["price"].toString()
                    val content = it["content"].toString()
                    val writer = it["writer"].toString()
                    val writerUid = it["writerUid"].toString()


                    val post = Post(image_1, image_2,title,category,price,content,writer,writerUid)
                    postList.add(post)


                }
                binding.postRecyclerview.adapter =
                    PostListAdapter(
                        postList,
                        LayoutInflater.from(context as MainActivity),
                        context as MainActivity
                    )
            }
            .addOnFailureListener {

            }
    }
}
class PostListAdapter(
    val postList: MutableList<Post>,
    val inflater: LayoutInflater,
    val context: Context
): RecyclerView.Adapter<PostListAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postImage: ImageView
        val postTitle: TextView
        val postPrice: TextView

        init{
            postImage = itemView.findViewById(R.id.post_image)
            postTitle = itemView.findViewById(R.id.post_title)
            postPrice = itemView.findViewById(R.id.post_price)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.post_item,parent,false))
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        val item = postList[position]
        holder.postTitle.text = item.title
        holder.postPrice.text = item.price
        Glide.with(context).load(item.image_1)
            .centerCrop()
            .into(holder.postImage)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}