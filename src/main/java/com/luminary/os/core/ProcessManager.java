package com.luminary.os.core;

import com.luminary.os.utils.Pair;
import lombok.Getter;
import com.luminary.os.OS;
import com.luminary.os.core.services.Service;
import com.luminary.os.events.ProcessTimeoutEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ProcessManager {
    private int currID = 0;
    private int elasped = 0;
    private static ProcessManager procManager;
    private Thread manager;
    private LinkedList<Pair<Integer, Integer>> processHistory = new LinkedList<>();
    private final Deque<Process> queuedProcess = new ArrayDeque<>();
    @Getter
    private final ConcurrentHashMap<Integer, Process> runningProcesses = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public void add(@NotNull Process t) {
        queuedProcess.add(t);
    }

    public Thread getProcess(int id) {
        return runningProcesses.get(id).getThread();

    }
    public void kill(int id) {
        Process proc = runningProcesses.get(id);
        if(proc != null) {
            currID--;
            proc.stop();
            runningProcesses.remove(id);
        } else {
            System.out.println("Unknown Process ID...");
        }
    }

    public void shutdown() {
        executor.shutdown();
        runningProcesses.forEach((k, v) -> { v.getThread().interrupt(); });
        runningProcesses.clear();
    }
    public void start() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                if(processHistory.size() >= 100) processHistory.removeFirst();
                processHistory.push(new Pair<>(elasped, runningProcesses.size()));
                elasped++;
            } catch (InterruptedException e) {
                System.out.println("Error when trying to load ");
            }
        }).start();
        executor.scheduleAtFixedRate(() -> {
            if (!queuedProcess.isEmpty()) {
                Process process = queuedProcess.poll();
                Thread thread = process.start();
                if(thread != null) {
                    runningProcesses.put(currID, process);
                    process.setId(currID);
                    currID++;
                    // Watcher
                    if(!(process instanceof Service)) {
                        new Thread(() -> {
                            try {
                                thread.join(process.getTimeoutMillis());
                                if (thread.isAlive()) {
                                    thread.interrupt();
                                    OS.getEventHandler().post(new ProcessTimeoutEvent(process, process.getId()));
                                    runningProcesses.remove(process.getId());
                                } else {
                                    runningProcesses.remove(process.getId());
                                }
                            }
                            catch (InterruptedException ignored) {}
                        }).start();
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    private ProcessManager() {}
    public static ProcessManager getProcessManager() {
        if(procManager == null) {
            procManager = new ProcessManager();
        }
        return procManager;
    }
}
