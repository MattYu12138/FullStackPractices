package com.matt.wiki.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "clients")
@Comment("客户表")
public class GuestRegisterEntity extends BaseEntity {

    @Comment("用户姓名")
    private String userName;


    @Comment("手机号码")
    private String phone;


    @Comment("邮箱")
    private String email;


    @Comment("案件类型")
    private String caseType;


    @Comment("地区")
    private String area;


    @Comment("语言（zh/en/both/other 等）")
    private String language;


    @Comment("紧急程度（low/medium/high）")
    private String urgency;

}