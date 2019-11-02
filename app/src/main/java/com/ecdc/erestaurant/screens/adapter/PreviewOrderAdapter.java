package com.ecdc.erestaurant.screens.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.OrderSessions;
import com.ecdc.androidlibs.utils.DatetimeUtils;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imstudio.core.ui.pager.IMSRecyclerView;
import me.imstudio.core.ui.pager.decoration.SpaceItemDecoration;

public class PreviewOrderAdapter extends IMSRecyclerView<OrderSessions> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new PreviewOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        OrderSessions item = setSelectable(holder, position);
        ((PreviewOrderHolder)holder).bind(item);
    }

    public class PreviewOrderHolder extends ViewHolder<OrderSessions>
    {
        @BindView(R.id.session_name)
        TextView sessionName;
        @BindView(R.id.text_time)
        TextView textTime;
        @BindView(R.id.creator_name)
        TextView creatorName;
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        private SessionDetailAdapter adapter;
        public PreviewOrderHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(OrderSessions item)
        {
            sessionName.setText(String.format(Locale.getDefault(), "Lượt %d", getItemCount() - getAdapterPosition()));
            textTime.setText(DatetimeUtils.formatType1(item.getCreatedTime()*1000));
            creatorName.setText(item.getCreator().getFullName().isEmpty() ? "Tôi" : item.getCreator().getFullName());

            adapter = new SessionDetailAdapter();
            adapter.addAll(item.getDetails());
            RecyclerViewBuilder.with().into(recyclerView)
                    .initializeWithDefaultLinearLayout(LinearLayoutManager.VERTICAL)
                    .addItemDecoration(new SpaceItemDecoration(2))
                    .setAdapter(adapter);
        }
    }
}
