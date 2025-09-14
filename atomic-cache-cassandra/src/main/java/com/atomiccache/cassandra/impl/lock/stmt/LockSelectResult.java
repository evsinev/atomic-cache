package com.atomiccache.cassandra.impl.lock.stmt;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Builder
@AllArgsConstructor
public class LockSelectResult {
    String key;
    String owner;
}
