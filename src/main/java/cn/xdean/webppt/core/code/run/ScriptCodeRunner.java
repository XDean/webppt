package cn.xdean.webppt.core.code.run;

import cn.xdean.webppt.core.process.ProcessExecutor;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ScriptCodeRunner extends AbstractCodeRunner {

    @Autowired ProcessExecutor processExecutor;

    @Override
    public Observable<Line> run(String code) {
        return Single.fromCallable(() -> createScriptFile(code))
                .map(f -> createProcess(f))
                .flatMapObservable(pb -> Single.fromCallable(() -> processExecutor.execute(pb))
                        .flatMapObservable(p -> CodeRunnerUtil.processToLineObservable(p)
                                .startWith(Line.Type.START.of(String.join(" ", pb.command())))
                                .concatWith(Observable.fromCallable(() -> Line.Type.DONE.of(p.waitFor())))
                                .doFinally(() -> p.destroy())))
                .onErrorReturn(e -> Line.Type.ERROR.of(e.getMessage()));
    }

    protected Path createTempFolder() throws IOException {
        Path folder = Files.createTempDirectory("code-script-");
        File file = folder.toFile();
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
        return folder;
    }

    protected Path createScriptFile(String code) throws IOException {
        Path folder = createTempFolder();
        Path file = Files.createFile(folder.resolve(scriptFileName()));
        return Files.write(file, code.getBytes());
    }

    protected ProcessBuilder createProcess(Path script) {
        return new ProcessBuilder()
                .directory(script.getParent().toFile())
                .command(scriptCommand());
    }

    protected abstract String[] scriptCommand();

    protected abstract String scriptFileName();
}
