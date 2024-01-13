/*
 * Copyright (c) 2024. Intel
 *
 * This file is part of LuminaryOS
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.luminary.os.plugin;

import lombok.Data;

@Data
public class PluginDescription {
    private final String name;
    private final String version;
    private final String description;
    private final String author;

    @Override
    public String toString() {
        return "\tPluginDescription" +
                "\n\t\tname=" + name +
                "\n\t\tversion=" + version +
                "\n\t\tdescription=" + description +
                "\n\t\tauthor=" + author;
    }
}
