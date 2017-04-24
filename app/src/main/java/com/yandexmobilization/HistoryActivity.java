package com.yandexmobilization;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.common.collect.Lists;
import com.yandexmobilization.adapters.HistoryAdapter;
import com.yandexmobilization.models.HistoryItem;
import com.yandexmobilization.predicates.HistoryPredicate;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter mAdapter;
    private EditText mEditSearch;
    private List<HistoryItem> mData = new ArrayList<>();
    private Switch mSwitch;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setToolbar();

        mEditSearch = (EditText) findViewById(R.id.edit_search);
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                filtered(editable.toString(), mSwitch.isChecked());
            }
        });

        mSwitch = (Switch) findViewById(R.id.switch_favorite);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                filtered(mEditSearch.getText().toString(), isChecked);
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEmptyView = findViewById(R.id.layout_empty);
        mAdapter = new HistoryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        refresh();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refresh() {
        mData = Lists.newArrayList(HistoryItem.findAll(HistoryItem.class));
        loadAdapterData(mData);
    }

    private void filtered(String text, boolean checked) {
        List<HistoryItem> filtered = (List<HistoryItem>) CollectionUtils.select(mData, new HistoryPredicate(text, checked));
        loadAdapterData(filtered);
    }

    private void loadAdapterData(List<HistoryItem> data) {
        mAdapter.clear();
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        mEmptyView.setVisibility(mAdapter.getData().size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}