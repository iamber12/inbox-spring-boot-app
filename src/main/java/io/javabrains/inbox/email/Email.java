package io.javabrains.inbox.email;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Data
@Table(value="message_by_id")
public class Email {

    @Id
    @PrimaryKeyColumn(name="id", ordinal=0, type= PrimaryKeyType.PARTITIONED)
    private UUID id;

    @CassandraType(type= CassandraType.Name.TEXT)
    private String from;

    @CassandraType(type= CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> to;

    @CassandraType(type= CassandraType.Name.TEXT)
    private String subject;

    @CassandraType(type= CassandraType.Name.TEXT)
    private String body;
}
