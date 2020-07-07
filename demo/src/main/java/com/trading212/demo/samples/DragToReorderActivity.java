package com.trading212.demo.samples;

import android.os.Bundle;
import android.widget.Toast;

import com.trading212.demo.BaseActivity;
import com.trading212.demo.item.DraggableRecyclerItem;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;
import com.trading212.diverserecycleradapter.drag.DragItemTouchHelperCallback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by svetlin on 29.01.18.
 */
public class DragToReorderActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DragItemTouchHelperCallback dragHelperCallback = new DragItemTouchHelperCallback(getAdapter());
        dragHelperCallback.setOnItemMoveListener(new DragItemTouchHelperCallback.OnItemMoveListener() {
            @Override
            public void onItemMoved(int fromPosition, int toPosition) {
                String message = String.format(Locale.US, "Item moved from position %d to position %d", fromPosition, toPosition);
                Toast.makeText(DragToReorderActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper dragHelper = new ItemTouchHelper(dragHelperCallback);
        dragHelper.attachToRecyclerView(getRecyclerView());
    }

    @Override
    public void fillElements(@NotNull DiverseRecyclerAdapter adapter) {
        for (int i = 0; i < 50; i++) {
            adapter.addItem(new DraggableRecyclerItem("Item " + i), false);
        }
        adapter.notifyDataSetChanged();
    }
}
