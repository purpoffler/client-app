package layer.dto;

import layer.enums.ExpectedDataType;

import java.io.Serializable;

public class Message implements Serializable {
    private final String data;
    private final ExpectedDataType dataType;
    private final boolean isContinue;

    public Message(String data, ExpectedDataType dataType) {
        this.data = data;
        this.dataType = dataType;
        this.isContinue = true;
    }

    public Message(boolean isContinue) {
        this.data = null;
        this.dataType = null;
        this.isContinue = isContinue;
    }

    public String getData() {
        return data;
    }

    public ExpectedDataType getDataType() {
        return dataType;
    }

    public boolean isContinue() {
        return isContinue;
    }
}
