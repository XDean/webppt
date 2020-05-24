CodeMirror.defineOption("play", [], function (cm, val) {
    if (val === true) {
        const wrap = cm.getWrapperElement();
        wrap.appendChild(createPlayPanel(cm));
    }
});

function createPlayPanel(cm) {
    const panel = document.createElement("div");
    panel.classList.add("code-run-panel", "close");

    const playButton = document.createElement("button");
    playButton.classList.add("play-button");
    playButton.innerText = "Play";

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
    output.classList.add("output");
    $(output).resizable({
        handles: 's,w,sw',
        minHeight: 70,
        minWidth: 100,
    });

    panel.appendChild(playButton);
    panel.appendChild(runButton);
    panel.appendChild(stopButton);
    panel.appendChild(closeButton);
    panel.appendChild(output);

    playButton.addEventListener("click", ev => {
        panel.classList.toggle("close");
        panel.classList.toggle("open");
    });

    closeButton.addEventListener("click", ev => {
        panel.classList.toggle("close");
        panel.classList.toggle("open");
    });

    return panel;
}

