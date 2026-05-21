package com.galgallery.common;

import java.util.List;

public record PageResult<T>(List<T> records, Long total, Long page, Long size) {
}

