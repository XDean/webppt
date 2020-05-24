package cn.xdean.webppt.core.code.run;

import cn.xdean.webppt.support.IOUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public abstract class CodeRunnerUtil {
    public static Observable<CodeRunner.Line> processToLineObservable(Process p) {
        return Observable.merge(
                IOUtil.readLines(p.getInputStream())
                        .map(s -> CodeRunner.Line.Type.STDOUT.of(s))
                        .subscribeOn(Schedulers.io()),
                IOUtil.readLines(p.getErrorStream())
                        .map(s -> CodeRunner.Line.Type.STDERR.of(s))
                        .subscribeOn(Schedulers.io()));
    }
}
