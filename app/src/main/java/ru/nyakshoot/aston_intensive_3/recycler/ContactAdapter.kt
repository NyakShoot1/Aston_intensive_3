package ru.nyakshoot.aston_intensive_3.recycler

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.nyakshoot.aston_intensive_3.databinding.ContactItemBinding

class ContactAdapter(
    private val onClickAction: (ContactModel, Int) -> Unit,
) : ListAdapter<ContactModel, ContactAdapter.ContactViewHolder>(
    AsyncDifferConfig
        .Builder(ContactDiffUtil)
        .build()
) {
    private val selectedContacts = mutableSetOf<ContactModel>()
    private var isDeleteMode = false

    @SuppressLint("NotifyDataSetChanged")
    fun setDeleteMode(enabled: Boolean) {
        isDeleteMode = enabled
        if (!enabled) {
            selectedContacts.clear()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        val holder = ContactViewHolder(binding)

        binding.root.setOnClickListener {
            val model = getItem(holder.adapterPosition)
            onClickAction(model, holder.adapterPosition)

            if (selectedContacts.contains(model)) {
                selectedContacts.remove(model)
                binding.contactCheckbox.isChecked = false
            } else {
                selectedContacts.add(model)
                binding.contactCheckbox.isChecked = true
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
        holder.binding.contactCheckbox.apply {
            visibility = if (isDeleteMode) View.VISIBLE else View.GONE
            isChecked = selectedContacts.contains(model)
        }
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads.first() is Bundle) {
            val newContent = payloads.first() as Bundle
            newContent.keySet().forEach() { key ->
                when (key) {
                    CONTACT_PAYLOAD_NAME_KEY -> {
                        holder.binding.contactName.text = newContent.getString(
                            CONTACT_PAYLOAD_NAME_KEY
                        )
                        holder.binding.contactName.animate()
                            .alpha(1f)
                            .setDuration(400)
                    }

                    CONTACT_PAYLOAD_SURNAME_KEY -> {
                        holder.binding.contactSurname.text = newContent.getString(
                            CONTACT_PAYLOAD_SURNAME_KEY
                        )
                        holder.binding.contactSurname.animate()
                            .alpha(1f)
                            .setDuration(400)
                    }

                    CONTACT_PAYLOAD_PHONE_NUMBER_KEY -> {
                        holder.binding.phoneNumber.text = newContent.getString(
                            CONTACT_PAYLOAD_PHONE_NUMBER_KEY
                        )
                        holder.binding.phoneNumber.animate()
                            .alpha(1f)
                            .setDuration(400)
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedContacts(contacts: Set<ContactModel>) {
        selectedContacts.clear()
        selectedContacts.addAll(contacts)
        notifyDataSetChanged()
    }

    class ContactViewHolder(
        val binding: ContactItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(model: ContactModel) {
            binding.contactId.text = model.id.toString()
            binding.contactName.text = model.name
            binding.contactSurname.text = model.surname
            binding.phoneNumber.text = model.phoneNumber
        }
    }
}