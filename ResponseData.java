package chat;

public class ResponseData {
    private int intValue;
    private String strValue;

    int getIntValue() {
        return intValue;
    }
    void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    int getStringValue() {
        return intValue;
    }
    void setStringValue(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public String toString() {
        return "" + intValue;
    }
}