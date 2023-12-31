package org.lenuscreations.lelib.pterodactyl.admin.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class PteroLocation {

    private final int id;
    private final String shortCode;
    private final String longCode;
    private final String description;
    private final String createdAt;
    private final String updatedAt;

}
