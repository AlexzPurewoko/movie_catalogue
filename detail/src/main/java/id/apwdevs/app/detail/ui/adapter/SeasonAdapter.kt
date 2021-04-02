package id.apwdevs.app.detail.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.apwdevs.app.core.domain.model.detail.TvShowSeason
import id.apwdevs.app.detail.databinding.ItemSeasonsBinding
import id.apwdevs.app.res.util.getImageURL

class SeasonAdapter(
    private val listData: List<TvShowSeason>
): RecyclerView.Adapter<SeasonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        return SeasonViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}

class SeasonViewHolder(
    private val binding: ItemSeasonsBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: TvShowSeason) {
        binding.apply {
            Glide.with(binding.root.context)
                    .load(data.posterPath.getImageURL("w154"))
                    .into(seasonImage)
            seasonName.text = data.name
            episodeCount.text = "${data.episodeCount} Episodes"
            seasonDate.text = data.airDate
        }
    }

    companion object {
        fun create(parent: ViewGroup): SeasonViewHolder {
            val binding = ItemSeasonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SeasonViewHolder(binding)
        }
    }
}