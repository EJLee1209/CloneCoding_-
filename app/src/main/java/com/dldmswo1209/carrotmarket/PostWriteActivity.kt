package com.dldmswo1209.carrotmarket

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.carrotmarket.databinding.ActivityPostWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PostWriteActivity : AppCompatActivity() {
    var mBinding : ActivityPostWriteBinding? = null
    var imageUri : Uri? = null
    val binding get() = mBinding!!
    val imageList = mutableListOf<Uri>()
    var title : String? = null
    lateinit var fbStorage : FirebaseStorage
    lateinit var adapter: ImageAdapter
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var sharedPreferences: SharedPreferences

    companion object{
        lateinit var activity : Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        fbStorage = FirebaseStorage.getInstance()
        sharedPreferences = getSharedPreferences("user_info",Context.MODE_PRIVATE)

        activity = this

        binding.close.setOnClickListener {
            finish()
        }
        binding.writePostImage.setOnClickListener {
            ActivityCompat.requestPermissions(
                this, // 앨범에 접근하는 것을 허용할지 선택을 하는 메시지
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
            ) // 한번 허용하면 앱이 설치 되어 있는 동안 다시 뜨지 않음

            var intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 200)
        }
        adapter = ImageAdapter(imageList, LayoutInflater.from(this), this)
        binding.imageRecyclerview.adapter = adapter



        binding.writePostComplete.setOnClickListener {
            title = binding.writePostTitle.text.toString()
            val price = binding.writePostPrice.text.toString()
            val content = binding.writePostMain.text.toString()
            val category = binding.writePostCategory.text.toString()

            if(title != "" && price != "" &&
                    content != "" && category != ""){
                // 모든 내용이 작성되었을 때
                val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                val nickname = sharedPreferences.getString("nickname","").toString()
                val post = Post(imageList[0],imageList[1], title!!, category, price, content,nickname,auth.currentUser!!.uid)

                db.collection("post")
                    .document()
                    .set(post)

                finish()

            }
            else{
                Toast.makeText(this, "모든 내용을 작성 해주세요",Toast.LENGTH_SHORT).show()
            }


        }
        binding.writePostCategory.setOnClickListener {
            startActivity(Intent(this, ChooseCategory::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 200){
            imageList.clear()
            if(data?.clipData != null){ // 사진 여러개 선택한 경우
                val count=data.clipData!!.itemCount
                if(count > 2){
                    Toast.makeText(applicationContext, "사진은 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    return
                }
                for(i in 0 until count){
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imageList.add(imageUri)
                }
            }
            else{ // 단일 선택
                data?.data?.let { uri ->
                    imageUri = data?.data
                    if(imageUri != null){
                        imageList.add(imageUri!!)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

}
class Post(
    val image_1: Uri,
    val image_2: Uri,
    val title: String,
    val category: String,
    val price: String,
    val content: String,
    val writer: String,
    val writerUid: String
)


class ImageAdapter(
    val imageList: MutableList<Uri>,
    val inflater: LayoutInflater,
    val context: Context
): RecyclerView.Adapter<ImageAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView

        init {
            image = itemView.findViewById(R.id.load_image)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.post_image_item,parent,false))
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        val image = imageList[position]
        Glide.with(context).load(image)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}