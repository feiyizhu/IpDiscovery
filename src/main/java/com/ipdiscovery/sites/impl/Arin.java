package main.java.com.ipdiscovery.sites.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.java.com.ipdiscovery.sites.IpDiscoverySiteInterface;
import main.java.com.ipdiscovery.types.IpRange;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Arin implements IpDiscoverySiteInterface {

    private String siteName;

    public Arin(String siteName) {
        this.siteName = siteName;
    }

    @Override
    public String getSiteName() {
        return siteName;
    }

    @Override
    public IpRange retrieveNetBlocks(IpRange ipAddress, HashSet<String> names) {
        HttpClient httpClient = new DefaultHttpClient();
        IpRange blocks = new IpRange();
        List<String> ipList = new ArrayList<String>();
        ipList.add(ipAddress.getLow());
        ipList.add(ipAddress.getHigh());

        for (String ip : ipList) {
            try {
                HttpGet request = new HttpGet("http://whois.arin.net/rest/net/NET-12-3-17-0-1/pft");

                request.addHeader("accept", "application/json");
                HttpResponse response = httpClient.execute(request);

                InputStream ips = response.getEntity().getContent();
                BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String s;
                while (true) {
                    s = buf.readLine();
                    System.out.println();
                    if (s == null || s.length() == 0)
                        break;
                    sb.append(s);

                }
                buf.close();
                ips.close();

                JsonParser parser = new JsonParser();
                JsonObject obj = (JsonObject) parser.parse(sb.toString());
                JsonObject company;
                String orgName = "";
                boolean orgFlag = false;
                if (obj.has("net")) {
                    company = obj.get("net").getAsJsonObject();

                    //get orgName
                    if (company.has("orgRef")) {
                        JsonObject org = company.get("orgRef").getAsJsonObject();
                        orgName = org.get("@name").getAsString();
                    }

                    //check to see a match
                    for (String name : names) {
                        if (orgName.contains(name)) {
                            orgFlag = true;
                        }
                    }

                    if (orgFlag) {
                        if (company.has("netBlocks")) {
                            JsonObject netBlocks = company.get("netBlocks").getAsJsonObject();
                            if (netBlocks.has("netBlock")) {
                                JsonObject netBlock = netBlocks.get("netBlock").getAsJsonObject();
                                if (netBlock.has("startAddress") && netBlock.has("endAddress")) {
                                    JsonObject start = netBlock.get("startAddress")
                                            .getAsJsonObject();
                                    JsonObject end = netBlock.get("endAddress").getAsJsonObject();

                                    String st = start.get("$").getAsString();
                                    String e = end.get("$").getAsString();

                                    return blocks = new IpRange(st, e);
                                }

                            }
                        }
                    }

                }

            } catch (JsonSyntaxException e) {
                System.out.println(e);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return blocks;

    }

    public static IpRange formatIp(String ip) {

        if (ip.contains("-")) {
            String[] lowHigh = ip.split("-");
            return new IpRange(lowHigh[0], lowHigh[1]);
        } else {
            return new IpRange(ip, ip);
        }
    }
}
