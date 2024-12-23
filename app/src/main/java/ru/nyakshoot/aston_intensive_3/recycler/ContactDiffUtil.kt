package ru.nyakshoot.aston_intensive_3.recycler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

const val CONTACT_PAYLOAD_NAME_KEY = "NAME"
const val CONTACT_PAYLOAD_SURNAME_KEY = "SURNAME"
const val CONTACT_PAYLOAD_PHONE_NUMBER_KEY = "PHONE_NUMBER"

object ContactDiffUtil : DiffUtil.ItemCallback<ContactModel>() {

    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ContactModel, newItem: ContactModel): Any? {
        val bundle = Bundle()
        if (oldItem.name != newItem.name) {
            bundle.putString(CONTACT_PAYLOAD_NAME_KEY, newItem.name)
        }
        if (oldItem.surname != newItem.surname) {
            bundle.putString(CONTACT_PAYLOAD_SURNAME_KEY, newItem.surname)
        }
        if (oldItem.phoneNumber != newItem.phoneNumber) {
            bundle.putString(CONTACT_PAYLOAD_PHONE_NUMBER_KEY, newItem.phoneNumber)
        } else {
            return super.getChangePayload(oldItem, newItem)
        }
        return bundle
    }

}