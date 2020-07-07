package com.trading212.demo.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trading212.demo.R;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * Created by svetlin on 27.01.18.
 */
public class SelectableRecyclerItem extends DiverseRecyclerAdapter.RecyclerItem<String, SelectableRecyclerItem.ViewHolder> {

    @SuppressWarnings("WeakerAccess")
    public static final int TYPE = ItemType.SELECTABLE.ordinal();

    private String title;

    public SelectableRecyclerItem(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @NotNull
    @Override
    public String getData() {
        return title;
    }

    @NotNull
    @Override
    protected ViewHolder createViewHolder(@NotNull ViewGroup parent, @NotNull LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.item_selectable, parent, false));
    }

    public static class ViewHolder extends DiverseRecyclerAdapter.ViewHolder<String> implements DiverseRecyclerAdapter.ViewHolder.Selectable {

        private TextView titleTextView = findViewById(R.id.tvTitle);

        private ImageView checkImageView = findViewById(R.id.ivCheck);

        ViewHolder(@NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindTo(@NotNull String data) {
            titleTextView.setText(data);
        }

        @Override
        public void updateSelectionState(boolean isSelected) {
            checkImageView.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
