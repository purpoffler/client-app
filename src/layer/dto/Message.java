package layer.dto;

import layer.enums.ExpectedDataType;

import java.io.Serializable;

public class Message implements Serializable {
    private final String data;
    private final ExpectedDataType dataType;

    public Message(String data, ExpectedDataType dataType) {
        this.data = data;
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public ExpectedDataType getDataType() {
        return dataType;
    }
}
