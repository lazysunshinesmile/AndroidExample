package com.base.module.screensaver.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtil {
    Runtime mRuntime;
    private static ShellUtil mShell = new ShellUtil();

    private ShellUtil() {
        mRuntime = Runtime.getRuntime();
    }

    public static ShellUtil getInstance() {
        return mShell;
    }

    public Process rawExecute(String shell) {
        try {
            return mRuntime.exec(shell);
        } catch (IOException e) {
            System.err.println("raw Execute error .");
            e.printStackTrace();
        }
        return null;
    }

    public String execute(File shFile) {
        if (!shFile.exists()) {
            return null;
        } else {
            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(shFile)));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(mShell.execute(line));
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * execute the shell to get the output information.
     * 
     * @param shell
     * @return
     */
    public String executeQuery(String shell) {
        Process p = null;
        try {
            p = mRuntime.exec(shell);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getNormalInfo(p);
    }

    /**
     * execute the shell to get the error information , if success , return null
     * .
     * 
     * @param shell
     * @return
     */
    public String execute(String shell) {
        Process p = null;
        try {
            p = mRuntime.exec(shell);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }

        return getErrInfo(p);
    }

    public String execute(String[] shells) {
        Process p = null;
        try {
            p = mRuntime.exec(shells);
        } catch (IOException e) {
            System.out.println("IOEXCEPTION...");
            e.printStackTrace();
        }
        return getErrInfo(p);
    }

    public String getErrInfo(Process process) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getNormalInfo(Process process) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
