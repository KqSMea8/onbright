package com.bright.apollo.service;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgReceiver {
    public static final int WAIT_SECONDS = 1;

    // if there are too many queues, a clear method could be involved after deleting the queue
    protected static final Map<String, Object> sLockObjMap = new HashMap<String, Object>();
    protected static Map<String, Boolean> sPollingMap = new ConcurrentHashMap<String, Boolean>();

    protected Object lockObj;
    protected String queueName;
    protected CloudQueue cloudQueue;
    protected int workerId;

    public MsgReceiver(int id, MNSClient mnsClient, String queue) {
        cloudQueue = mnsClient.getQueueRef(queue);
        queueName = queue;
        workerId = id;

        synchronized (sLockObjMap) {
            lockObj = sLockObjMap.get(queueName);
            if (lockObj == null) {
                lockObj = new Object();
                sLockObjMap.put(queueName, lockObj);
            }
        }
    }

    public boolean setPolling()
    {
        synchronized (lockObj) {
            Boolean ret = sPollingMap.get(queueName);
            if (ret == null || !ret) {
                sPollingMap.put(queueName, true);
                return true;
            }
            return false;
        }
    }

    public void clearPolling()
    {
        synchronized (lockObj) {
            sPollingMap.put(queueName, false);
            lockObj.notifyAll();
            System.out.println("Everyone WakeUp and Work!");
        }
    }

    public Message receiveMessage()
    {
        boolean polling = false;
        while (true) {
            synchronized (lockObj) {
                Boolean p = sPollingMap.get(queueName);
                if (p != null && p) {
                    try {
                        System.out.println("Thread" + workerId + " Have a nice sleep!");
                        polling = false;
                        lockObj.wait();
                    } catch (InterruptedException e) {
                        System.out.println("MessageReceiver Interrupted! QueueName is " + queueName);
                        return null;
                    }
                }
            }

            try {
                Message message = null;
                if (!polling) {
                    message = cloudQueue.popMessage();
                    if (message == null) {
                        polling = true;
                        continue;
                    }
                } else {
                    if (setPolling()) {
                        System.out.println("Thread" + workerId + " Polling!");
                    } else {
                        continue;
                    }
                    do {
                        try {
                            message = cloudQueue.popMessage(WAIT_SECONDS);
                        } catch(Exception e)
                        {
                            System.out.println("Exception Happened when polling popMessage: " + e);
                        }
                    } while (message == null);
                    clearPolling();
                }
                return message;
            } catch (Exception e) {
                // it could be network exception
                System.out.println("Exception Happened when popMessage: " + e);
            }
        }
    }
}
