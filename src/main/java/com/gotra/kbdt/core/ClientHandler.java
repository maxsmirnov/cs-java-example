package com.gotra.kbdt.core;

import com.gotra.kbdt.core.domain.DataTask;
import com.gotra.kbdt.core.domain.OperationType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author gotra
 */

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class.getName());

    private Socket socket;
    private List<Integer> registeredIdsList;
    private ConcurrentLinkedQueue<DataTask> workingQueue;

    private boolean shutDownTrigger;


    public ClientHandler(Socket socket, List<Integer> registeredIdsList, ConcurrentLinkedQueue<DataTask> workingQueue) {
        this.socket = socket;
        this.registeredIdsList = registeredIdsList;
        this.workingQueue = workingQueue;
        shutDownTrigger = false;
        //logger.info("New client handler created!");
    }

    @Override
    public void run() {
        Date sessionStartTime = new Date();

        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //authorise
            writer.println(Protocol.GIVE_YOUR_ID);
            writer.flush();
            String idStr = reader.readLine();
            int id = Integer.parseInt(idStr);
            if (!registeredIdsList.contains(id)) {
                logger.warn("Unregistered id: " + idStr + ". Connection will be closed.");
                writer.println(Protocol.END_CONVERSATION);
                writer.flush();
                return;
            } else {
                //write to journal session start
                DataTask sessionStartTask =
                        new DataTask(OperationType.CREATE_SESSION_DATA, id, "" + sessionStartTime.getTime());
                workingQueue.add(sessionStartTask);

                logger.info("Client # " + id + " authorised successfully.");
                writer.println(Protocol.OK);
                writer.flush();
            }
            String fromClient = null;
            String toClient = null;
            while (!shutDownTrigger) {
                fromClient = reader.readLine();
                if (fromClient.equals(Protocol.END_CONVERSATION)) {
                    logger.info("Client # " + id + " shuts down conversation");

                    //write to journal session end time
                    DataTask sessionEndTimeTask =
                            new DataTask(OperationType.SET_SESSION_END_TIME, id, "" + (new Date()).getTime());
                    workingQueue.add(sessionEndTimeTask);

                    return;
                }

                int i = Integer.parseInt(fromClient);
                i *= i;
                toClient = "" + i;

                //write to journal request response pair
                DataTask conversationLogTask =
                        new DataTask(OperationType.LOG_CONVERSATION, id, fromClient + "," + toClient);
                workingQueue.add(conversationLogTask);

                writer.println(toClient);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
