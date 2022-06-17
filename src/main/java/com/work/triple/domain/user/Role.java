package com.work.triple.domain.user;


import lombok.*;

import javax.persistence.*;

@Getter @NoArgsConstructor
@Builder @AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

}
