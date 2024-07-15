package io.javabrains.inbox.emailList;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Data
@PrimaryKeyClass
public class EmailListItemKey {

    @PrimaryKeyColumn(name="user_id", ordinal=0, type= PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name="label", ordinal=1, type= PrimaryKeyType.PARTITIONED)
    private String label;

    @PrimaryKeyColumn(name="created_time_uuid", ordinal=2, type= PrimaryKeyType.CLUSTERED)
    private UUID timeUUID;
}
