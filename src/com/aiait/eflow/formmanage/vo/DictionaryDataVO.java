package com.aiait.eflow.formmanage.vo;

public class DictionaryDataVO {
    private String id;
    private String value;
    private String misc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the misc
     */
    public String getMisc() {
        return misc;
    }

    /**
     * @param misc
     *            the misc to set
     */
    public void setMisc(String misc) {
        this.misc = misc;
    }
}
