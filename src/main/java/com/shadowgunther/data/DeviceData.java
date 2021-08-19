package com.shadowgunther.data;

public class DeviceData {
    private final String RA;
    private final String RB;
    private final String RC;
    private final String IRA;
    private final String IRB;
    private final String IRC;

    public DeviceData(String ra, String rb, String rc, String ira, String irb, String irc) {
        RA = ra;
        RB = rb;
        RC = rc;
        IRA = ira;
        IRB = irb;
        IRC = irc;
    }

    public String getRA() {
        return RA;
    }

    public String getRB() {
        return RB;
    }

    public String getRC() {
        return RC;
    }

    public String getIRA() {
        return IRA;
    }

    public String getIRB() {
        return IRB;
    }

    public String getIRC() {
        return IRC;
    }


    public String[] getArray() {
        return new String[]{RA, RB, RC, IRA, IRB, IRC};
    }
}
