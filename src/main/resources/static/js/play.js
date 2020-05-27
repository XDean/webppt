CodeMirror.defineOption("play", [], function (cm, option) {
    if (option.enable) {
        createPlayPanel(cm, option);
    }
});

let runId = 1;

function createPlayPanel(cm, option) {
    const wrap = cm.getWrapperElement();

    const panel = document.createElement("div");
    panel.classList.add("code-run-panel", "close");

    const playButton = document.createElement("button");
    playButton.classList.add("play-button");
    playButton.innerText = "Play";

    if (!option.support) {
        let tooltip = document.createElement("div");
        tooltip.classList.add("unsupported-tooltip");
        tooltip.innerText = "Server doesn't support run [" + option.language + "] code";

        panel.appendChild(playButton);
        panel.appendChild(tooltip);
        wrap.appendChild(panel);
        return;
    }

    const runButton = document.createElement("button");
    runButton.classList.add("run-button");
    runButton.innerText = "Run";

    const stopButton = document.createElement("button");
    stopButton.classList.add("stop-button");
    stopButton.innerText = "Stop";

    const closeButton = document.createElement("button");
    closeButton.classList.add("close-button");
    closeButton.innerText = "Close";

    const output = document.createElement("div");
    output.classList.add("code-output");
    $(output).resizable({
        handles: 's,w,sw',
        minHeight: 70,
        minWidth: 100,
    });

    const outputContent = document.createElement("div");
    outputContent.classList.add("code-output-content");
    output.appendChild(outputContent);

    panel.appendChild(playButton);
    panel.appendChild(runButton);
    panel.appendChild(stopButton);
    panel.appendChild(closeButton);

    playButton.addEventListener("click", ev => {
        panel.classList.toggle("close");
        panel.classList.toggle("open");
    });

    closeButton.addEventListener("click", ev => {
        panel.classList.toggle("close");
        panel.classList.toggle("open");
    });

    runButton.addEventListener("click", ev => {
        let myId = runId++;
        outputContent.innerHTML = "";
        topicWebsocket.addHandler("code", event => {
            switch (event.event) {
                case "line":
                    if (event.payload.id !== myId) {
                        return
                    }
                    let line = document.createElement("div");
                    line.classList.add("output-line", event.payload.type.toLowerCase());
                    line.innerText = event.payload.message;
                    outputContent.appendChild(line);
                    break;
                case "close":
                    panel.classList.toggle("run");
                    return true;
            }
        });
        topicWebsocket.send("code", "run", {
            id: myId,
            content: cm.getValue(),
            language: option.language,
        });
        panel.classList.toggle("run");
    });

    stopButton.addEventListener("click", ev => {

    });

    wrap.appendChild(panel);
    wrap.appendChild(output);
}