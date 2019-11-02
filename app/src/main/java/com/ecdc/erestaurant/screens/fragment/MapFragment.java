package com.ecdc.erestaurant.screens.fragment;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.Department;
import com.ecdc.androidlibs.database.entity.DepartmentWrapper;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.activity.MainActivity;
import com.ecdc.erestaurant.screens.activity.MenuActivity;
import com.ecdc.erestaurant.screens.adapter.TabDepartmentAdapter;
import com.ecdc.erestaurant.screens.adapter.TableMapAdapter;
import com.ecdc.erestaurant.screens.core.BaseFragment;
import com.ecdc.erestaurant.screens.dialog.ChangeTableStatusDialog;
import com.ecdc.erestaurant.usecases.MapUseCase;
import com.ecdc.erestaurant.usecases.MapUseCaseImpl;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.listener.EndlessRecyclerViewScrollListener;

public class MapFragment extends BaseFragment<MapUseCase.Presenter> implements MapUseCase.View {

    @BindView(R.id.edit_search)
    EditText searchBar;
    @BindView(R.id.tab)
    RecyclerView tabDepartment;
    @BindView(R.id.recycler_view)
    RecyclerView listTable;
    @BindView(R.id.layout_empty_table)
    LinearLayout layoutEmptyTable;

    @Inject
    NetworkProvider provider;

    private TabDepartmentAdapter tabAdapter;
    private TableMapAdapter tableMapAdapter;

    private ChangeTableStatusDialog dialog;
    private Table selectedTable;
    private String selectedDepartment;
    private boolean isSearching;

    @Override
    public int getLayout()
    {
        return R.layout.layout_map_fragment;
    }

    @Override
    public void onSyncViews(View view)
    {
        super.onSyncViews(view);
        ERestaurant.self(requireContext()).getApplicationComponent().inject(this);
        setPresenter(new MapUseCaseImpl(requireContext(), this, provider));

        tabAdapter = new TabDepartmentAdapter(requireContext(), department -> {
            selectedDepartment = department.getId();
            isSearching = false;
            tableMapAdapter.clearAll();
            if (department.getId() != null)
                presenter.getTableByDepartment(department.getId(), 1, 10);
            else
                presenter.getAllTable(1, 10);

        });

        tableMapAdapter = new TableMapAdapter(requireContext(), table -> {
            selectedTable = table;
            dialog.setStatus(table.getStatus());
            dialog.show(getFragmentManager(), ChangeTableStatusDialog.TAG);
        });

        dialog = new ChangeTableStatusDialog();

        RecyclerViewBuilder.with().into(tabDepartment)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.HORIZONTAL)
                .setAdapter(tabAdapter);

        RecyclerViewBuilder.with().into(listTable)
                .initializeWithDefaultGridLayout(3)
                .setAdapter(tableMapAdapter);
    }

    @Override
    public void onSyncEvents()
    {
        super.onSyncEvents();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tableMapAdapter.clearAll();
                presenter.searchTable(searchBar.getText().toString(), 1, 10);
                isSearching = true;
            }
        });

        dialog.addOnDoneListener(integer -> {
            if (integer == 2)
            {
                MenuActivity.start(requireContext(), selectedTable);
                getOwnActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
            else if (integer == 1 || integer == 3)
                presenter.changeTableStatus(selectedTable.getId(), integer);
        });

        listTable.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        listTable.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                (GridLayoutManager) listTable.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isSearching)
                    presenter.searchTable(searchBar.getText().toString(), page + 1, totalItemsCount);
                else if (selectedDepartment == null)
                    presenter.getAllTable(page + 1, totalItemsCount);
                else
                    presenter.getTableByDepartment(selectedDepartment, page + 1, totalItemsCount);

            }
        });
    }

    @Override
    public void onSyncData()
    {
        super.onSyncData();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        tableMapAdapter.clearAll();
        tabAdapter.clearAll();
        presenter.getDepartment();
        presenter.getAllTable(1, 10);
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        if (isVisibleToUser) {
            if (tabAdapter!=null)
                tabAdapter.clearAll();
            if (tableMapAdapter!=null)
                tableMapAdapter.clearAll();
            if (presenter!=null) {
                //presenter.getDepartment();
                //presenter.getAllTable(1, 10);
                new MapTask().execute(1, 1);
            }
        }
    }*/

    @Override
    public void onLoadDepartmentSuccessfully(DepartmentWrapper data)
    {
        Department allDepartment = new Department();
        allDepartment.setNumTable(data.getNumTable());

        List<Department> departments = new ArrayList<>();
        departments.add(allDepartment);
        departments.addAll(data.getDepartments());

        tabAdapter.addAll(departments);
    }

    @Override
    public void onLoadTableSuccessfully(List<Table> data) {
        tableMapAdapter.addAll(data);
        if (tableMapAdapter.getItemCount() == 0)
            layoutEmptyTable.setVisibility(View.VISIBLE);
        else
            layoutEmptyTable.setVisibility(View.GONE);
    }

    @Override
    public void onHandleTableStatusSuccessfully(Table result)
    {
        selectedTable.setStatus(result.getStatus());
        tableMapAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_back)
    public void onBackToPreviousPage()
    {
        ((MainActivity)getOwnActivity()).scrollToPage(0);
    }
    @Override
    public void onFailure(String msg)
    {
        showError(msg);
    }

    @Override
    public void onTimeout()
    {

    }
}
