package com.fiordelisi.fiordelisiproduct.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    private String username;
    private String password;
    private String email;
    private String createdBy;
    @CreatedDate
    LocalDateTime createTime;
    @LastModifiedDate
    LocalDateTime lastUpdateTime;
}
