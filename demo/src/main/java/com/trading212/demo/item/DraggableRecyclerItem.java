package com.trading212.demo.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trading212.demo.R;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;
import com.trading212.diverserecycleradapter.drag.Draggable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by svetlin on 29.01.18.
 */

public class DraggableRecyclerItem extends DiverseRecyclerAdapter.RecyclerItem<String, DraggableRecyclerItem.ViewHolder> {

    private String text;

    public DraggableRecyclerItem(String text) {
        this.text = text;
    }

    @Nullable
    @Override
    public String getData() {
        return text;
    }

    @NotNull
    @Override
    protected ViewHolder createViewHolder(@NotNull ViewGroup parent, @NotNull LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.item_draggable, parent, false));
    }

    public static class ViewHolder extends DiverseRecyclerAdapter.ViewHolder<String> implements Draggable {

        private TextView textView;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);

            textView = findViewById(R.id.textView);
        }

        @Override
        protected void bindTo(@Nullable String data) {
            textView.setText(data);
        }

        @Override
        public void onDragStart() {
            itemView.setAlpha(0.9f);
            itemView.setScaleX(1.05f);
            itemView.setScaleY(1.05f);
        }

        @Override
        public void onDragFinish() {
            itemView.setAlpha(1f);
            itemView.setScaleX(1f);
            itemView.setScaleY(1f);
        }

        @Override
        public boolean isDraggable() {
            return true;
        }
    }
}
