package com.trading212.demo.samples;

import com.trading212.demo.BaseActivity;
import com.trading212.demo.item.SimpleTextRecyclerItem;
import com.trading212.demo.item.SingleChoiceRecyclerItem;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * Created by svetlin on 27.01.18.
 */

public class SingleChoiceListActivity extends BaseActivity {

    @Override
    public void fillElements(@NotNull DiverseRecyclerAdapter adapter) {

        adapter.setSelectionMode(DiverseRecyclerAdapter.SelectionMode.MULTIPLE);

        adapter.addItem(new SimpleTextRecyclerItem("Not selectable"));

        for (int i = 0; i < 50; i++) {
            adapter.addItem(new SingleChoiceRecyclerItem("Item " + i), false);
        }
        adapter.notifyDataSetChanged();

        adapter.setItemSelected(adapter.getItem(1), true);
        adapter.setItemSelected(adapter.getItem(2), true);
    }
}
