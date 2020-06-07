export type XRawInfo = {
    startLineIndex: number
    endLineIndex: number
}

export type XElement = {
    name: string
    children: (XElement | XParam | XText)[]
    raw: XRawInfo
}

export type XParam = {
    key: string
    value: string
    element: string
    raw: XRawInfo
}

export type XText = {
    lines: string[]
    raw: XRawInfo
}