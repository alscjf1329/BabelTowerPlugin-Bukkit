package org.dev.babeltower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateDTO {

    private Location pos1;
    private Location pos2;

    public boolean isFull() {
        return pos1 != null && pos2 != null;
    }
}
