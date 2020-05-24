package cn.xdean.webppt.core.code.run;

import cn.xdean.webppt.support.IOUtil;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ScriptCodeRunner implements CodeRunner {
    @Override
    public Observable<Line> run(String code) {
        return Single.fromCallable(() -> {
            Path file = createScriptFile(code);
            ProcessBuilder pb = createProcess(file);
            return pb.start();
        })
                .flatMapObservable(p -> Observable.merge(
                        IOUtil.readLines(p.getInputStream())
                                .map(s -> Line.Type.STDOUT.of(s))
                                .subscribeOn(Schedulers.io()),
                        IOUtil.readLines(p.getErrorStream())
                                .map(s -> Line.Type.STDERR.of(s))
                                .subscribeOn(Schedulers.io()))
                        .concatWith(Observable.fromCallable(() -> Line.Type.SYSTEM.of("Exit Code: " + p.waitFor()))))
                .startWith(Line.Type.STATUS.of("running"))
                .concatWith(Single.just(Line.Type.STATUS.of("done")))
                .onErrorReturn(e -> Line.Type.SYSTEM.of(e.getMessage()));
    }

    protected Path createScriptFile(String code) throws IOException {
        Path folder = Files.createTempDirectory("code-script-");
        Path file = Files.createFile(folder.resolve(scriptFileName()));
        return Files.write(file, code.getBytes());
    }

    protected ProcessBuilder createProcess(Path script) {
        return new ProcessBuilder()
                .directory(script.getParent().toFile())
                .command(scriptCommand(script));
    }

    protected abstract String[] scriptCommand(Path script);

    protected abstract String scriptFileName();
}
