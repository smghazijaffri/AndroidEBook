package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.DownloadAdapter;
import com.example.androidebookapps.PDFShow;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.FragmentFavoriteBinding;
import com.example.item.DownloadList;
import com.example.util.DatabaseHandler;
import com.example.util.Method;
import com.example.util.OnClick;
import com.folioreader.FolioReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class DownloadFragment extends Fragment {

    FragmentFavoriteBinding viewDownload;
    List<File> inFiles;
    List<DownloadList> databaseLists;
    List<DownloadList> downloadLists;
    DatabaseHandler db;
    Method method;
    DownloadAdapter downloadAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewDownload = FragmentFavoriteBinding.inflate(inflater, container, false);

        db = new DatabaseHandler(getActivity());
        databaseLists = new ArrayList<>();
        method = new Method(requireActivity());
        if (method.isNetworkAvailable()){
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg_orange));
        }
        inFiles = new ArrayList<>();
        downloadLists = new ArrayList<>();
        viewDownload.progressFav.setVisibility(View.GONE);
        viewDownload.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewDownload.rvFav.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 3);
        viewDownload.rvFav.setLayoutManager(layoutManager);
        viewDownload.rvFav.setFocusable(false);

        new Execute().execute();
        return viewDownload.getRoot();
    }

    @SuppressLint("StaticFieldLeak")
    class Execute extends AsyncTask<String, String, String> {

        File file;

        @Override
        protected void onPreExecute() {

            viewDownload.progressFav.setVisibility(View.VISIBLE);
            databaseLists.clear();
            inFiles.clear();
            downloadLists.clear();
            db = new DatabaseHandler(getContext());
            file = new File(method.bookStorage());

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                databaseLists.addAll(db.getDownload());
                Queue<File> files = new LinkedList<>(Arrays.asList(file.listFiles()));
                while (!files.isEmpty()) {
                    File file = files.remove();
                    if (file.isDirectory()) {
                        files.addAll(Arrays.asList(file.listFiles()));
                    } else if (file.getName().endsWith(".epub") || file.getName().endsWith(".pdf")) {
                        inFiles.add(file);
                    }
                }
                for (int i = 0; i < databaseLists.size(); i++) {
                    for (int j = 0; j < inFiles.size(); j++) {
                        if (inFiles.get(j).toString().contains(databaseLists.get(i).getUrl())) {
                            downloadLists.add(databaseLists.get(i));
                            break;
                        } else {
                            if (j == inFiles.size() - 1) {
                                db.deleteDownloadBook(databaseLists.get(i).getId());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (downloadLists.size() == 0) {
                viewDownload.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewDownload.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_download));
            } else {
                downloadAdapter = new DownloadAdapter(getActivity(), downloadLists);
                viewDownload.rvFav.setAdapter(downloadAdapter);
                downloadAdapter.setOnItemClickListener(new OnClick() {
                    @Override
                    public void position(int position) {
                        DownloadList list = downloadLists.get(position);
                        if (list.getUrl().endsWith(".epub")) {
                            FolioReader folioReader = FolioReader.get();
                            folioReader.openBook(list.getUrl());
                        } else {
                            String[] strings = list.getUrl().split("filename-");
                            String[] idPdf = strings[1].split(".pdf");

                            startActivity(new Intent(getActivity(), PDFShow.class)
                                    .putExtra("id", idPdf[0])
                                    .putExtra("link", list.getUrl())
                                    .putExtra("toolbarTitle", list.getTitle())
                                    .putExtra("type", "file")
                                    .putExtra("posLast", ""));
                        }
                    }
                });
            }

            viewDownload.progressFav.setVisibility(View.GONE);
            super.onPostExecute(s);
        }
    }

}
