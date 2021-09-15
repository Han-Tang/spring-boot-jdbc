package com.lou.springboot.controller;


import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class ResponseBody<T> implements Serializable {
    private Return rtn;

    private ResponseData businessData;

    public ResponseBody() {}
    public ResponseBody(T data, ReturnCode returnCode, String rtnMsg, String compresse) {
        rtn = new Return();
        rtn.setRtnCode(returnCode.code());
        if (StringUtils.isBlank(rtnMsg)) {
            rtn.setRtnMsg(returnCode.name());
        } else {
            rtn.setRtnMsg(rtnMsg);
        }
        businessData = new ResponseData();
        businessData.setCompresse(compresse);
        businessData.setData(data);
    }


    public Return getRtn() {
        return rtn;
    }

    public void setRtn(Return rtn) {
        this.rtn = rtn;
    }

    public ResponseData getBusinessData() {
        return businessData;
    }

    public void setBusinessData(ResponseData businessData) {
        this.businessData = businessData;
    }


    static class ResponseData<T> {
        private String compresse;
        private T data;

        public String getCompresse() {
            return compresse;
        }

        public void setCompresse(String compresse) {
            this.compresse = compresse;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    static class Return {
        int rtnCode;
        private String rtnMsg;

        public int getRtnCode() {
            return rtnCode;
        }

        public void setRtnCode(int rtnCode) {
            this.rtnCode = rtnCode;
        }

        public String getRtnMsg() {
            return rtnMsg;
        }

        public void setRtnMsg(String rtnMsg) {
            this.rtnMsg = rtnMsg;
        }
    }


}
