package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.erestaurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class TableMapAdapter extends IMSRecyclerView<Table>
{
    private Context context;
    private CallBack.With<Table> callBack;

    public TableMapAdapter(Context context, CallBack.With<Table> callBack)
    {
        this.context = context;
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);
        return new TableMapHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        Table item  = setSelectable(holder, position);
        ((TableMapHolder)holder).bind(item);
    }

    public class TableMapHolder extends ViewHolder<Table>
    {
        @BindView(R.id.text_table_name)
        TextView tableName;

        public TableMapHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Table item)
        {
            tableName.setText(item.getName());
            switch (item.getStatus())
            {
                case 1:
                    tableName.setBackground(context.getDrawable(R.drawable.ic_empty_table));
                    break;
                case 2:
                    tableName.setBackground(context.getDrawable(R.drawable.ic_serving_table));
                    break;
                case 3:
                    tableName.setBackground(context.getDrawable(R.drawable.ic_reserved_table));
                    break;
            }
        }

        @OnClick(R.id.text_table_name)
        public void onTableSelected()
        {
            if (callBack!=null && getItem().getStatus()!=2)
                callBack.run(getItem());
        }
    }
}
