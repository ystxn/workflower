package com.symphony.devsol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.*;

@Slf4j
@Service
public class BotService {
    @Async
    public void runBot() throws IOException {
        Process p = Runtime.getRuntime().exec("java -jar workflow-bot-app.jar", null, new File("wdk-bot"));
        (new Thread(new StreamListener(p.getInputStream()))).start();
        (new Thread(new StreamListener(p.getErrorStream()))).start();
    }

    private static class StreamListener implements Runnable {
        private final BufferedReader reader;

        public StreamListener(InputStream s) {
            reader = new BufferedReader(new InputStreamReader(s));
        }

        public void run() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[WDK Bot] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
