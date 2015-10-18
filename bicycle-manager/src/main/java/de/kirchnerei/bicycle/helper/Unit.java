package de.kirchnerei.bicycle.helper;

import de.kirchnerei.bicycle.R;

public enum Unit {

    DISTANCE(R.string.unit_format_distance),

    SPEED(R.string.unit_format_speed);

    private final int formatId;

    Unit(int formatId) {
        this.formatId = formatId;
    }

    public int getFormatId() {
        return formatId;
    }
}
