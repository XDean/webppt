export type Language = {
    name: string,
    extensions: string[],
    mime: string,
    codemirrorJs: string,
}

export const Languages: Language[] = [
    {
        name: "java",
        extensions: ["java"],
        mime: "text/x-java",
        codemirrorJs: "clike/clike.js",
    },
    {
        name: "go",
        extensions: ["go"],
        mime: "text/x-go",
        codemirrorJs: "go/go.js",
    },
    {
        name: "python",
        extensions: ["py"],
        mime: "text/x-python",
        codemirrorJs: "python/python.js",
    }
];

export function findLanguageByExt(ext: string): Language | null {
    for (const lang of Languages) {
        if (lang.extensions.indexOf(ext) !== -1) {
            return lang;
        }
    }
    return null;
}

export function findLanguageByName(name: string): Language | null {
    for (const lang of Languages) {
        if (lang.name === name) {
            return lang;
        }
    }
    return null;
}