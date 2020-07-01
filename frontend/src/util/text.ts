export function replaceDollarVar(input: string, lookup: (key: string) => string | undefined):string {
    const pattern = /(?<!\$)\${(\w+)(?::((?:[^}\\]|\\.)*))?}/g;
    let res = input;
    while (true) {
        const matcher = pattern.exec(input);
        if (!matcher) {
            break;
        }
        const key = matcher[1];
        const value = lookup(key) || matcher[2] || "";
        const offset = res.length - input.length;
        res = replaceRange(res, matcher.index + offset, matcher[0].length, value);
    }
    return res;
}

function replaceRange(s: string, start: number, len: number, substitute: string): string {
    return s.substring(0, start) + substitute + s.substring(start + len);
}