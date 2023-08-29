package com.delta.bhansalitechno.fargments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.adapter.TexListAdapterWithFilter;
import com.delta.bhansalitechno.interfaces.TextListInterface;
import com.delta.bhansalitechno.model.JobNoModel;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.List;

public class TextListFragmentWithFilter extends DialogFragment {

    List<JobNoModel> listTextLists;
    TextListInterface textListInterface;
    RecyclerView recyclerView;
    TexListAdapterWithFilter adapter;

    ImageView imgClearText;
    EditText txtSearch;
    LinearLayout linearCancel;

    public TextListFragmentWithFilter(@NonNull Context context, @NonNull List<JobNoModel> listTextLists, @NonNull TextListInterface textListInterface) {
        this.listTextLists = listTextLists;
        this.textListInterface = textListInterface;
    }

    @Override
    public void onActivityCreated(@NonNull Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.FullScreenDialogStyleWhiteStatusBar;
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleWhiteStatusBar);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_text_list_with_filter, container, false);

            //Casting
            recyclerView = view.findViewById(R.id.recyclerView);
            txtSearch = view.findViewById(R.id.txtSearch);
            linearCancel = view.findViewById(R.id.linearCancel);
            imgClearText = view.findViewById(R.id.imgClearText);

            //Setting Up RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setFocusable(false);

            //Setting Up List To The Adapter
            adapter = new TexListAdapterWithFilter(getActivity(), listTextLists, (problemId, problem,no) -> {
                dismiss();
                hideKeyboardInActivity(requireActivity(), txtSearch);
                textListInterface.onSelect(problemId, problem,no);
            });
            recyclerView.setAdapter(adapter);

            linearCancel.setOnClickListener(view1 -> {
                try {
                    txtSearch.getText().clear();
                    txtSearch.clearFocus();
                    imgClearText.setVisibility(View.GONE);
                    hideKeyboardInActivity(requireActivity(), txtSearch);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            });

            imgClearText.setOnClickListener(view12 -> {
                txtSearch.getText().clear();
                imgClearText.setVisibility(View.GONE);
            });

            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (charSequence != null && charSequence.toString().trim().length() > 0) {
                            if (adapter != null) {
                                adapter.getFilter().filter(charSequence.toString().trim());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (charSequence != null && charSequence.toString().trim().length() > 0) {
                            imgClearText.setVisibility(View.VISIBLE);
                            linearCancel.setVisibility(View.VISIBLE);

                            if (adapter != null) {
                                adapter.getFilter().filter(charSequence.toString().trim());
                            }

                        } else {
                            imgClearText.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if (adapter != null) {
                            adapter.getFilter().filter(editable.toString().trim());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }


        return view;
    }

    public static void hideKeyboardInActivity(Activity activity, EditText txt) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);
        }
    }
}
