package com.gotra.kbdt.modules.server;

import com.gotra.kbdt.core.ClientHandler;
import com.gotra.kbdt.core.DataJournalManager;
import com.gotra.kbdt.core.KeyListener;
import com.gotra.kbdt.core.domain.DataJournal;
import com.gotra.kbdt.core.domain.DataTask;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerModule implements Runnable {

    private static Logger logger = Logger.getLogger(ServerModule.class.getName());
    private ConcurrentLinkedQueue<DataTask> workingQueue;
    private DataJournalManager journalManager;
    private DataJournal dataJournal;
    private List<Integer> registeredIdsList;

    private ServerSocket serverSocket;
    private int port;
    private int threadPoolSize;
    private ExecutorService executor;

    private boolean exitTrigger = false;

    public void run() {


        Properties properties = new Properties();
        registeredIdsList = new ArrayList<>();
        workingQueue = new ConcurrentLinkedQueue<>();
        try {
            properties.load(new FileInputStream("src/main/resources/config.dat"));
            port = Integer.parseInt(properties.getProperty("serverPort"));
            String clientIds = properties.getProperty("idList");
            String[] tmp = clientIds.split(",");
            threadPoolSize = tmp.length;
            for (String s : tmp) {
                registeredIdsList.add(Integer.parseInt(s));
            }

            serverSocket = new ServerSocket(port);
            dataJournal = new DataJournal();
            journalManager = new DataJournalManager(dataJournal, workingQueue);

            executor = Executors.newFixedThreadPool(threadPoolSize);

            //start journal manager
            Thread jmThread = new Thread(journalManager);
            jmThread.start();

            logger.info("ServerModule initialized successfully! Server started on port " + port +
                    ", handlers pool size: " + threadPoolSize);

            Thread exitListenerThread = new Thread(new KeyListener(this));
            exitListenerThread.start();

            while (!executor.isShutdown()) {
                executor.submit(new ClientHandler(serverSocket.accept(), registeredIdsList, workingQueue));
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

/*    public static void main(String[] args) throws IOException {
        ServerModule sm = new ServerModule();
        Thread thread = new Thread(sm);
        thread.start();
    }
*/
    public void shutdown() {
        logger.info("Server shutting down");
        for (Integer id : registeredIdsList) {
            journalManager.printSessionData(id);
        }
        executor.shutdownNow();
        return;
        //System.exit(0);
    }
}
