package org.lenuscreations.lelib.utils.reflection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Parameter;

@Data
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LParameter {

    private final Parameter parameter;

    private final String name;
    private final Class<?> type;


}
