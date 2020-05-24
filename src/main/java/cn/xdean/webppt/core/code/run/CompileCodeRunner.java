package cn.xdean.webppt.core.code.run;

import cn.xdean.webppt.support.IOUtil;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class CompileCodeRunner implements CodeRunner {
    @Override
    public Observable<Line> run(String code) {
        return Single.fromCallable(() -> createSourceFile(code))
                .flatMapObservable(sourceFile -> Single.fromCallable(() -> createCompileProcess(sourceFile).start())
                        .flatMapObservable(p -> CodeRunnerUtil.processToLineObservable(p)
                                .startWith(Line.Type.STATUS.of("Compiling"))
                                .concatWith(Observable.fromCallable(() -> Line.Type.SYSTEM.of("Compile Exit Code: " + p.waitFor())))
                                .concatWith(Single.just(Line.Type.STATUS.of("Compile Done")))
                                .onErrorReturn(e -> Line.Type.SYSTEM.of("Compile Error Happened: " + e.getMessage()))
                        )
                        .concatWith(Single.fromCallable(() -> createRunProcess(sourceFile).start())
                                .flatMapObservable(p -> CodeRunnerUtil.processToLineObservable(p)
                                        .startWith(Line.Type.STATUS.of("Running"))
                                        .concatWith(Observable.fromCallable(() -> Line.Type.SYSTEM.of("Run Exit Code: " + p.waitFor())))
                                        .concatWith(Single.just(Line.Type.STATUS.of("Run Done")))
                                        .onErrorReturn(e -> Line.Type.SYSTEM.of("Run Error Happened: " + e.getMessage()))
                                )
                        )
                );
    }

    protected Path createSourceFile(String code) throws IOException {
        Path folder = Files.createTempDirectory("code-compile-");
        Path file = Files.createFile(folder.resolve(scriptFileName()));
        return Files.write(file, code.getBytes());
    }

    protected ProcessBuilder createCompileProcess(Path sourceFile) {
        return new ProcessBuilder()
                .directory(sourceFile.getParent().toFile())
                .command(compileCommand());
    }

    protected ProcessBuilder createRunProcess(Path sourceFile) {
        return new ProcessBuilder()
                .directory(sourceFile.getParent().toFile())
                .command(runCommand());
    }

    protected abstract String[] compileCommand();

    protected abstract String[] runCommand();

    protected abstract String scriptFileName();
}
