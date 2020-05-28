package cn.xdean.webppt.core.code.run;

import cn.xdean.webppt.core.process.ProcessExecutor;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ScriptCodeRunner extends AbstractCodeRunner {

    @Autowired ProcessExecutor processExecutor;

    @Override
    public Observable<Line> run(String code) {
        return Single.fromCallable(() -> {
            Path file = createScriptFile(code);
            ProcessBuilder pb = createProcess(file);
            return processExecutor.execute(pb);
        })
                .flatMapObservable(p -> CodeRunnerUtil.processToLineObservable(p)
                        .startWith(Line.Type.STATUS.of("Run"))
                        .concatWith(Single.just(Line.Type.STATUS.of("Done")))
                        .concatWith(Observable.fromCallable(() -> Line.Type.SYSTEM.of("Exit Code: " + p.waitFor())))
                        .doFinally(() -> p.destroy()))
                .onErrorReturn(e -> Line.Type.SYSTEM.of("Error Happened: " + e.getMessage()));
    }

    protected Path createScriptFile(String code) throws IOException {
        Path folder = Files.createTempDirectory("code-script-");
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
