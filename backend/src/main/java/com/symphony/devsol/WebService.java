package com.symphony.devsol;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class WebService {
    private final String workflowPath = "workflows/abc.swadl.yaml";
    private final BotService botService;

    @GetMapping("logs")
    public SseEmitter stream() {
        return botService.getEmitter();
    }

    @GetMapping("read-workflow")
    public Workflow readWorkflow() throws IOException {
        return new Workflow(String.join("\n", Files.readAllLines(Paths.get(workflowPath), StandardCharsets.UTF_8)));
    }

    @PostMapping("write-workflow")
    public Workflow writeWorkflow(@RequestBody Workflow workflow) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(workflowPath));
        writer.write(workflow.contents);
        writer.close();
        return new Workflow(workflow.contents);
    }

    record Workflow(String contents) {}
}
