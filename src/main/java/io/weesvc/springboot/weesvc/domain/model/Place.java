package io.weesvc.springboot.weesvc.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@Table("places")
public class Place {

    @Id
    private Long id;
    private String name;
    private String description;
    private Float latitude;
    private Float longitude;
    private Instant createdAt;
    private Instant updatedAt;

}
