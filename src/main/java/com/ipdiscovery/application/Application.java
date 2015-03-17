package main.java.com.ipdiscovery.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.java.com.ipdiscovery.sites.impl.Arin;
import main.java.com.ipdiscovery.types.IpRange;
import main.java.com.ipdiscovery.utils.IpUtils;

public class Application {

    public static void main(String[] args) {
        List<IpRange> ipRanges = readFileForIPS("C:/Users/Nick/Desktop/gs.txt");
        Arin arin = new Arin("arin");
        HashSet<String> names = new HashSet<String>();
        names.add("Goldman Sachs");

        HashSet<IpRange> globalList = new HashSet<IpRange>();

        for (IpRange ipAddress : ipRanges) {
            IpRange ipRange = arin.retrieveNetBlocks(ipAddress, names);
            System.out.println("Middle " + globalList.size());
            if (!ipRange.isEmpty()) {
                globalList = IpUtils.isIpRangeInSet(ipRange, globalList);
            }
        }

        System.out.println("End " + globalList.size());
    }

    public static List<IpRange> readFileForIPS(String path) {
        List<IpRange> ranges = new ArrayList<IpRange>();

        try {
            FileInputStream fis = new FileInputStream(new File(path));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] args = line.split("-");
                IpRange range = new IpRange();
                if (args.length == 1) {
                    range = new IpRange(args[0]);
                } else {
                    range = new IpRange(args[0], args[1]);
                }
                ranges.add(range);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return ranges;
    }

}
