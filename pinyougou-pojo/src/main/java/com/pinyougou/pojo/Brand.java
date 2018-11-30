package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;


@Table(name = "tb_brand")
public class Brand implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name="first_char")
    private String firstChar;


    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstChar='" + firstChar + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public Brand() {
    }

    public Brand(long id, String name, String firstChar) {
        this.id = id;
        this.name = name;
        this.firstChar = firstChar;
    }
}
