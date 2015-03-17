package main.java.com.ipdiscovery.utils;

import java.util.ArrayList;
import java.util.HashSet;

import main.java.com.ipdiscovery.types.IpRange;

public class IpUtils {

    public static long ipToLong(String addr) {
        String[] addrArray = addr.split("\\.");

        long num = 0;

        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }
        return num;
    }

    public static HashSet<IpRange> isIpRangeInSet(IpRange ipRange, HashSet<IpRange> globalIpList) {

        for (IpRange validIps : globalIpList) {

            if (ipRange.getLowLong() >= validIps.getLowLong()
                    && ipRange.getHighLong() <= validIps.getHighLong()) {
                return globalIpList; //ipRange is in list, return
            }

            if (ipRange.getLowLong() >= validIps.getLowLong()
                    && ipRange.getHighLong() >= validIps.getHighLong()) {

                validIps.setHighLong(ipRange.getHighLong()); //low is valid, add new high
                return globalIpList;
            }
            if (ipRange.getLowLong() >= validIps.getLowLong()
                    && ipRange.getHighLong() >= validIps.getHighLong()) {

                validIps.setLowLong(ipRange.getLowLong()); //high is valid, add low high
                return globalIpList;
            }

        }

        globalIpList.add(ipRange);

        return globalIpList;
    }

    public static IpRange getNextIpAddress(long l) { // Use bitwise bit shifting to build the new IP
        String currentIp = ((l >> 24) & 0xFF) + "." + ((l >> 16) & 0xFF) + "." + ((l >> 8) & 0xFF)
                + "." + (l & 0xFF);

        IpRange ip = new IpRange(currentIp);
        IpRange nextIP = getNextIpAddressString(ip.getLow());
        return nextIP;
    }

    public static ArrayList<IpRange> getIpRange(IpRange range) {
        ArrayList<IpRange> iprange = new ArrayList<IpRange>();

        IpRange next = range;
        iprange.add(range);
        while (!(next.getLowLong() == range.getHighLong())) {
            iprange.add(next);
            next = getNextIpAddress(next.getLowLong());
        }
        iprange.add(range);
        return iprange;
    }

    public static IpRange getNextIpAddressString(String input) {
        final String[] tokens = input.split("\\.");
        if (tokens.length != 4)
            throw new IllegalArgumentException();
        for (int i = tokens.length - 1; i >= 0; i--) {
            final int item = Integer.parseInt(tokens[i]);
            if (item < 255) {
                tokens[i] = String.valueOf(item + 1);
                for (int j = i + 1; j < 4; j++) {
                    tokens[j] = "0";
                }
                break;
            }
        }

        String ip = new StringBuilder().append(tokens[0]).append('.').append(tokens[1]).append('.')
                .append(tokens[2]).append('.').append(tokens[3]).toString();

        return new IpRange(ip);
    }
}
