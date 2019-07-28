package za.co.steff.shopaholicdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.shopaholicdemo.R;
import za.co.steff.shopaholicsdk.common.dto.Shop;

public class ShopsAdapter extends BaseAdapter {

    private List<Shop> items;
    private Context context;

    private ShopsAdapterListener listener;

    public ShopsAdapter(Context context, List<Shop> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            // If there's no view, inflate the view
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_id_name, parent, false);

            // Assign the view holder
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Populate data
        Shop item = items.get(position);
        holder.txtId.setText(String.format("%d", item.getId()));
        holder.txtName.setText(item.getName());

        // Set click listener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onShopSelected(item);
                }
            }
        });

        // Return the view
        return convertView;
    }

    public void setListener(ShopsAdapterListener listener) {
        this.listener = listener;
    }

    static class ViewHolder {

        @BindView(R.id.txtId)
        TextView txtId;
        @BindView(R.id.txtName)
        TextView txtName;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public interface ShopsAdapterListener {
        void onShopSelected(Shop shop);
    }
}
