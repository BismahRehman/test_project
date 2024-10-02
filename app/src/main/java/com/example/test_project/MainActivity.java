package com.example.test_project;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemLongClickListener {
    private MyRecyclerViewAdapter adapter;
    private ItemRepository itemRepository;
    private List<Item> itemList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemRepository = new ItemRepository(this.getApplication());
        adapter = new MyRecyclerViewAdapter(itemList, this);
        recyclerView.setAdapter(adapter);
        setupSwipeToDelete();

        itemRepository.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemList.clear();
                itemList.addAll(items);
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showDialog());
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // We are not implementing drag-and-drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Item itemToDelete = itemList.get(position);

                // Show confirmation dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this Task")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // User confirmed, delete the item
                            itemRepository.delete(itemToDelete);
                            itemList.remove(position);
                            adapter.notifyItemRemoved(position);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // User cancelled, restore the item by notifying the adapter
                            adapter.notifyItemChanged(position);
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onItemLongClick(Item item) {
        showEditDialog(item);
    }

    private void showDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Task")
                .setMessage("Please enter your Task below:")
                .setView(input)
                .setPositiveButton("OK", (dialog, id) -> {
                    String userInput = input.getText().toString();
                    if (!userInput.isEmpty()) {
                        Item newItem = new Item(userInput);
                        itemRepository.insert(newItem);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditDialog(Item item) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(item.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Item")
                .setMessage("Please edit your text below:")
                .setView(input)
                .setPositiveButton("OK", (dialog, id) -> {
                    String updatedInput = input.getText().toString();
                    if (!updatedInput.isEmpty()) {
                        item.setName(updatedInput);
                        itemRepository.update(item);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
