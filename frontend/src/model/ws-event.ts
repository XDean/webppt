export type RunCodeEvent = {
    id: number
    content: string
    language: string
}

export type RunLineEvent = {
    id: number
    type: string
    message: string
}

export type RunCloseEvent = {
    id: number
}