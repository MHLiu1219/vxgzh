package com.vxgzh.maoxiandao.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * BaiduImageSegtResult
 *
 * @Author: lmh
 * @CreateTime: 2020-09-01
 * @Description:
 */
public class BaiduImageSegtResult implements Serializable {

    /**
     * log_id : 7469706785468563257
     * results : [{"location":{"height":34,"left":245,"top":37,"width":34},"mask
     * ":"nnX14W50M4M3M3M2O2N2N1N3M3O0N3O001000O00010N101O0O2O1M3N1N3N2M2O2M3N2M20TaZ1","name":"d","score":0
     * .9983250498771667},{"location":{"height":35,"left":333,"top":51,"width":37},"mask
     * ":"YPh16S53M3M2O1O2N101M3M3N1O2O0O2N1010O1O001O00001N101M3N1O2M3N1O2M3N2MVek0","name":"d","score":0
     * .9931443929672241},{"location":{"height":37,"left":151,"top":48,"width":36},"mask
     * ":"^^i01V54N3M3M3N2N1N3N2M2O1N3M3O1O1O0011N01O01O001N101M3N2N1N3N1O2N2N2M3M20]li1","name":"a","score":0
     * .985828161239624},{"location":{"height":37,"left":397,"top":51,"width":37},"mask
     * ":"[fR24T54N2L4N2N2M2O2M2N3N1N3O1O1O0011N01O00010OO2O0O3M2N2N1O1O1O2L4N2L5K30\\d`0","name":"a","score":0
     * .9825958013534546}]
     */

    @SerializedName("log_id")
    private long logId;
    @SerializedName("results")
    private List<ResultsBean> results;

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * location : {"height":34,"left":245,"top":37,"width":34}
         * mask : nnX14W50M4M3M3M2O2N2N1N3M3O0N3O001000O00010N101O0O2O1M3N1N3N2M2O2M3N2M20TaZ1
         * name : d
         * score : 0.9983250498771667
         */

        @SerializedName("location")
        private ImageLocation location;
        @SerializedName("mask")
        private String mask;
        @SerializedName("name")
        private String name;
        @SerializedName("score")
        private Double score;

        public ImageLocation getLocation() {
            return location;
        }

        public void setLocation(ImageLocation location) {
            this.location = location;
        }

        public String getMask() {
            return mask;
        }

        public void setMask(String mask) {
            this.mask = mask;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public static class ImageLocation {
            /**
             * height : 34
             * left : 245
             * top : 37
             * width : 34
             */

            @SerializedName("height")
            private Integer height;
            @SerializedName("left")
            private Integer left;
            @SerializedName("top")
            private Integer top;
            @SerializedName("width")
            private Integer width;

            public Integer getHeight() {
                return height;
            }

            public void setHeight(Integer height) {
                this.height = height;
            }

            public Integer getLeft() {
                return left;
            }

            public void setLeft(Integer left) {
                this.left = left;
            }

            public Integer getTop() {
                return top;
            }

            public void setTop(Integer top) {
                this.top = top;
            }

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }
        }
    }
}