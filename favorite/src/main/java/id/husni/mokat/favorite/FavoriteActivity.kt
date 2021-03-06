package id.husni.mokat.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import id.husni.mokat.R
import id.husni.mokat.core.ui.MoviesAdapter
import id.husni.mokat.detail.DetailActivity
import id.husni.mokat.favorite.databinding.ActivityFavoriteBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var _binding : ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.favoriteToolbar)
        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadKoinModules(favoriteModule)
        val rvAdapter = MoviesAdapter()
        rvAdapter.onItemClick = { detailData ->
            val i = Intent(this, DetailActivity::class.java)
            i.putExtra(DetailActivity.EXTRA_DATA,detailData)
            startActivity(i)
        }
        favoriteViewModel.getAllFavorite.observe(this,{
            rvAdapter.setMovies(it)
            binding.tvEmpty.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE

        })

        with(binding.favoriteRv){
            layoutManager = GridLayoutManager(context,2)
            setHasFixedSize(true)
            adapter = rvAdapter
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}