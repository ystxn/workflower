require.config({
    paths: { vs: "https://cdn.jsdelivr.net/npm/monaco-editor@0.33.0/min/vs" }
});

const fetchLogs = () => {
    const logs = document.querySelector("#logs");
    let eventSource = new EventSource("/logs");
    eventSource.onmessage = function(event) {
        logs.innerHTML = logs.innerHTML + (logs.innerHTML.length > 0 ? "\n" : "") + event.data;
        if (document.querySelector("#tail").checked) {
            logs.scrollTop = logs.scrollHeight;
        }
    };
};

const readWorkflow = () => {
    fetch('/read-workflow')
        .then(response => response.text())
        .then(text => {
            require(["vs/editor/editor.main"], function () {
                window.editor = monaco.editor.create(document.getElementById("editor"), {
                    value: text,
                    language: "yaml",
                    theme: 'dracula',
                    automaticLayout: true
                });
            });
        });
};

const writeWorkflow = () => {
    const config = {
        method: 'POST',
        body: window.editor.getValue()
    }
    fetch('/write-workflow', config)
        .then(response => response.text())
        .then(() => {
            document.querySelector("#status").textContent = "Saved!";
            setTimeout(() => {
                document.querySelector("#status").textContent = "";
            }, 2000);
        });
};

fetchLogs();
readWorkflow();
