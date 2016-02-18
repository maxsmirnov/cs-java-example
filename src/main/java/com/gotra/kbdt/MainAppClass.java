package com.gotra.kbdt;

import com.gotra.kbdt.core.exceptions.UncaughtExceptions;
import com.gotra.kbdt.core.service.abs.ServiceSettings;
import com.gotra.kbdt.core.service.settings.ServiceSettingsCLImpl;
import com.gotra.kbdt.modules.client.ClientModule;
import com.gotra.kbdt.modules.server.ServerModule;
import org.apache.log4j.Logger;

/**
 * @author gotra
 */

public class MainAppClass {
    private static Logger logger = Logger.getLogger(MainAppClass.class.getName());

    private static volatile MainAppClass instance;

    private UncaughtExceptions exception = new UncaughtExceptions();

    public static MainAppClass getInstance(String[] args) {
        MainAppClass localInstance = instance;
        if (localInstance == null) {
            synchronized (MainAppClass.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MainAppClass();

                    Thread.setDefaultUncaughtExceptionHandler(instance.exception);

                    instance.StartApp(args);
                }
            }
        }
        return localInstance;
    }

    private void StartApp(String[] args) {
        if (args.length <= 0) {
            logger.error("Incorrect command line args...");
            return;
            //System.exit(-1);
        }

        if (args[0].equals("-client")) {

            ClientModule cm = new ClientModule(1);
            Thread thread = new Thread(cm);
            thread.start();

        } else if (args[0].equals("-server")) {

            ServerModule sm = new ServerModule();
            Thread thread = new Thread(sm);
            thread.start();

        } else if (!args[0].equals("")) {

            ServiceSettings settings = new ServiceSettingsCLImpl(args);
            int id = settings.GetNumericParam("id");
            int portNumber = settings.GetNumericParam("port");
            if (id>0) {
                ClientModule cm = new ClientModule(id);
                Thread thread = new Thread(cm);
                thread.start();
            }
        }

    }
}

