package cn.xdean.jslide.core.code.play;

import cn.xdean.jslide.support.IOUtil;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ScriptCodePlayer extends AbstractCodePlayer {
    public ScriptCodePlayer(String name, String... extensions) {
        super(name, extensions);
    }

    @Override
    public Observable<Line> run(String code) {
        return Single.fromCallable(() -> {
            Path file = createScriptFile(code);
            ProcessBuilder pb = createProcess(file);
            return pb.start();
        })
                .flatMapObservable(p -> Observable.merge(
                        IOUtil.readLines(p.getInputStream())
                                .map(s -> Line.builder().message(s).type(Line.Type.STDOUT).build())
                                .subscribeOn(Schedulers.io()),
                        IOUtil.readLines(p.getErrorStream())
                                .map(s -> Line.builder().message(s).type(Line.Type.STDERR).build())
                                .subscribeOn(Schedulers.io())));
    }

    protected Path createScriptFile(String code) throws IOException {
        Path folder = Files.createTempDirectory(String.format("code-%s-", getName()));
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
