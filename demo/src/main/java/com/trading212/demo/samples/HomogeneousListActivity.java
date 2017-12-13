package com.trading212.demo.samples;

import android.view.View;
import android.widget.Toast;

import com.trading212.demo.BaseActivity;
import com.trading212.demo.item.SimpleTextRecyclerItem;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * Created by svetlin on 9.12.17.
 */
public class HomogeneousListActivity extends BaseActivity {

    @Override
    public void fillElements(@NotNull final DiverseRecyclerAdapter adapter) {
        for (int i = 0; i < 50; i++) {
            adapter.addItem(new SimpleTextRecyclerItem("Item " + i), false);
        }
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new DiverseRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClicked(@NotNull View v, int position) {

                SimpleTextRecyclerItem item = adapter.getItem(position);
                Toast.makeText(v.getContext(), "Clicked " + item.getData(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
