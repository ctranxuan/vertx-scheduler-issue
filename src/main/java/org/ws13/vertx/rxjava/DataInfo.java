package org.ws13.vertx.rxjava;

/**
 * @author ctranxuan
 */
public class DataInfo {
    private final String id;
    private final String data;

    public DataInfo(String aId, String aData) {
        id = aId;
        data = aData;
    }

    public String id() {
        return id;
    }

    public String data() {
        return data;
    }

//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        System.out.println("DataInfo.finalize");
//    }
}
