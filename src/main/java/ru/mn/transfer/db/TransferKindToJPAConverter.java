package ru.mn.transfer.db;

import javax.persistence.AttributeConverter;

public class TransferKindToJPAConverter implements AttributeConverter<TransferKind, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TransferKind attribute) {
        return attribute.getValue();
    }

    @Override
    public TransferKind convertToEntityAttribute(Integer dbData) {
        for (TransferKind transferKind: TransferKind.values()) {
            if (transferKind.getValue() == dbData) {
                return transferKind;
            }
        }
        throw new IllegalArgumentException(
                String.format("ошибка преобразования не известное целочисленное значение вида трансферта %d", dbData));
    }
}
