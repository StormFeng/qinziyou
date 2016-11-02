package com.midian.maplib.map;

import java.util.List;


public class ItemBean {
	private String id;
	private String pic;
	private String lat;
	private String lon;
	private String name;
    private String countyid;//区id
    private String carrierCount;
    private List<Traffics> traffics;
    private Carrier carrier;



    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

    public String getCarrierCount() {
        return carrierCount;
    }

    public void setCarrierCount(String carrierCount) {
        this.carrierCount = carrierCount;
    }

    public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public ItemBean(String id, String lat, String lon,String name,String countyid,String carrierCount) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.name=name;
		this.id=id;
        this.countyid=countyid;
        this.carrierCount = carrierCount;
    }

	public ItemBean() {
		super();
	}


    public class Carrier {
        private String carrierid;
        private String shortName;
        private String carrierType;
        private String lng;
        private String lat;

        public String getCarrierid() {
            return carrierid;
        }

        public void setCarrierid(String carrierid) {
            this.carrierid = carrierid;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getCarrierType() {
            return carrierType;
        }

        public void setCarrierType(String carrierType) {
            this.carrierType = carrierType;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }

    public class Traffics  {
        private String trafficsid;
        private String trafficsName;
        private String trafficstype;
        private String trafficstypeid;
        private String lng;
        private String lat;

        public String getTrafficsid() {
            return trafficsid;
        }

        public void setTrafficsid(String trafficsid) {
            this.trafficsid = trafficsid;
        }

        public String getTrafficsName() {
            return trafficsName;
        }

        public void setTrafficsName(String trafficsName) {
            this.trafficsName = trafficsName;
        }

        public String getTrafficstype() {
            return trafficstype;
        }

        public void setTrafficstype(String trafficstype) {
            this.trafficstype = trafficstype;
        }

        public String getTrafficstypeid() {
            return trafficstypeid;
        }

        public void setTrafficstypeid(String trafficstypeid) {
            this.trafficstypeid = trafficstypeid;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }


}