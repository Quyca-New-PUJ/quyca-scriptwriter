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

/**
 * The IpScanner class scans a range of IP addresses in the network to check for active devices.
 * It uses multithreading to parallelize the scanning of IPs for faster processing.
 * The scan method attempts to connect to each IP within a specified range on port 6000,
 * and collects IP addresses that respond within a timeout period.
 */
public class IpScanner {

    /**
     * Scans a range of IP addresses in the network to find active devices.
     *
     * @param firstIpInTheNetwork The starting IP address in the network (e.g., "192.168.1.0").
     * @param numOfIps The number of IPs to scan (e.g., 254 would scan from "192.168.1.0" to "192.168.1.254").
     * @return A list of active IP addresses that responded to the scan.
     */
    public static List<String> scan(String firstIpInTheNetwork, int numOfIps) {
        // Create a thread pool with 20 threads to scan IP addresses concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // Extract the network part of the IP address by removing the last octet
        final String networkId = firstIpInTheNetwork.substring(0, firstIpInTheNetwork.length() - 1);

        // A thread-safe set to store the IPs that respond to the scan
        ConcurrentSkipListSet<String> ipsSet = new ConcurrentSkipListSet<>();

        // AtomicInteger to increment and generate each IP address in the range
        AtomicInteger ips = new AtomicInteger(0);

        // Submit tasks to the thread pool to scan each IP in the range
        while (ips.get() <= numOfIps) {
            // Generate the next IP address by appending the incremented value to the network ID
            String ip = networkId + ips.getAndIncrement();
            executorService.submit(() -> {
                // Create a socket address with the generated IP and port 6000
                SocketAddress sockaddr = new InetSocketAddress(ip, 6000);
                try (Socket socket = new Socket()) {
                    // Attempt to connect to the IP with a timeout of 500ms
                    socket.connect(sockaddr, 500);
                    // If the connection succeeds, add the IP to the set of active IPs
                    ipsSet.add(ip);
                } catch (IOException ignored) {
                    // Ignore exceptions as they indicate the IP is not active or reachable
                }
            });
        }

        // Shutdown the executor service after submitting all tasks
        executorService.shutdown();
        try {
            // Await termination of all tasks or timeout after 1 minute
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Return the list of active IP addresses that responded to the scan
        return new ArrayList<>(ipsSet);
    }
}
