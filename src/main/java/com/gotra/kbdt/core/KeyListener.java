package com.gotra.kbdt.core;

import com.gotra.kbdt.modules.server.ServerModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author gotra
 */

public class KeyListener implements Runnable {
    private static final String exitKey = "q";
    private ServerModule serverModule;

    public KeyListener(ServerModule serverModule) {
        this.serverModule = serverModule;
    }

    @Override
    public void run() {
        boolean trigger = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (!trigger) {
            try {
                input = br.readLine();
                if (exitKey.equals(input)) {
                    serverModule.shutdown();
                    trigger = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
