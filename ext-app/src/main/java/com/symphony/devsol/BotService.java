package com.symphony.devsol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.*;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {
    private final Environment env;
    @Getter
    private SseEmitter emitter;

    private void resetEmitter() {
        emitter = new SseEmitter();
        emitter.onTimeout(this::resetEmitter);
    }

    @Async
    public void runBot() throws IOException {
        resetEmitter();
        String command = "java -jar workflow-bot-app.jar --spring.profiles.active=" + env.getActiveProfiles()[0];
        Process p = Runtime.getRuntime().exec(command, null, new File("wdk-bot"));
        (new Thread(new StreamListener(p.getInputStream()))).start();
        (new Thread(new StreamListener(p.getErrorStream()))).start();
    }

    public void writeLog(String log) {
        try {
            emitter.send(SseEmitter.event()
                .data(log)
                .id(Instant.now().toString())
                .name("message"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class StreamListener implements Runnable {
        private final BufferedReader reader;

        public StreamListener(InputStream s) {
            reader = new BufferedReader(new InputStreamReader(s));
        }

        public void run() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    writeLog(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
