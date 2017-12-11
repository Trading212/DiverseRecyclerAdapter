[ ![Download](https://api.bintray.com/packages/svetlin-mollov/maven/diverse-recycler-adapter/images/download.svg) ](https://bintray.com/svetlin-mollov/maven/diverse-recycler-adapter/_latestVersion)

# Diverse Recycler Adapter

DiverseRecyclerAdapter is a small and yet powerful library, which greatly simplifies building lists of different items, by breaking each item type into separate class called `RecyclerItem`. This way the complexity of supporting multiple item types is moved out of the adapter, which makes the code easier to write, easier to maintain and better encapsulated. DiverseRecyclerAdapter provides a set of useful functions witch does everything you would want to do with a list - add, insert, move, remove, find item(s).

Written in Kotlin, with full Java interoperability

## Installation

```compile 'com.trading212:diverse-recycler-adapter:0.5.0'```

## Getting Started

### ViewHolder

The well-known `ViewHolder` pattern. For more details see [Android Documentation](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html).

```java
public class ViewHolder extends DiverseRecyclerAdapter.ViewHolder<String> {

    private TextView textView;

    public ViewHolder(@NotNull View itemView) {
        super(itemView);

        textView = findViewById(R.id.textView);
    }

    @Override
    protected void bindTo(@Nullable String data) {
        textView.setText(data);
    }
}
```

#### ViewHolder lifecycle

![ViewHolder lifecycle](docs/ViewHolderLifecycle.png)

### RecyclerItem

The `RecyclerItem` class describes a category(all items of the same type) in the adapter. It contains the data about a single item, as well as factory method for creating `ViewHolder`s for that category.

```java
public class SimpleTextRecyclerItem extends DiverseRecyclerAdapter.RecyclerItem<String, ViewHolder> {

    public static final int TYPE = 1;

    private String text;

    public SimpleTextRecyclerItem(String text) {
        this.text = text;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Nullable
    @Override
    public String getData() {
        return text;
    }

    @NotNull
    @Override
    protected ViewHolder createViewHolder(@NotNull ViewGroup parent, @NotNull LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.item_simple_text, parent, false));
    }
}

```

### Working with DiverseRecyclerAdapter

- Add item
```java
adapter.addItem(new SimpleTextRecyclerItem("Item Text"));
```
- Add multiple items
```java
List<SimpleTextRecyclerItem> items = new ArrayList<>(30);
for (int i = 0; i < 30; i++) {
    items.add(new SimpleTextRecyclerItem("Item " + i));
}  
adapter.addItems(items);
```
- Insert item
```java
adapter.insertItem(5, new SimpleTextRecyclerItem("Item Text"));
```
- Insert multiple items
```java
List<SimpleTextRecyclerItem> items = new ArrayList<>(30);
for (int i = 0; i < 30; i++) {
    items.add(new SimpleTextRecyclerItem("Item " + i));
}  
adapter.insertItems(5, items);
```
- Move item
```java
adapter.moveItem(3, 4);
```
- Remove item
```java
adapter.removeItem(4);
```
- Remove range
```java
adapter.removeRange(0, 5);
```
- Remove all items
```java
adapter.removeAll();
```
- Find the position of the first `RecyclerItem` with the specified type, i.e. the position of the first item of a category
```java
adapter.findFirstItemTypePosition(SimpleTextRecyclerItem.TYPE);
```
- Find the position of the last `RecyclerItem` with the specified type, i.e. the position of the last item of a category
```java
adapter.findLastItemTypePosition(SimpleTextRecyclerItem.TYPE);
```
- Get a reference to `RecyclerItem` by position
```java
if (adapter.getItemViewType(4) == SimpleTextRecyclerItem.TYPE) {
    SimpleTextRecyclerItem textRecyclerItem = adapter.getItem(4);
}
````
- Handle `itemView` events
```java
adapter.setOnItemClickListener(new DiverseRecyclerAdapter.OnItemClickListener() {
    @Override
    public void onItemClicked(@NotNull View v, int position) {
        // Handle itemView click
    }
    @Override
    public boolean onItemTouched(@NotNull View v, @NotNull MotionEvent event, int position) {
        // Handle itemView touch events
        return super.onItemTouched(v, event, position);
    }
});
```

*For more details, explore and run the **demo** module, as well as the library itself*

## License

Copyright 2017 Trading 212 Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.