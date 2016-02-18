package com.gotra.kbdt.modules.client;

import com.gotra.kbdt.core.Protocol;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

/**
 * @author gotra
 */

public class ClientModule implements Runnable {

    private static final Logger logger = LogManager.getLogger(ClientModule.class.getName());

    private int id;
    private List<Integer> registeredIdsList;

    public ClientModule(int id) {
        this.id = id;
    }

    public void run() {


        Socket s = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        try {
            s = new Socket("localhost", 8888);

            writer = new PrintWriter(s.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String fromServer = null;
            String toServer = null;

            //authorise
            fromServer = reader.readLine();
            logger.info("There should be id request from server! :: " + fromServer);
            if (!Protocol.GIVE_YOUR_ID.equals(fromServer)) {
                logger.error("Server violates protocol!");
                writer.println(Protocol.END_CONVERSATION);
                writer.flush();
                return;
            } else {
                logger.info("Sending id " + id);
                writer.println("" + id);
                writer.flush();
            }
            //expecting OK from server
            fromServer = reader.readLine();
            if (!Protocol.OK.equals(fromServer)) {
                logger.info("Server responded not OK! server response: " + fromServer);
                logger.info("Ending conversation!");
                writer.println(Protocol.END_CONVERSATION);
                writer.flush();
                return;
            }
            //starting main interaction
            Random r = new Random();
            for (int i = 1; i <= r.nextInt(100) + 10; i++) {

                int randomNum = r.nextInt(i) + 1;

                Thread.currentThread().sleep(100);
                writer.println("" + randomNum);
                writer.flush();
                fromServer = reader.readLine();
                if (Protocol.END_CONVERSATION.equals(fromServer)) {
                    logger.info("Server ended conversation");
                    return;
                }
                logger.info(" id # " + id + "    Request: " + randomNum + ", response: " + fromServer);
            }


            //end conversation
            logger.info("All data was sent, ending conversation");
            writer.println(Protocol.END_CONVERSATION);
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 /*   public static void main(String[] args) throws IOException, InterruptedException {
        ClientModule cm = new ClientModule(1);
        ClientModule cm1 = new ClientModule(2);
        ClientModule cm2 = new ClientModule(3);
        ClientModule cm3 = new ClientModule(4);
        ClientModule cm4 = new ClientModule(5);
        ClientModule cm5 = new ClientModule(6);
        Thread thread = new Thread(cm);
        Thread thread1 = new Thread(cm1);
        Thread thread2 = new Thread(cm2);
        Thread thread3 = new Thread(cm3);
        Thread thread4 = new Thread(cm4);
        Thread thread5 = new Thread(cm5);
        thread.start();
        Thread.sleep(20);
        thread1.start();
        Thread.sleep(20);
        thread2.start();
        Thread.sleep(20);
        thread3.start();
        Thread.sleep(20);
        thread4.start();
        Thread.sleep(20);
        thread5.start();
    }*/
}
