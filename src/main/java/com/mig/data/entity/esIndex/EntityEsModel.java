package com.mig.data.entity.esIndex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @description: EntityEsModel
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 09:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityEsModel {

    @Id
    private String id;
}
