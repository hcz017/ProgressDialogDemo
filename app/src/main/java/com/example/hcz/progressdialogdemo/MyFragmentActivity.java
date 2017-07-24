package com.example.hcz.progressdialogdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.example.hcz.progressdialogdemo.fragmenttest.ChildFragmentOfB;
import com.example.hcz.progressdialogdemo.fragmenttest.DirectChileFragmentA;
import com.example.hcz.progressdialogdemo.fragmenttest.DirectChileFragmentB;
import com.example.hcz.progressdialogdemo.fragmenttest.ParentFragment;

public class MyFragmentActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MyFragmentActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fg_toolbar);
        setActionBar(toolbar);

        findViewById(R.id.parent).setOnClickListener(this);
        findViewById(R.id.child_a).setOnClickListener(this);
        findViewById(R.id.child_b).setOnClickListener(this);
        findViewById(R.id.b_child).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        android.app.Fragment fragment = null;
        String title = null;
        switch (v.getId()) {
            case R.id.parent:
                fragment = new ParentFragment();
                title = "Parent";
                break;
            case R.id.child_a:
                fragment = new DirectChileFragmentA();
                title = "Child A";
                break;
            case R.id.child_b:
                fragment = new DirectChileFragmentB();
                title = "Child B";
                break;
            case R.id.b_child:
                fragment = new ChildFragmentOfB();
                title = "Child of B";
                break;
            default:
                break;
        }
        if (fragment != null) {
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (fragment != null && getActionBar() != null) {
            Log.d(TAG, "displayView: title " + title);
            getActionBar().setTitle(title);
        }
    }
}
