package com.matt.wiki.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "clients")
@Comment("客户表")
@DynamicInsert
@DynamicUpdate
public class GuestRegisterEntity extends BaseEntity {

    @Column(name = "user_name", length = 100)
    @Comment("用户姓名")
    private String userName;

    @Column(name = "phone", length = 32)
    @Comment("手机号码")
    private String phone;

    @Column(name = "email", length = 255)
    @Comment("邮箱")
    private String email;

    @Column(name = "case_type", length = 120)
    @Comment("案件类型")
    private String caseType;

    @Column(name = "area", length = 120)
    @Comment("地区")
    private String area;

    @Column(name = "language", length = 16)
    @Comment("语言（zh/en/both/other 等）")
    private String language;

    @Column(name = "urgency", length = 16)
    @Comment("紧急程度（low/medium/high）")
    private String urgency;

    @Column(name = "completed", nullable = false)
    @Comment("是否完成登记：1=是，0=否")
    private Boolean completed = Boolean.FALSE;

    @Lob
    @Column(name = "output", columnDefinition = "TEXT")
    @Comment("LLM output to Guest")
    private String output;
}