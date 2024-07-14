package io.javabrains.inbox.folders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "folders_by_user")
public class Folder {

    @PrimaryKeyColumn(name = "user_id", ordinal=0, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "label", ordinal=1, type = PrimaryKeyType.CLUSTERED)
    private String label;

    @CassandraType(type= CassandraType.Name.TEXT)
    private String color;

}
