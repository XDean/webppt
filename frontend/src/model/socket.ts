export class TopicEvent<T> {
    constructor(
        public topic: string,
        public event: string,
        public payload: T,
    ) {
    }
}

export interface TopicHandler<T> {
    topics: string[]

    /**
     * Handle the event and return if handler close.
     * @return return true to remove the handler
     */
    handle(event: TopicEvent<T>): boolean
}

export class TopicSocket {
    private ws?: WebSocket;
    private sendBeforeOpen: TopicEvent<any>[] = [];
    private handlers: TopicHandler<any>[] = [];
    private closed = false;
    private _url: string;

    constructor(
        url: string
    ) {
        this._url = url;
    }

    get url(): string {
        return this._url;
    }

    set url(url: string) {
        this._url = url;
        if (this.ws){
            this.ws.close();
        }
        this.ws = this.initWS();
    }

    send<T>(event: TopicEvent<T>) {
        if (!this.closed) {
            let ws = this.get();
            if (ws.readyState === WebSocket.CONNECTING) {
                this.sendBeforeOpen.push(event);
            } else if (ws.readyState === WebSocket.OPEN) {
                console.debug("topic ws send", event);
                ws.send(JSON.stringify(event));
            }
        }
    }

    addHandler(handler: TopicHandler<any>) {
        this.handlers.push(handler);
        return () => this.removeHandler(handler);
    }

    removeHandler(handler: TopicHandler<any>) {
        let index = this.handlers.indexOf(handler);
        if (index > -1) {
            this.handlers.splice(index, 1);
        }
    }

    close() {
        this.closed = true;
        this.ws?.close();
    }

    private get(): WebSocket {
        if (!this.closed && (!this.ws || this.ws.readyState === WebSocket.CLOSED || this.ws.readyState === WebSocket.CLOSING)) {
            this.ws = this.initWS();
        }
        return this.ws!;
    }

    private initWS(): WebSocket {
        let ws = new WebSocket(this.url);
        ws.onopen = ev => {
            console.debug("topic ws open", ev);
            this.sendBeforeOpen.forEach(e => {
                this.send(e);
            });
            this.sendBeforeOpen = [];
        };
        ws.onclose = ev => {
            console.debug("topic ws close", ev);
        };
        ws.onmessage = ev => {
            console.debug("topic ws event", ev);
            const data = JSON.parse(ev.data);
            const topic = data.topic;
            this.handlers.forEach(h => {
                if (h.topics.indexOf(topic) !== -1) {
                    if (h.handle(data)) {
                        this.removeHandler(h)
                    }
                }
            });
        };
        return ws;
    }
}