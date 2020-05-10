package cn.xdean.jslide.support;

import io.reactivex.Observable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class IOUtil {
    public static Observable<String> readLines(InputStream is) {
        return Observable.create(emitter -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                emitter.onNext(line);
            }
            emitter.onComplete();
        });
    }
}
