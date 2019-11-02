package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.Category;
import com.ecdc.erestaurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class TabCategoryAdapter extends IMSRecyclerView<Category>
{
    private Context context;
    private CallBack.With<Category> callBack;
    private int selectedTabPos = 0;

    public TabCategoryAdapter(Context context, CallBack.With<Category> callBack)
    {
        this.context = context;
        this.callBack = callBack;
    }

    public void disselectTab()
    {
        selectedTabPos = -1;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false);
        return new TabCategoryAdapter.TabCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        Category item = setSelectable(holder, position);
        ((TabCategoryHolder)holder).bind(item);
    }

    public class TabCategoryHolder extends ViewHolder<Category>
    {
        @BindView(R.id.layout_holder)
        LinearLayout layoutHolder;
        @BindView(R.id.text_department_name)
        TextView departmentName;
        @BindView(R.id.text_num_table)
        TextView numTable;

        public TabCategoryHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Category item)
        {
            if (item.getName() == null || item.getName().isEmpty())
                departmentName.setText("Tất cả");
            else
                departmentName.setText(item.getName());

            numTable.setText(String.format("(%s)",item.getNumProducts()));

            if (getAdapterPosition() == selectedTabPos)
            {
                layoutHolder.setBackground(context.getDrawable(R.drawable.background_tab_pressed));
                departmentName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else
            {
                layoutHolder.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                departmentName.setTextColor(context.getResources().getColor(R.color.colorTextPrimary));
            }
        }

        @OnClick(R.id.layout_holder)
        public void onSelectTab()
        {
            selectedTabPos = getAdapterPosition();
            notifyDataSetChanged();
            if (callBack!=null)
                callBack.run(getItem());
        }
    }
}
