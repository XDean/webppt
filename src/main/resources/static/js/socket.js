const topicWebsocket = {
    _ws: null,
    _sendCache: [],
    _handlers: [],
    get: function () {
        if (!this._ws || this._ws.readyState === WebSocket.CLOSED || this._ws.readyState === WebSocket.CLOSING) {
            let ws;
            if (window.location.protocol === "http:") {
                ws = new WebSocket('ws://' + window.location.host + '/socket/topic');
            } else if (window.location.protocol === "https:") {
                ws = new WebSocket('wss://' + window.location.host + '/socket/topic');
            }
            ws.onopen = ev => {
                console.debug("topic ws open", ev);
                this._sendCache.forEach(e => {
                    this.send(e.topic, e.event, e.payload);
                });
                this._sendCache = [];
            };
            ws.onclose = ev => {
                console.debug("topic ws close", ev);
            };
            ws.onmessage = ev => {
                console.debug("topic ws event", ev);
                const data = JSON.parse(ev.data);
                const topic = data.topic;
                this._handlers.forEach(h => {
                    if (h.topic === topic) {
                        if (h.handler(data)) {
                            this._removeHandler(h)
                        }
                    }
                });
            };
            this._ws = ws;
        }
        return this._ws
    },
    send: function (topic, event, payload) {
        let e = {
            topic: topic,
            event: event,
            payload: payload,
        };
        let ws = this.get();
        if (ws.readyState === WebSocket.CONNECTING) {
            this._sendCache.push(e);
        } else if (ws.readyState === WebSocket.OPEN) {
            console.debug("topic ws send", e);
            ws.send(JSON.stringify(e));
        }
    },
    addHandler: function (topic, handler /*ev=>close*/) {
        const h = {
            topic: topic,
            handler: handler,
        };
        this._handlers.push(h);
        return () => this._removeHandler(h);
    },
    _removeHandler: function (handler) {
        let index = this._handlers.indexOf(handler);
        if (index > -1) {
            this._handlers.splice(index, 1);
        }
    }
};

Object.keys(topicWebsocket).forEach(key => {
    let value = topicWebsocket[key];
    if (typeof value === "function") {
        topicWebsocket[key] = value.bind(topicWebsocket);
    }
});