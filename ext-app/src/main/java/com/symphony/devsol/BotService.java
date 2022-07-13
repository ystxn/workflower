package com.symphony.devsol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {
    private final Environment env;

    @Async
    public void runBot() throws IOException {
        String command = "java -jar workflow-bot-app.jar --spring.profiles.active=" + env.getActiveProfiles()[0];
        Process p = Runtime.getRuntime().exec(command, null, new File("wdk-bot"));
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
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}