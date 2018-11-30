package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name="tb_specification")
public class Specification implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="spec_name")
    private String specName;
    @Transient
    private List<SpecificationOption> specificationOptionList;

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }

    public Specification(Long id, String specName) {
        this.id = id;
        this.specName = specName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Specification() {
    }


}
