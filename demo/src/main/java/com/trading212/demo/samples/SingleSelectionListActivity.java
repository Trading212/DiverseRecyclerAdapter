package com.trading212.demo.samples;

import android.view.View;
import android.widget.Toast;

import com.trading212.demo.BaseActivity;
import com.trading212.demo.item.SelectableRecyclerItem;
import com.trading212.demo.item.SimpleTextRecyclerItem;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * Created by svetlin on 27.01.18.
 */

public class SingleSelectionListActivity extends BaseActivity {

    @Override
    public void fillElements(@NotNull DiverseRecyclerAdapter adapter) {

        adapter.setSelectionMode(DiverseRecyclerAdapter.SelectionMode.SINGLE);
        adapter.setOnItemSelectionStateChangeListener(new ItemSelectionStateChangeListener());

        adapter.addItem(new SimpleTextRecyclerItem("Not selectable"));

        for (int i = 0; i < 50; i++) {
            adapter.addItem(new SelectableRecyclerItem("Item " + i), false);
        }
        adapter.notifyDataSetChanged();

        adapter.setItemSelected(adapter.getItem(1), true);

        adapter.setItemsSelected(true, 2, 3); // This will select only 3 since SelectionMode is SINGLE
    }

    private class ItemSelectionStateChangeListener implements DiverseRecyclerAdapter.OnItemSelectionStateChangeListener {

        @Override
        public void onItemSelectionStateChanged(@NotNull View v, int position, boolean isSelected) {
            SelectableRecyclerItem recyclerItem = getAdapter().getItem(position);
            String text = String.format("%s selection state change to %s",
                    recyclerItem.getData(),
                    isSelected ? "selected" : "unselected");

            Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
