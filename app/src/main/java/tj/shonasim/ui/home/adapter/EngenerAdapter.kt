package tj.shonasim.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tj.shonasim.R
import tj.shonasim.data.entity.Tasks
import tj.shonasim.data.entity.Users
import tj.shonasim.databinding.ClientInfoCardBinding
import tj.shonasim.databinding.TaskInfoCardBinding


class EngenerAdapter(private val context:Context,private var list: List<Tasks>, private var onClickItem: (Tasks) -> Unit) :
    RecyclerView.Adapter<EngenerAdapter.TaskViewHolder>() {

    class TaskViewHolder(var binding: TaskInfoCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            TaskInfoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.imCar.setImageResource(R.drawable.car)
        holder.binding.tvBrandCar.text = list[position].name
        val status = list[position].status
        holder.binding.tvStatus.text = status
        holder.binding.tvDate.text = list[position].description

        if (status == "В процессе") {
            holder.binding.tvStatus.text = "В процессе"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else if (status == "Готова") {
            holder.binding.tvStatus.text = "Готова"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
        }


        holder.binding.root.setOnClickListener {
            onClickItem(list[position])
        }
    }
}