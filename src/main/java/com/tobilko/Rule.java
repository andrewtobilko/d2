package com.tobilko;

import org.hibernate.annotations.CollectionType;

import javax.persistence.*;

/**
 * Created by Andrew Tobilko on 10/28/18.
 */
@Entity
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String role;

    @OrderColumn
    @ElementCollection
    @CollectionTable(joinColumns=@JoinColumn(name="id"))
    private String[] allowedURIs;

    public Rule() {
    }

    public Rule(String role, String[] allowedURIs) {
        this.role = role;
        this.allowedURIs = allowedURIs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getAllowedURIs() {
        return allowedURIs;
    }

    public void setAllowedURIs(String[] allowedURIs) {
        this.allowedURIs = allowedURIs;
    }
}
