package io.javabrains.inbox.emailList;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Data
@Table(value="message_by_user_folder")
public class EmailListItem {

    @PrimaryKey
    private EmailListItemKey key;

    @CassandraType(type= CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> to;

    @CassandraType(type= CassandraType.Name.TEXT)
    private String subject;

    @CassandraType(type= CassandraType.Name.BOOLEAN)
    private boolean isUnread;

    private String agoTimeString;
}
