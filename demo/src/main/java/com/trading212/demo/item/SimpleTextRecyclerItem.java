package com.trading212.demo.item;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trading212.demo.R;
import com.trading212.demo.samples.PayloadUpdatesActivity;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * Created by svetlin on 9.12.17.
 */
public class SimpleTextRecyclerItem extends DiverseRecyclerAdapter.RecyclerItem<String, SimpleTextRecyclerItem.ViewHolder> {

    // Using Enum ordinal positions to guarantee uniqueness of item type
    public static final int TYPE = ItemType.SIMPLE_TEXT.ordinal();

    private String text;

    public SimpleTextRecyclerItem(String text) {
        this.text = text;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public long getId() {
        return text.hashCode();
    }

    @NotNull
    @Override
    public String getData() {
        return text;
    }

    @NotNull
    @Override
    protected ViewHolder createViewHolder(@NotNull ViewGroup parent, @NotNull LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.item_simple_text, parent, false));
    }

    public static class ViewHolder extends DiverseRecyclerAdapter.ViewHolder<String> {

        private TextView textView;

        ViewHolder(@NotNull View itemView) {
            super(itemView);

            textView = findViewById(R.id.textView);
        }

        @Override
        protected void bindTo(@NotNull String data) {
            textView.setText(data);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void updateWith(@NotNull String data, @NotNull Object update) {
            super.updateWith(data, update);

            if(!(update instanceof PayloadUpdatesActivity.SimplePayload)) {
                return;
            }

            PayloadUpdatesActivity.SimplePayload payload = (PayloadUpdatesActivity.SimplePayload) update;

            textView.setText(data + " - " + payload.getMessage());
        }
    }
}
