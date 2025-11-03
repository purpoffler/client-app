package layer.dto;

import layer.enums.ExpectedDataType;

public record Message(String data, ExpectedDataType dataType) {
}
