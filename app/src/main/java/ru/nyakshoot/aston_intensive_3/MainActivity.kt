package ru.nyakshoot.aston_intensive_3

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import ru.nyakshoot.aston_intensive_3.databinding.ActivityMainBinding
import ru.nyakshoot.aston_intensive_3.databinding.EditContactDialogBinding
import ru.nyakshoot.aston_intensive_3.databinding.NewContactDialogBinding
import ru.nyakshoot.aston_intensive_3.recycler.ContactAdapter
import ru.nyakshoot.aston_intensive_3.recycler.ContactModel
import ru.nyakshoot.aston_intensive_3.recycler.DragAndDropCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        val toolbar = binding.toolbar

        setContentView(view)
        setSupportActionBar(toolbar.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val newContactButton = binding.addContactBtn
        val doneDeletionContactButton = binding.doneDeletionContactBtn
        val startDeletionContactButton = toolbar.startDeletionContactsButton
        val recycler = binding.recycler


        adapter = ContactAdapter { contact, position ->
            if (viewModel.getDeletionState().value == true) {
                viewModel.selectContactForDeletion(contact)
            } else {
                showEditContactDialog(contact, position)
            }
        }
        recycler.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(DragAndDropCallback { fromPosition, toPosition ->
            viewModel.moveContact(fromPosition, toPosition)
            adapter.notifyItemMoved(fromPosition, toPosition)
        })

        itemTouchHelper.attachToRecyclerView(recycler)


        newContactButton.setOnClickListener {
            showNewContactDialog()
        }

        doneDeletionContactButton.setOnClickListener {
            viewModel.deleteSelectedContacts()
            viewModel.updateDeletionState()
        }

        startDeletionContactButton.setOnClickListener {
            viewModel.updateDeletionState()
        }

        viewModel.getCurrentContacts().observe(this) { contacts ->
            adapter.submitList(contacts.toList())
        }

        viewModel.getSelectedContacts().observe(this){
            adapter.setSelectedContacts(it)
        }

        viewModel.getDeletionState().observe(this){
            adapter.setDeleteMode(it)
            if (it) {
                newContactButton.visibility = View.GONE
                binding.cancelDeletionBtn.visibility = View.VISIBLE
                doneDeletionContactButton.visibility = View.VISIBLE
            } else {
                newContactButton.visibility = View.VISIBLE
                binding.cancelDeletionBtn.visibility = View.GONE
                doneDeletionContactButton.visibility = View.GONE
            }
        }

        binding.cancelDeletionBtn.setOnClickListener {
            viewModel.updateDeletionState()
        }
    }


    private fun showNewContactDialog() {
        val dialogBinding = NewContactDialogBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        val nameField = dialogBinding.inputNameField
        val surnameField = dialogBinding.inputSurnameField
        val phoneField = dialogBinding.inputPhoneField

        val dialog = AlertDialog.Builder(this)
            .setTitle("New contact")
            .setView(dialogView)
            .setPositiveButton(R.string.create, null)
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (nameField.text.isEmpty() || surnameField.text.isEmpty() || phoneField.text.isEmpty()) {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val newContact = ContactModel(
                        id = viewModel.getCurrentContacts().value?.size ?: 0,
                        name = nameField.text.toString(),
                        surname = surnameField.text.toString(),
                        phoneNumber = phoneField.text.toString()
                    )
                    viewModel.addNewContact(newContact)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showEditContactDialog(contact: ContactModel, position: Int) {
        val dialogBinding = EditContactDialogBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        val nameField = dialogBinding.inputNameField
        val surnameField = dialogBinding.inputSurnameField
        val phoneField = dialogBinding.inputPhoneField

        nameField.setText(contact.name)
        surnameField.setText(contact.surname)
        phoneField.setText(contact.phoneNumber)

        val dialog = AlertDialog.Builder(this)
            .setTitle("New contact")
            .setView(dialogView)
            .setPositiveButton(R.string.edit, null)
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (nameField.text.isEmpty() || surnameField.text.isEmpty() || phoneField.text.isEmpty()) {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val editedContact = contact.copy(
                        name = nameField.text.toString(),
                        surname = surnameField.text.toString(),
                        phoneNumber = phoneField.text.toString()
                    )
                    viewModel.editContact(position, editedContact)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }
}
