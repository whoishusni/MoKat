package id.husni.mokat.core.utils

import androidx.recyclerview.widget.DiffUtil
import id.husni.mokat.core.domain.model.Movies

class MovieDiffCallBack(private val oldListMovies: List<Movies>, private val newListMovies: List<Movies>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldListMovies.size
    }

    override fun getNewListSize(): Int {
        return newListMovies.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldListMovies[oldItemPosition].id == newListMovies[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovies = oldListMovies[oldItemPosition]
        val newMovies = newListMovies[newItemPosition]
        return oldMovies.id == newMovies.id || oldMovies.title == newMovies.title
    }
}