package baecon.devgames.util;

import java.util.concurrent.Executor;

/**
 * TODO: provide class level documentation
 */
public class CurrentThreadExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}