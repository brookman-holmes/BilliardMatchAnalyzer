package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.Page;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectBallsDialog extends Fragment {
    static final String ARG_KEY = "key";
    PageFragmentCallbacks mCallbacks;
    String mKey;
    Page mPage;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.ballGrid)
    GridLayout ballGrid;

    public static SelectBallsDialog create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString("title", "Balls Brookman made");

        SelectBallsDialog fragment = new SelectBallsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Fragment must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) getParentFragment();

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_nine_ball_dialog, container, false);
        ButterKnife.bind(this, view);
        title.setText(getArguments().getString("title", "--"));

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = (ballGrid.getMeasuredWidth());
                int cy = (ballGrid.getMeasuredHeight());
                int radius = Math.max(ballGrid.getWidth(), ballGrid.getHeight());
                Animator anim = ViewAnimationUtils
                        .createCircularReveal(ballGrid, cx, cy, 0, radius);
                anim.setStartDelay(50);
                ballGrid.setVisibility(View.VISIBLE);
                anim.start();
            }
        });

        return view;
    }
}
