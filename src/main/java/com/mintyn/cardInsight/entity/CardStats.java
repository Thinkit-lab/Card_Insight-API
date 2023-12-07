package com.mintyn.cardInsight.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CardStats extends BaseModel{
    private String binNumber;
    private Long hits;
}
