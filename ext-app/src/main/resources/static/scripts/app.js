require.config({
    paths: { vs: "https://cdn.jsdelivr.net/npm/monaco-editor@0.33.0/min/vs" }
});

const fetchLogs = () => {
    fetch('https://localhost:10443/logs')
        .then(response => response.text())
        .then(text => {
            const logs = document.querySelector("#logs");
            logs.innerHTML = text;
            logs.scrollTop = logs.scrollHeight;
            setTimeout(fetchLogs, 1000);
        });
};

const readWorkflow = () => {
    fetch('https://localhost:10443/read-workflow')
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
    fetch('https://localhost:10443/write-workflow', config)
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
