package main.java.com.ipdiscovery.types;

public class IpRange {

    private String low;
    private String high;
    private String range;
    private long lowLong;
    private long highLong;
    private boolean isEmpty;

    public IpRange() {
        isEmpty = true;
    }

    public IpRange(long ip) {
        this.lowLong = ip;
        this.highLong = ip;
        this.isEmpty = false;
        //formatToString();
    }

    public IpRange(String ip) {
        this.low = ip;
        this.high = ip;
        this.isEmpty = false;
        formatToRange();
    }

    public IpRange(String low, String high) {
        this.low = low;
        this.high = high;
        this.isEmpty = false;
        formatToRange();
        formatToLong();
    }

    public String getLow() {
        return low;
    }

    public String getHigh() {
        return high;
    }

    public String getRange() {
        return range;
    }

    private void formatToRange() {
        range = this.low + "-" + this.high;
    }

    public long getLowLong() {
        return lowLong;
    }

    public long getHighLong() {
        return highLong;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setLowLong(long lowLong) {
        this.lowLong = lowLong;
    }

    public void setHighLong(long highLong) {
        this.highLong = highLong;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    private void formatToLong() {
        String[] lowaddrArray = low.split("\\.");
        String[] highaddrArray = high.split("\\.");

        long lownum = 0;
        long highnum = 0;

        for (int i = 0; i < lowaddrArray.length; i++) {
            int power = 3 - i;
            lownum += ((Integer.parseInt(lowaddrArray[i]) % 256 * Math.pow(256, power)));
        }
        for (int i = 0; i < highaddrArray.length; i++) {
            int power = 3 - i;
            highnum += ((Integer.parseInt(highaddrArray[i]) % 256 * Math.pow(256, power)));
        }
        this.highLong = highnum;
        this.lowLong = lownum;
    }

}
