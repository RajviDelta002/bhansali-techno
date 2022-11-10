package com.delta.bhansalitechno.utils;

import java.io.File;

public class DirectoryCleaner {

    private final File mFile;

    public DirectoryCleaner(File file) {
        mFile = file;
    }

    public void clean() {
        if (null == mFile || !mFile.exists() || !mFile.isDirectory()) return;
        for (File file : mFile.listFiles()) {
            delete(file);
        }
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();

    }
}
