package main.java.com.ipdiscovery.sites;

import java.util.HashSet;

import main.java.com.ipdiscovery.types.IpRange;

public interface IpDiscoverySiteInterface {

    public String getSiteName();

    public IpRange retrieveNetBlocks(IpRange ipAddress, HashSet<String> names);

}
