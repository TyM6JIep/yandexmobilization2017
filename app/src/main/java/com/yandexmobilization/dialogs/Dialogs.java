package com.yandexmobilization.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.yandexmobilization.MainActivity;
import com.yandexmobilization.R;
import com.yandexmobilization.adapters.LanguageAdapter;
import com.yandexmobilization.models.Language;
import com.yandexmobilization.predicates.LanguagePredicate;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class Dialogs {

    private static AlertDialog mDialog = null;

    public static void show(AlertDialog dialog) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = dialog;
        mDialog.show();
    }
    public static void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private static AlertDialog build(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }
    private static AlertDialog build(AlertDialog.Builder builder, Boolean isInsideTouchable) {
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCanceledOnTouchOutside(isInsideTouchable);
        dialog.setCancelable(isInsideTouchable);

        return dialog;
    }

    public static AlertDialog getLanguageDialog(MainActivity activity, final LanguageAdapter adapter) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_language, null);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_language);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final View emptyView = view.findViewById(R.id.view_empty);

        final List<Language> list = adapter.getData();

        EditText search = (EditText) view.findViewById(R.id.edit_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                List<Language> filtered = (List<Language>) CollectionUtils.select(list, new LanguagePredicate(editable.toString()));
                adapter.clear();
                adapter.loadAddress(filtered);
                adapter.notifyDataSetChanged();

                if (filtered.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.setView(view);
        return build(builder);
    }
}
