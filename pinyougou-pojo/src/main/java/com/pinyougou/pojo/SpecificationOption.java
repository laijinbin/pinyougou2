package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "tb_specification_option")
public class SpecificationOption implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "option_name")
    private String optionName;
    @Column(name = "spec_id")
    private Long specId;
    @Column(name="orders")
    private int orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public SpecificationOption() {
    }

    public SpecificationOption(Long id, String optionName, Long specId, int orders) {
        this.id = id;
        this.optionName = optionName;
        this.specId = specId;
        this.orders = orders;
    }


}
