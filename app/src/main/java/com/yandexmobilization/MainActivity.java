package com.yandexmobilization;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yandexmobilization.adapters.LanguageAdapter;
import com.yandexmobilization.dialogs.Dialogs;
import com.yandexmobilization.interfaces.ConnectionListener;
import com.yandexmobilization.interfaces.LanguageSelectListener;
import com.yandexmobilization.models.HistoryItem;
import com.yandexmobilization.models.Language;
import com.yandexmobilization.models.SupportLanguage;
import com.yandexmobilization.models.Translation;
import com.yandexmobilization.net.HttpClient;
import com.yandexmobilization.responses.Error;
import com.yandexmobilization.responses.ResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        ConnectionListener,
        LanguageSelectListener {

    private final int LANGUAGE_FROM = 0;
    private final int LANGUAGE_TO = 1;

    private EditText mEditText;
    private String mCodeFrom, mCodeTo;
    private LanguageAdapter mAdapter;
    private Button mButtonLangFrom, mButtonLangTo;
    private TextView mTextTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextTranslate = (TextView) findViewById(R.id.text_translate);
        mEditText = (EditText) findViewById(R.id.edit_text);

        mButtonLangFrom = (Button) findViewById(R.id.button_language_from);
        mButtonLangFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportLanguage.getLanguages(MainActivity.this, mCodeFrom, new ResponseCallback<SupportLanguage>() {
                    @Override
                    public void onSuccess(SupportLanguage response) {
                        HashMap<String, String> map = response.getLangs();
                        createAndShowDialog(map, LANGUAGE_FROM);
                    }

                    @Override
                    public void onError(Error error) {
                        showError(error);
                    }
                });
            }
        });
        mButtonLangTo = (Button) findViewById(R.id.button_language_to);
        mButtonLangTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportLanguage.getLanguages(MainActivity.this, mCodeFrom, new ResponseCallback<SupportLanguage>() {
                    @Override
                    public void onSuccess(SupportLanguage response) {
                        HashMap<String, String> map = response.getLangs();
                        createAndShowDialog(map, LANGUAGE_TO);
                    }

                    @Override
                    public void onError(Error error) {
                        showError(error);
                    }
                });
            }
        });

        findViewById(R.id.button_replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mButtonLangTo.getText().toString();
                mButtonLangTo.setText(mButtonLangFrom.getText().toString());
                mButtonLangFrom.setText(s);

                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(400);

                mButtonLangFrom.startAnimation(animation);
                mButtonLangTo.startAnimation(animation);

                s = mCodeFrom;
                mCodeFrom = mCodeTo;
                mCodeTo = s;
            }
        });
        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
                mTextTranslate.setText("");

            }
        });
        findViewById(R.id.button_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString().trim();
                List<HistoryItem> list = HistoryItem.find(HistoryItem.class, "text = ?", text);
                if (list != null && list.size() != 0) {
                    HistoryItem item = list.get(0);
                    item.setFavorite();
                    item.save();
                } else {
                    HistoryItem item = new HistoryItem(text, mTextTranslate.getText().toString(), String.format("%s-%s", mCodeFrom, mCodeTo));
                    item.setFavorite();
                    item.save();
                }
            }
        });
        findViewById(R.id.button_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
            }
        });
        findViewById(R.id.button_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mEditText.getText().toString(), mEditText.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this, getString(R.string.message_copy), Toast.LENGTH_SHORT).show(); //todo
            }
        });
        findViewById(R.id.button_copy_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mTextTranslate.getText().toString(), mTextTranslate.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this, getString(R.string.message_copy), Toast.LENGTH_SHORT).show(); //todo
            }
        });
        initDefaultLanguage(mButtonLangFrom, mButtonLangTo);
        mAdapter = new LanguageAdapter(this);
    }

    private void createAndShowDialog(HashMap<String, String> map, int type) {
        List<Language> list = new ArrayList<>();
        for (Map.Entry<String, String> item : map.entrySet()) {
            list.add(new Language(item.getKey(), item.getValue()));
        }
        mAdapter.clear();
        mAdapter.loadAddress(list);
        mAdapter.setType(type);
        Dialogs.show(Dialogs.getLanguageDialog(this, mAdapter));
    }

    private void translate() {
        String text = mEditText.getText().toString();
        if (text.length() <= 10000) { //todo
            SupportLanguage.translate(this, text, String.format("%s-%s", mCodeFrom, mCodeTo), new ResponseCallback<Translation>() {
                @Override
                public void onSuccess(Translation response) {
                    if (response != null && response.getCode() == 200) {
                        mTextTranslate.setText(response.getText().get(0));
                        saveTranslation(response);
                    } else if (response != null) {
                        showError(new Error(response.getCode(), getString(R.string.message_unknown_error)));
                    } else {
                        showError(getString(R.string.message_unknown_error));
                    }
                }

                @Override
                public void onError(Error error) {
                    showError(error);
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.message_big_size), Toast.LENGTH_LONG).show();
        }
    }

    private void saveTranslation(Translation translation) {
        String text = mEditText.getText().toString().trim();
        List<HistoryItem> list = HistoryItem.find(HistoryItem.class, "text = ?", new String[]{text});
        if (list != null && list.size() == 0) {
            HistoryItem item = new HistoryItem(text, translation.getText().get(0), translation.getLang());
            item.save();
        }
    }

    private void initDefaultLanguage(Button buttonLangFrom, Button buttonLangTo) {
        buttonLangFrom.setText(getString(R.string.lang_ru));
        buttonLangTo.setText(getString(R.string.lang_en));

        mCodeFrom = getString(R.string.lang_ru_code);
        mCodeTo = getString(R.string.lang_en_code);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_history) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showError(Error error) {
        if (error != null) {
            showError(error.getMessage());
        } else {
            showError(getString(R.string.message_unknown_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        HttpClient.addConnectionListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        HttpClient.removeConnectionListener(this);
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, getString(R.string.message_connection), Toast.LENGTH_LONG).show(); //todo
    }

    @Override
    public void onSelectLanguage(Language language, int type) {
        Dialogs.dismiss();
        if (language != null) {
            if (type == LANGUAGE_FROM) {
                mCodeFrom = language.getCode();
                mButtonLangFrom.setText(language.getName());
            } else {
                mCodeTo = language.getCode();
                mButtonLangTo.setText(language.getName());
            }
        } else {
            showError(getString(R.string.message_unknown_error));
        }
    }
}