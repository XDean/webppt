export type RunCodeEvent = {
    id: number
    content: string
    language: string
}

export type RunLineEvent = {
    id: number
    type: "STDOUT" | "STDERR" | "START" | "STOP" | "DONE" | "ERROR" | string
    message: string
}

export type RunCloseEvent = {
    id: number
}

export type RunStopEvent = {
    id: number
}