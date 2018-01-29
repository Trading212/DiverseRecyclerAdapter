package com.trading212.demo.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trading212.demo.R;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by svetlin on 27.01.18.
 */

public class SingleChoiceRecyclerItem extends DiverseRecyclerAdapter.RecyclerItem<String, SingleChoiceRecyclerItem.ViewHolder> {

    private String title;

    public SingleChoiceRecyclerItem(String title) {

        this.title = title;
    }

    @Override
    public int getType() {
        return 7;
    }

    @Nullable
    @Override
    public String getData() {
        return title;
    }

    @NotNull
    @Override
    protected ViewHolder createViewHolder(@NotNull ViewGroup parent, @NotNull LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.intem_single_choice, parent, false));
    }

    public static class ViewHolder extends DiverseRecyclerAdapter.ViewHolder<String> implements DiverseRecyclerAdapter.Selectable {

        private TextView titleTextView = findViewById(R.id.tvTitle);

        private ImageView checkImageView = findViewById(R.id.ivCheck);

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindTo(@Nullable String data) {
            titleTextView.setText(data);
        }

        @Override
        public void onSelectedStateChanged(boolean isSelected) {
            checkImageView.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
