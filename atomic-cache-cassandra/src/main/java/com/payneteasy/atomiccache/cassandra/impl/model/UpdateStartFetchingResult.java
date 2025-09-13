package com.payneteasy.atomiccache.cassandra.impl.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Builder
public class UpdateStartFetchingResult {
    boolean applied;
    UUID    fetchId;
}
