package baecon.devgames.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;

public interface DevGamesTab{

    Fragment getFragmentFromTab();
    String getTitle();
    DevGamesTab setTitle(String s);

    interface OnSearchActionListener extends SearchView.OnQueryTextListener {
    }

    interface OnSortActionListener {
        void onSortRequest();
    }

    interface OnAddActionListener {
        void onAddRequest();
    }

    interface OnFilterRequestListener {
        void onFilterRequest();
    }

    interface OnRefreshRequestListener {
        void onRefreshRequest();
    }

    interface OnCallRequestListener {
        void onCallRequest();
    }

    interface OnSaveRequestListener {
        void onSaveRequest();
    }

    interface OnEditRequestListener {
        void onEditRequest();
    }

    interface OnCancelRequestListener {
        void onCancelRequest();
    }
}
