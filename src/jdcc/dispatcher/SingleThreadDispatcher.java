package jdcc.dispatcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jdcc.events.commands.Command;
import jdcc.events.messages.Message;
import jdcc.logger.JdccLogger;


public class SingleThreadDispatcher implements Dispatcher, Runnable {
    private static long DEF_TIME_TO_SLEEP = 100;
    private long timeToSleep;

    private Lock stopLock;
    private Lock messagesLock;
    private Lock commandsLock;
    private Queue<Message> messages;
    private Queue<Command> commands;
    private List<DispatcherObserver> messageObservers;
    private List<DispatcherObserver> commandObservers;
    private boolean isRunning;

    public SingleThreadDispatcher() {
        this(DEF_TIME_TO_SLEEP);
    }

    public SingleThreadDispatcher(long timeToSleep) {
        this.timeToSleep = timeToSleep;
        messagesLock = new ReentrantLock();
        commandsLock = new ReentrantLock();
        stopLock = new ReentrantLock();
        messages = new ConcurrentLinkedQueue<>();
        commands = new ConcurrentLinkedQueue<>();
        messageObservers = new LinkedList<>();
        commandObservers = new LinkedList<>();
    }

    @Override
    public void addCommand(Command command) {
        try {
            commandsLock.lock();
            doAddCommand(command);
        } finally {
            commandsLock.unlock();
        }
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        // TODO
    }

    @Override
    public void addMessage(Message message) {
        try {
            messagesLock.lock();
            doAddMessage(message);
        } finally {
            messagesLock.unlock();
        }
    }

    @Override
    public void registerCommandsObserver(DispatcherObserver observer) {
        try {
            commandsLock.lock();
            doRegisterObserver(commandObservers, observer);
        } finally {
            commandsLock.unlock();
        }
    }

    @Override
    public void unregisterCommandsObserver(DispatcherObserver observer) {
        try {
            commandsLock.lock();
            doUnregisterObserver(commandObservers, observer);
        } finally {
            commandsLock.unlock();
        }
    }

    @Override
    public void registerMessagesObserver(DispatcherObserver observer) {
        try {
            messagesLock.lock();
            doRegisterObserver(messageObservers, observer);
        } finally {
            messagesLock.unlock();
        }
    }

    @Override
    public void unregisterMessagesObserver(DispatcherObserver observer) {
        try {
            messagesLock.lock();
            doUnregisterObserver(messageObservers, observer);
        } finally {
            messagesLock.unlock();
        }
    }

    @Override
    public void run() {
        JdccLogger.logger.info("dispatcher start");
        startDispatcher();
        JdccLogger.logger.info("is running {}", isRunning());
        while (isRunning()) {
            dispatch();
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                JdccLogger.logger.error("Dispatcher sleep error", e);
            }
        }
    }

    public void stopDispatcher() {
        try {
            stopLock.lock();
            isRunning = false;
        } finally {
            stopLock.unlock();
        }
    }

    public boolean isRunning() {
        try {
            stopLock.lock();
            return isRunning;
        } finally {
            stopLock.unlock();
        }
    }

    public void startDispatcher() {
        try {
            stopLock.lock();
            isRunning = true;
        } finally {
            stopLock.unlock();
        }
    }

    private void dispatch() {
        notifyNewCommands();
        notifyNewMessages();
    }

    private void notifyNewCommands() {
        try {
            commandsLock.lock();
            commands.forEach(c -> commandObservers.forEach(o -> o.notify(c)));
            commands.clear();
        } finally {
            commandsLock.unlock();
        }
    }

    private void notifyNewMessages() {
        try {
            messagesLock.lock();
            messages.forEach(m -> messageObservers.forEach(o -> o.notify(m)));
            messages.clear();
        } finally {
            messagesLock.unlock();
        }
    }

    private void doUnregisterObserver(List<DispatcherObserver> observers, DispatcherObserver
            toUnregister) {
        Iterator<DispatcherObserver> it = observers.iterator();
        while (it.hasNext()) {
            DispatcherObserver currentObserver = it.next();
            if (currentObserver == toUnregister) {
                it.remove();
                break;
            }
        }
    }

    private boolean doRegisterObserver(List<DispatcherObserver> observers, DispatcherObserver
            toRegister) {
        if (!observers.contains(toRegister)) {
            observers.add(toRegister);
            JdccLogger.logger.debug("observer {} registered", toRegister);
            return true;
        }
        return false;
    }

    private void doAddMessage(Message message) {
        messages.add(message);
    }

    private void doAddCommand(Command command) {
        commands.add(command);
    }
}
