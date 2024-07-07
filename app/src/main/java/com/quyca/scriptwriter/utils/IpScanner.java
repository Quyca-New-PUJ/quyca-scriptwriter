package com.quyca.scriptwriter.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// https://www.theswdeveloper.com/post/scan-for-devices-in-the-network-with-java
// https://stackoverflow.com/questions/14905982/java-specifying-port-with-inetaddress

/*
  List<String> networkIps = scan("192.168.1.0", 254);
 */
public class IpScanner {
    /**
     *
     * @param firstIpInTheNetwork e.g: 192.168.1.0
     * @param numOfIps e.g: 254
     * @return
     */
    public static List<String> scan(String firstIpInTheNetwork, int numOfIps) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final String networkId = firstIpInTheNetwork.substring(0, firstIpInTheNetwork.length() - 1);
        ConcurrentSkipListSet<String> ipsSet = new ConcurrentSkipListSet<String>();

        AtomicInteger ips = new AtomicInteger(0);
        while (ips.get() <= numOfIps) {
            String ip = networkId + ips.getAndIncrement();
            executorService.submit(() -> {
                SocketAddress sockaddr = new InetSocketAddress(ip, 6000);
                try (Socket socket = new Socket()) {
                    socket.connect(sockaddr, 500);
                    ipsSet.add(ip);
                } catch (IOException ignored) {

                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<String>(ipsSet);
    }
}

