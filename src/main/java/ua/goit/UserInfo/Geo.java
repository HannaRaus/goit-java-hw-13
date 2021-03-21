package ua.goit.UserInfo;

public  class Geo {
    private String lat;
    private String lng;

    public Geo(Builder builder) {
        this.lat = builder.lat;
        this.lng = builder.lng;
    }

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }

    public static class Builder {
        private String lat;
        private String lng;

        public Builder lat(String lat) {
            this.lat = lat;
            return this;
        }
        public Builder lng(String lng) {
            this.lng = lng;
            return this;
        }

        public Geo build() {
            return new Geo(this);
        }
    }
}
