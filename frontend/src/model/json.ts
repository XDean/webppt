export type JRawInfo = {
    startLineIndex: number
    endLineIndex: number
}

export type JElement = {
    name: string
    children: (JElement | JParam | JText)[]
    raw: JRawInfo
}

export type JParam = {
    key: string
    value: string
    element: string
    raw: JRawInfo
}

export type JText = {
    lines: string[]
    raw: JRawInfo
}