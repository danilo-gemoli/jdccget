package jdcc.dispatcher;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import jdcc.events.Event;
import jdcc.logger.JdccLogger;


public class SingleThreadDispatcher implements Dispatcher, Runnable {
    private static long DEF_TIME_TO_SLEEP = 100;
    private long timeToSleep;

    private Queue<Event> events;
    private List<DispatcherObserver> observers;
    private boolean isRunning;

    public SingleThreadDispatcher() {
        this(DEF_TIME_TO_SLEEP);
    }

    public SingleThreadDispatcher(long timeToSleep) {
        this.timeToSleep = timeToSleep;
        events = new ConcurrentLinkedQueue<>();
        observers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public synchronized void stop() {
        isRunning = false;
    }

    @Override
    public void addEvent(Event e) {
        JdccLogger.logger.trace(
                "SingleThreadDispatcher: addEvent - name: {} - id: {}"
                , e.getClass().getCanonicalName(), e.getId());
        events.add(e);
    }

    @Override
    public void registerObserver(DispatcherObserver observer) {
        JdccLogger.logger.trace(
                "SingleThreadDispatcher: registerObserver - name: {}"
                , observer.getClass().getCanonicalName());
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(DispatcherObserver observer) {
        JdccLogger.logger.trace(
                "SingleThreadDispatcher: unregisterObserver - name: {}"
                , observer.getClass().getCanonicalName());
        observers.remove(observer);
    }

    @Override
    public void run() {
        JdccLogger.logger.trace("SingleThreadDispatcher: run");
        startDispatcher();
        JdccLogger.logger.trace("SingleThreadDispatcher: run - is running {}", isRunning());
        while (isRunning()) {
            dispatch();
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                JdccLogger.logger.error("SingleThreadDispatcher: InterruptedException", e);
            }
        }
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized void startDispatcher() {
        isRunning = true;
    }

    private void dispatch() {
        notifyEvents();
    }

    private void notifyEvents() {
        Event e;
        while ((e = events.poll()) != null) {
            Iterator<DispatcherObserver> it = observers.iterator();
            while (it.hasNext()) {
                it.next().notify(e);
            }
        }
    }
}