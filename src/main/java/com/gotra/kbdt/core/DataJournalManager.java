package com.gotra.kbdt.core;

import com.gotra.kbdt.core.domain.DataJournal;
import com.gotra.kbdt.core.domain.DataTask;
import com.gotra.kbdt.core.domain.RequestResponsePair;
import com.gotra.kbdt.core.domain.SessionData;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author gotra
 */

/**
 * reads/writes data to the journal
 */

public class DataJournalManager implements Runnable {
    private static final Logger logger = LogManager.getLogger(DataJournalManager.class.getName());
    private DataJournal journal;
    private ConcurrentLinkedQueue<DataTask> workingQueue;

    public DataJournalManager(DataJournal journal, ConcurrentLinkedQueue<DataTask> workingQueue) {
        this.journal = journal;
        this.workingQueue = workingQueue;
    }

    @Override
    public void run() {
        logger.info("Journal manager started successfully!");
        while (true) {
            if (!workingQueue.isEmpty()) {
                executeTask(workingQueue.poll());
            }
        }
    }

    private void executeTask(DataTask task) {
        switch (task.getOperation()) {
            case CREATE_SESSION_DATA: {
                SessionData sessionData =
                        new SessionData(task.getClientId(), new Date(Long.parseLong(task.getData())));
                journal.setSessionData(task.getClientId(), sessionData);
                break;
            }
            case SET_SESSION_END_TIME: {
                SessionData sessionData = journal.getSessionData(task.getClientId());
                sessionData.setSessionEndTime(new Date(Long.parseLong(task.getData())));
                break;
            }
            case LOG_CONVERSATION: {
                SessionData sessionData = journal.getSessionData(task.getClientId());
                String[] tmp = task.getData().split(",");
                RequestResponsePair pair = new RequestResponsePair(tmp[0], tmp[1]);
                sessionData.getConversationLog().add(pair);
                break;
            }
            default: {
                logger.warn("Unknown operation: " + task.getOperation().name());
            }
        }
    }

    public void printSessionData(int id) {
        if (journal.getSessionData(id) == null) {
            return;
        }

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        SessionData sessionData = journal.getSessionData(id);
        logger.info("-------------------Session data----------------------");
        logger.info("Client id: " + id);
        logger.info("Session start time: " + df.format(sessionData.getSessionStartTime()));
        logger.info("Session end time: " + df.format(sessionData.getSessionEndTime()));
        logger.info("Session time: " + sessionData.getSessionTimeMillis() + " (ms)");
        logger.info("Conversation log: ");
        for (RequestResponsePair pair : sessionData.getConversationLog()) {
            logger.info("#" + id + ") [request] " + pair.getRequest() + ", [response] " + pair.getResponse());
        }
    }
}
