package com.midian.maplib.map;

import java.util.List;

public class ItemBean1 {
    private List<String> traffics;
    private Carrier carrier;


    public ItemBean1() {
        super();
    }

    public ItemBean1(Carrier carrier, List<String> traffics) {

    }


    public class Carrier {
        private String carrierid;
        private String shortName;
        private String carrierType;
        private String lng;
        private String lat;

    }

    public class Traffics {
        private String trafficsid;
        private String trafficsName;
        private String trafficstype;
        private String trafficstypeid;
        private String lng;
        private String lat;

    }


}