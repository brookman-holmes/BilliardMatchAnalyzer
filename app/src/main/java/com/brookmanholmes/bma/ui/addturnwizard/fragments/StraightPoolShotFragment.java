package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.brookmanholmes.bma.wizard.model.Page.SIMPLE_DATA_KEY;


/**
 * Created by Brookman Holmes on 12/1/2016.
 */

public class StraightPoolShotFragment extends BaseFragment {
    private static final String TAG = "StraightShotFragment";
    private static final String ARG_KEY = "key";
    @Bind(R.id.ballCount)
    TextView ballCount;
    @Bind(R.id.title)
    TextView title;

    Page page;
    String key;
    PageFragmentCallbacks callbacks;
    int ballsOnTable = 0;

    public static StraightPoolShotFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putAll(matchData);

        StraightPoolShotFragment fragment = new StraightPoolShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
        ArrayList<Integer> ballsOnTable = args.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY);

        if (ballsOnTable != null)
            this.ballsOnTable = ballsOnTable.size();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = callbacks.onGetPage(key);
        page.getData().putInt(SIMPLE_DATA_KEY, 0);
        page.notifyDataChanged();

        View view = inflater.inflate(R.layout.select_straight_pool_balls_dialog, container, false);
        ButterKnife.bind(this, view);
        title.setText(page.getTitle());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }


    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.zero})
    void padSelected(TextView textView) {
        // // FIXME: 12/4/2016 don't be retarded plx
        ballCount.append(textView.getText());
        updatePage();
    }

    @OnClick(R.id.backspace)
    void backspace() {
        if (!TextUtils.isEmpty(ballCount.getText().toString())) {
            ballCount.setText(ballCount.getText().toString().substring(0, ballCount.getText().toString().length() - 1));
            if (TextUtils.isEmpty(ballCount.getText().toString()))
                ballCount.setText(String.format(Locale.getDefault(), "%1$d", 0));
            updatePage();
        }
    }

    private void updatePage() {
        page.getData().putInt(SIMPLE_DATA_KEY, Integer.parseInt(ballCount.getText().toString()));
        page.notifyDataChanged();
    }
}
