package org.dev.babeltower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoordinateDTO{
    private final long x;
    private final long y;
    private final long z;
}
