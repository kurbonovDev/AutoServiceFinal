package tj.shonasim.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tj.shonasim.R
import tj.shonasim.data.entity.Users
import tj.shonasim.databinding.ClientInfoCardBinding


class UserAdapter(private var list: List<Users>,private var onClickItem: (Users) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(var binding: ClientInfoCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ClientInfoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.imUser.setImageResource(R.drawable.user)
        holder.binding.tvName.text = list[position].name
        holder.binding.tvClass.text = "Первый уровень инжинер"
        holder.binding.tvPhoneNum.text = list[position].phone

        holder.binding.root.setOnClickListener {
            onClickItem(list[position])
        }
    }
}