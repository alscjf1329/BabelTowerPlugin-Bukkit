package org.dev.babeltower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BabelRewardDTO {
    private final String title;
    private final String serializedReward;
}
