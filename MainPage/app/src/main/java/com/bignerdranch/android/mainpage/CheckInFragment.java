package com.bignerdranch.android.mainpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CheckInFragment extends Fragment {

    private Button mCheckInButton;
    private Button mCheckPosButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //覆盖onCreateView()方法，创建和配置fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin,container,false);
        //实例化Buttons
        mCheckInButton = view.findViewById(R.id.bt_checkinfragment_checkin);
        mCheckPosButton = view.findViewById(R.id.bt_checkinfragment_checkpositon);

        return view;
    }
}
