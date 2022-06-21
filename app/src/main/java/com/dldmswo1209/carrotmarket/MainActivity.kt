package com.dldmswo1209.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dldmswo1209.carrotmarket.databinding.ActivityLoginBinding
import com.dldmswo1209.carrotmarket.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var mBinding : ActivityMainBinding? = null
    val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ShowPostList()
        binding.search.setOnClickListener {

        }
        binding.menu.setOnClickListener {

        }
        binding.mainTab.addTab(binding.mainTab.newTab().setIcon(R.drawable.homepage).setText("홈"))
        binding.mainTab.addTab(binding.mainTab.newTab().setIcon(R.drawable.bubble_chat).setText("채팅"))
        binding.mainTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null){
                    if(tab.position == 0) ShowPostList()
                    else ShowChatList()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }
    fun ShowPostList(){
        val fragment = PostListFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun ShowChatList(){
        val fragment = ChatFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
