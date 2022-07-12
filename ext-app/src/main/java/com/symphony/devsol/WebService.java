package com.symphony.devsol;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class WebService {
    private final String logPath = "wdk-bot/wdk.log";
    private final String workflowPath = "workflows/abc.swadl.yaml";
    private final int limit = 500;
    private final BotService botService;

    @PostConstruct
    public void initBot() throws IOException {
        botService.runBot();
    }

    @GetMapping("logs")
    public String getLogs() {
        return readLog(logPath);
    }

    @GetMapping("read-workflow")
    public String readWorkflow() throws IOException {
        return String.join("\n", Files.readAllLines(Paths.get(workflowPath), StandardCharsets.UTF_8));
    }

    @PostMapping("write-workflow")
    public String writeWorkflow(@RequestBody String contents) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(workflowPath));
        writer.write(contents);
        writer.close();
        return contents;
    }

    public String readLog(String path) {
        int lines = 0;
        File file = new File(path);
        StringBuilder builder = new StringBuilder();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long fileLength = file.length() - 1;
            randomAccessFile.seek(fileLength);
            for (long pointer = fileLength; pointer >= 0; pointer--) {
                randomAccessFile.seek(pointer);
                char c;
                c = (char) randomAccessFile.read();
                if (c == '\n' && lines++ > limit) {
                    break;
                }
                builder.append(c);
            }
            builder.reverse();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
