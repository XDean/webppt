import React, {Dispatch, MutableRefObject, SetStateAction, useState} from "react";

//https://github.com/Darth-Knoppix/example-react-fullscreen/blob/master/src/utils/useFullscreenStatus.js
export default function useFullscreen<T extends Element | null>(elRef: MutableRefObject<T>): [boolean, Dispatch<boolean>] {
    const [isFullscreen, setIsFullscreen] = useState(document.fullscreenElement != null);

    const setFullscreen = (full: boolean) => {
        if (elRef.current == null) return;

        if (full) {
            elRef.current
                .requestFullscreen()
                .then(() => setIsFullscreen(document.fullscreenElement != null))
                .catch(() => setIsFullscreen(false));
        } else {
            document.exitFullscreen()
                .then(() => setIsFullscreen(document.fullscreenElement != null))
                .catch(() => setIsFullscreen(false));
        }
    };

    React.useLayoutEffect(() => {
        document.onfullscreenchange = () => setIsFullscreen(document.fullscreenElement != null);

        return () => {
            document.onfullscreenchange = null
        };
    });

    return [isFullscreen, setFullscreen];
}