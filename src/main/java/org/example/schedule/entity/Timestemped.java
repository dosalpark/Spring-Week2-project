package org.example.schedule.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestemped {

    @CreatedDate                        //최초 생성될때 시간 값이 createdAt에 저장
    @Column(updatable = false)          //컬럼의 업데이트를 제한
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate                   //마지막 변경된 시간이 modifiedAt에 저장
    @Column
    @Temporal(TemporalType.TIMESTAMP)   //DATE(2020-01-01), TIME(20:54), TIMESTAMP(2020-01-01 20:54) 데이터를 매핑 할 때 사용
    private LocalDateTime modifiedAt;
}
