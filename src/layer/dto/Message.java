package layer.dto;

import layer.enums.ExpectedDataType;

public class Message {
    private final String data;
    private final ExpectedDataType dataType;

    public Message(String data, ExpectedDataType dataType) {
        this.data = data;
        this.dataType = dataType;
    }

    public Message(boolean isContinue) {
        this.data = null;
        this.dataType = null;
    }

    public String getData() {
        return data;
    }

    public ExpectedDataType getDataType() {
        return dataType;
    }
}
