package com.example.fema_botapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fema_botapp.Fragments.login_fragment;
import com.example.fema_botapp.Fragments.sign_up_fragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new login_fragment();
            case 1:
                return new sign_up_fragment();
            default:
                return new login_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
