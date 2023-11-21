package com.example.gallery

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private lateinit var galleryViewModel:GalleryViewModel

    private lateinit var _binding: FragmentGalleryBinding

    val galleryAdapter = GalleryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        _binding = FragmentGalleryBinding.inflate(inflater,container,false)
//        val view = _binding.root

        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId ){
                     R.id.swipeIndicator ->{
                         _binding.swipeRefreshLayout.isRefreshing = true
                         galleryViewModel.fetchData()
                     }
                }
                return false
            }
        },viewLifecycleOwner,Lifecycle.State.RESUMED)

        _binding = FragmentGalleryBinding.bind(view)
        _binding.recycleView.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }


        galleryViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GalleryViewModel::class.java)
        galleryViewModel.photoListLive.observe(viewLifecycleOwner,Observer {
            galleryAdapter.submitList(it)
            _binding.swipeRefreshLayout.isRefreshing = false
        })
        galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()

        _binding.swipeRefreshLayout.setOnRefreshListener {
            galleryViewModel.fetchData()
        }

    }




}