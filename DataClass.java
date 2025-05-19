package com.example.loginsignup;

public class DataClass {
   // private String switch1;  //change
    private String dataDname;  //change3
    private String dataDid;             // change2
    private String dataLang;
    private String dataImage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataDname() {
        return dataDname;
    }  //change3
    //public String getswitch1() {
       // return switch1;
    ///}///change

    public String getDataDid() { return dataDid; }     // change2

    public String getDataLang() {
        return dataLang;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataDname, String dataDid, String dataLang, String dataImage) {//change 2 //change3 String switch1,
       // this.switch1 = switch1;//change
        this.dataDname = dataDname; //change3
        this.dataDid = dataDid;  //change2
        this.dataLang = dataLang;
        this.dataImage = dataImage;
    }

    public DataClass(){

    }

}
