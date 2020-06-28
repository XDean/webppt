package cn.xdean.webppt.core.code.run;

import cn.xdean.webppt.core.process.ProcessExecutor;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public abstract class CompileCodeRunner extends AbstractCodeRunner {
    @Autowired ProcessExecutor processExecutor;

    @Override
    public Observable<Line> run(String code) {
        return Single.fromCallable(() -> createSourceFile(code))
                .flatMapObservable(sourceFile -> {
                            ProcessBuilder compileProcess = createCompileProcess(sourceFile);
                            ProcessBuilder runProcess = createRunProcess(sourceFile);
                            return Single.fromCallable(() -> processExecutor.execute(compileProcess))
                                    .flatMapObservable(p -> CodeRunnerUtil.processToLineObservable(p)
                                            .startWith(Line.Type.START.of(String.join(" ", compileProcess.command())))
                                            .concatWith(Observable.fromCallable(() -> Line.Type.DONE.of(p.waitFor())))
                                            .doFinally(() -> p.destroy())
                                    )
                                    .onErrorResumeNext((Throwable e) -> Observable.just(
                                            Line.Type.ERROR.of(e.getMessage())
                                    ))
                                    .concatWith(Single.fromCallable(() -> processExecutor.execute(runProcess))
                                            .flatMapObservable(p -> CodeRunnerUtil.processToLineObservable(p)
                                                    .startWith(Line.Type.START.of(String.join(" ", runProcess.command())))
                                                    .concatWith(Observable.fromCallable(() -> Line.Type.DONE.of(p.waitFor())))
                                                    .doFinally(() -> p.destroy())
                                            )
                                            .onErrorResumeNext((Throwable e) -> Observable.just(
                                                    Line.Type.ERROR.of(e.getMessage())
                                            ))
                                    );
                        }
                );
    }

    protected Path createTempFolder() throws IOException {
        Path folder = Files.createTempDirectory("code-compile-");
        File file = folder.toFile();
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
        return folder;
    }

    protected Path createSourceFile(String code) throws IOException {
        Path folder = createTempFolder();
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
