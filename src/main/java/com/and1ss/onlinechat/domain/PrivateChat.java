package com.and1ss.onlinechat.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "private_chat")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class PrivateChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    protected UUID id;

    @OneToOne
    @JoinColumn(name = "user_1_id", referencedColumnName = "id")
    private AccountInfo user1;

    @OneToOne
    @JoinColumn(name = "user_2_id", referencedColumnName = "id")
    private AccountInfo user2;
}
